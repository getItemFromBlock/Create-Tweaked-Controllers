package com.getitemfromblock.create_tweaked_controllers.controller;

import com.getitemfromblock.create_tweaked_controllers.input.JoystickInputs;

public enum ControlType
{
    KEYBOARD_MOUSE,
    JOYSTICK,
    CUSTOM_0,
    CUSTOM_1;

    public boolean IsAdapted()
    {
        switch (this)
        {
            case KEYBOARD_MOUSE:
                return !JoystickInputs.HasJoystick();
            case JOYSTICK:
                return JoystickInputs.HasJoystick();
            default:
                return true;
        }
    }
}