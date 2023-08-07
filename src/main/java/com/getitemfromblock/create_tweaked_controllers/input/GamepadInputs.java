package com.getitemfromblock.create_tweaked_controllers.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

public class GamepadInputs
{
    static final public boolean buttons[] = new boolean[15];
    static final public float axis[] = new float[6];

    protected static GLFWGamepadState state = null;
    protected static int selectedGamepad = -1;

    static final private String[] buttonNames =
    {
        "Gamepad A",
        "Gamepad B",
        "Gamepad X",
        "Gamepad Y",
        "Gamepad Left Shoulder",
        "Gamepad Right Shoulder",
        "Gamepad Back",
        "Gamepad Start",
        "Gamepad Guide",
        "Gamepad Left joystick Click",
        "Gamepad Right joystick Click",
        "Gamepad DPad Up",
        "Gamepad DPad Right",
        "Gamepad DPad Down",
        "Gamepad DPad Left"
    };

    static final private String[] axisNames =
    {
        "Gamepad Left +X Axis",
        "Gamepad Left -X Axis",
        "Gamepad Left +Y Axis",
        "Gamepad Left -Y Axis",
        "Gamepad Right +X Axis",
        "Gamepad Right -X Axis",
        "Gamepad Right +Y Axis",
        "Gamepad Right -Y Axis",
        "Gamepad Left Trigger Axis",
        "Gamepad Right Trigger Axis"
    };

    public static void GetControls()
    {
        CheckState();
        if (selectedGamepad < 0)
        {
            int uniqueGamepadID = -1;
            for (int i = 0; i < 16 && selectedGamepad < 0; i++)
            {
                if (!GLFW.glfwJoystickIsGamepad(i)) continue;
                if (uniqueGamepadID == -1) // At least one gamepad is found
                {
                    uniqueGamepadID = i;
                }
                else if (uniqueGamepadID >= 0) // More than one gamepad is found
                {
                    uniqueGamepadID = -2;
                }
                GLFW.glfwGetGamepadState(i, state); // Check for gamepad activity
                for (int b = 0; b < 15; b++)
                {
                    if (state.buttons(i) == 0) continue;
                    selectedGamepad = i;
                    break;
                }
            }
            if (selectedGamepad < 0 && uniqueGamepadID >= 0) // Exactly one gamepad is found, no need to check for activity
            {
                selectedGamepad = uniqueGamepadID;
            }
        }
        if (selectedGamepad < 0 || !GLFW.glfwJoystickIsGamepad(selectedGamepad))
        {
            Empty();
            selectedGamepad = -1;
        }
        else
        {
            GLFW.glfwGetGamepadState(selectedGamepad, state);
            Fill();
        }
	}

    public static int GetGamepadIndex()
    {
        return selectedGamepad;
    }

    public static boolean HasGamepad()
    {
        return selectedGamepad >= 0;
    }

    public static void SearchGamepad()
    {
        selectedGamepad = -1;
    }

	public static boolean GetButton(int button)
    {
		return GamepadInputs.buttons[button];
	}

    public static float GetAxis(int axis)
    {
		return GamepadInputs.axis[axis];
	}

    private static void CheckState()
    {
        if (state == null)
        {
            state = GLFWGamepadState.create();
        }
    }

    public static void Empty()
    {
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = false;
        }
        for (int i = 0; i < axis.length; i++)
        {
            axis[i] = i < 4 ? 0.0f : -1.0f;
        }
    }

    private static void Fill()
    {
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = state.buttons(i) != 0;
        }
        for (int i = 0; i < axis.length; i++)
        {
            axis[i] = state.axes(i);
        }
    }

    public static final String GetButtonName(int index)
    {
        return buttonNames[index];
    }

    public static final String GetAxisName(int index)
    {
        return axisNames[index];
    }
}
