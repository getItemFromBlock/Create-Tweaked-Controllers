package com.getitemfromblock.create_tweaked_controllers.input;

public class JoystickAxisInput implements GenericInput
{
    private int axisID = -1;
    private float minBound = 0.0f;
    private float maxBound = 1.0f;

    public JoystickAxisInput(int axisID)
    {
        this.axisID = axisID;
    }

    @Override
    public boolean GetButtonValue()
    {
        return GetAxisValue() >= 0.5f;
    }

    @Override
    public float GetAxisValue()
    {
        return (JoystickInputs.GetAxis(axisID) - minBound) / (maxBound - minBound);
    }

    @Override
    public String GetDisplayName()
    {
        return "Joystick axis " + axisID;
    }

    @Override
    public boolean IsInputValid()
    {
        return axisID < JoystickInputs.GetAxisCount() && axisID >= 0;
    }
    
}
