package com.getitemfromblock.create_tweaked_controllers.input;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Vector;

import org.lwjgl.glfw.GLFW;

public class JoystickInputs
{
    private static Vector<Boolean> buttons = new Vector<>(0);
    private static Vector<Float> axis = new Vector<>(0);
    private static Vector<Float> storedAxis = new Vector<>(0);

    protected static int selectedJoystick = -1;

    public static void GetControls()
    {
        if (selectedJoystick < 0)
        {
            int uniqueJoystickID = -1;
            for (int i = 0; i < 16 && selectedJoystick < 0; i++)
            {
                if (!GLFW.glfwJoystickPresent(i)) continue;
                if (uniqueJoystickID == -1) // At least one joystick is found
                {
                    uniqueJoystickID = i;
                }
                else if (uniqueJoystickID >= 0) // More than one joystick is found
                {
                    uniqueJoystickID = -2;
                }
                ByteBuffer res = GLFW.glfwGetJoystickButtons(i); // Check for joystick activity
                if (res == null) continue;
                for (int b = 0; b < res.limit(); b++)
                {
                    if (res.get(b) != GLFW.GLFW_PRESS) continue;
                    SelectJoystick(i);
                    break;
                }
            }
            if (selectedJoystick < 0 && uniqueJoystickID >= 0) // Exactly one joystick is found, no need to check for activity
            {
                SelectJoystick(uniqueJoystickID);
            }
        }
        if (selectedJoystick >= 0 && GLFW.glfwJoystickPresent(selectedJoystick))
        {
            ByteBuffer b = GLFW.glfwGetJoystickButtons(selectedJoystick);
            FloatBuffer a = GLFW.glfwGetJoystickAxes(selectedJoystick);
            if (b == null || buttons.size() != b.limit()
                || a == null || axis.size() != a.limit())
            {
                Empty();
                selectedJoystick = -1;
            }
            else
            {
                Fill(b, a);
            }
        }
        else
        {
            Empty();
            selectedJoystick = -1;
        }
    }

    private static void SelectJoystick(int id)
    {
        selectedJoystick = id;
        ByteBuffer b = GLFW.glfwGetJoystickButtons(selectedJoystick);
        buttons = new Vector<>(b.limit());
        for (int i = 0; i < b.limit(); i++)
        {
            buttons.add(false);
        }
        FloatBuffer a = GLFW.glfwGetJoystickAxes(selectedJoystick);
        axis = new Vector<>(a.limit());
        storedAxis = new Vector<>(a.limit());
        for (int i = 0; i < a.limit(); i++)
        {
            axis.add(0.0f);
            storedAxis.add(0.0f);
        }
    }

    public static int GetButtonCount()
    {
        return HasJoystick() ? buttons.size() : 0;
    }

    public static int GetAxisCount()
    {
        return HasJoystick() ? axis.size() : 0;
    }

    public static int GetJoystickIndex()
    {
        return selectedJoystick;
    }

    public static boolean HasJoystick()
    {
        return selectedJoystick >= 0;
    }

    public static void SearchGamepad()
    {
        selectedJoystick = -1;
    }

    public static boolean GetButton(int button)
    {
        return button >= GetButtonCount() ? false : JoystickInputs.buttons.get(button);
    }

    public static float GetAxis(int axis)
    {
        return axis >= GetAxisCount() ? 0.0f : JoystickInputs.axis.get(axis);
    }

    public static void Empty()
    {
        for (int i = 0; i < buttons.size(); i++)
        {
            buttons.set(i, false);
        }
        for (int i = 0; i < axis.size(); i++)
        {
            axis.set(i, 0.0f);
        }
    }

    public static void Fill(ByteBuffer b, FloatBuffer a)
    {
        for (int i = 0; i < b.limit(); i++)
        {
            buttons.set(i, b.get(i) == GLFW.GLFW_PRESS);
        }
        for (int i = 0; i < a.limit(); i++)
        {
            axis.set(i, a.get(i));
        }
    }

    public static void StoreAxisValues()
    {
        for (int i = 0; i < axis.size(); i++)
        {
            storedAxis.set(i, axis.get(i));
        }
    }

    public static int GetFirstButton()
    {
        for (int i = 0; i < buttons.size(); i++)
        {
            if (buttons.get(i)) return i;
        }
        return -1;
    }

    public static int GetFirstAxis()
    {
        for (int i = 0; i < axis.size(); i++)
        {
            if (Math.abs(axis.get(i) - storedAxis.get(i)) > 0.75f) return i;
        }
        return -1;
    }

    public static float GetStoredAxis(int index)
    {
        return storedAxis.get(index);
    }
}
