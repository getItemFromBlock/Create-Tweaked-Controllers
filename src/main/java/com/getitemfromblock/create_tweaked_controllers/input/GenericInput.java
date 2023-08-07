package com.getitemfromblock.create_tweaked_controllers.input;

public interface GenericInput
{
    // Returns true if input is active
    boolean GetButtonValue();

    // Returns a float between 0 and 1
    float GetAxisValue();

    default int GetButtonRedstoneValue()
    {
        return GetButtonValue() ? 15 : 0;
    }

    default int GetAxisRedstoneValue()
    {
        return Math.round(GetAxisValue() * 15);
    }

    String GetDisplayName();

    boolean IsInputValid();
}
