package com.getitemfromblock.create_tweaked_controllers.input;

public class JoystickButtonInput implements GenericInput
{
    private int buttonID = -1;
    private boolean invertValue = false;

    public JoystickButtonInput(int buttonID)
    {
        this.buttonID = buttonID;
    }

    @Override
    public boolean GetButtonValue()
    {
        return invertValue ? !JoystickInputs.GetButton(buttonID) : JoystickInputs.GetButton(buttonID);
    }

    @Override
    public float GetAxisValue()
    {
        return GetButtonValue() ? 1.0f : 0.0f;
    }

    @Override
    public String GetDisplayName()
    {
        return "Joystick button " + buttonID;
    }

    @Override
    public boolean IsInputValid()
    {
        return buttonID < JoystickInputs.GetButtonCount() && buttonID >= 0;
    }
    
}
