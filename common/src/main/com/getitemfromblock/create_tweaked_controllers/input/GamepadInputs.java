package com.getitemfromblock.create_tweaked_controllers.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.config.ModClientConfig;

import net.minecraft.network.chat.Component;

public class GamepadInputs
{
    static final public boolean buttons[] = new boolean[15];
    static final public float axis[] = new float[6];

    protected static GLFWGamepadState state = null;
    protected static int selectedGamepad = -1;

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
                    if (state.buttons(b) == 0) continue;
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

    public static final Component GetButtonName(int index)
    {
        if (index < 4)
        {
            switch (ModClientConfig.CONTROLLER_LAYOUT_TYPE.get())
            {
                case NINTENDO:
                    return CreateTweakedControllers.translateDirect("gui_gamepad_button_" + (index ^ 0x1)); // Swap X/Y and A/B butons for Nintendo controllers

                case PLAYSTATION:
                    return CreateTweakedControllers.translateDirect("gui_gamepad_button_playstation_" + index); // Use PlayStation controller symbols
        
                default:
                    return CreateTweakedControllers.translateDirect("gui_gamepad_button_" + index);
            }
        }
        return CreateTweakedControllers.translateDirect("gui_gamepad_button_"+index);
    }

    public static final Component GetAxisName(int index)
    {
        return CreateTweakedControllers.translateDirect("gui_gamepad_axis_"+index);
    }
}
