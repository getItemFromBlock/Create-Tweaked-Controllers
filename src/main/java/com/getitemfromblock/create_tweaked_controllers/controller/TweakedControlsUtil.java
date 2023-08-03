package com.getitemfromblock.create_tweaked_controllers.controller;

import com.getitemfromblock.create_tweaked_controllers.ControllerInputs;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

public class TweakedControlsUtil
{
    protected static GLFWGamepadState state = null;
    protected static int selectedGamepad = -1;

	public static void GetControls(ControllerInputs result)
    {
        CheckState();
        if (selectedGamepad < 0)
        {
            for (int i = 0; i < 16 && selectedGamepad < 0; i++)
            {
                if (!GLFW.glfwJoystickIsGamepad(i)) continue;
                GLFW.glfwGetGamepadState(i, state);
                for (int b = 0; b < 15; b++)
                {
                    if (state.buttons(i) == 0) continue;
                    selectedGamepad = i;
                    break;
                }
            }
        }
        if (selectedGamepad < 0 || !GLFW.glfwJoystickIsGamepad(selectedGamepad))
        {
            result.Empty();
            selectedGamepad = -1;
        }
        else
        {
            GLFW.glfwGetGamepadState(selectedGamepad, state);
            result.Fill(state);
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

	public static boolean IsPressed(ControllerInputs kb, int button)
    {
		return kb.buttons[button];
	}

    public static float GetAxis(ControllerInputs kb, int axis)
    {
		return kb.axis[axis];
	}


    private static void CheckState()
    {
        if (state == null)
        {
            state = GLFWGamepadState.create();
        }
    }
}
