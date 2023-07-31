package com.getitemfromblock.create_tweaked_controllers.controller;

import com.getitemfromblock.create_tweaked_controllers.ControllerInputs;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

public class TweakedControlsUtil
{
    protected static GLFWGamepadState state = null;

	public static void GetControls(ControllerInputs result)
    {
        CheckState();
        if (!GLFW.glfwJoystickIsGamepad(0))
        {
            result.Empty();
        }
        else
        {
            GLFW.glfwGetGamepadState(0, state);
            result.Fill(state);
        }
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
