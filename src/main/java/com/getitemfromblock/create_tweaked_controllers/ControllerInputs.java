package com.getitemfromblock.create_tweaked_controllers;

import org.lwjgl.glfw.GLFWGamepadState;

public class ControllerInputs
{
    final public boolean buttons[] = new boolean[15];
    final public float axis[] = new float[6];

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

    public void Empty()
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

    public void Fill(GLFWGamepadState state)
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
