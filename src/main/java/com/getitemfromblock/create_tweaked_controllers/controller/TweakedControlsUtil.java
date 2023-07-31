package com.getitemfromblock.create_extended_controllers.controller.extended;

import com.getitemfromblock.create_extended_controllers.ControllerInputs;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

public class ExtendedControlsUtil
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
