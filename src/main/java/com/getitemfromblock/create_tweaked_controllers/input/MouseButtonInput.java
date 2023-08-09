package com.getitemfromblock.create_tweaked_controllers.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.simibubi.create.AllKeys;

public class MouseButtonInput implements GenericInput
{
    private int buttonID = -1;
    private boolean invertValue = false;

    public MouseButtonInput(int button)
    {
        this.buttonID = button;
    }
    
    public MouseButtonInput()
    {
    }

    @Override
    public boolean GetButtonValue()
    {
        return invertValue ? !AllKeys.isMouseButtonDown(buttonID) : AllKeys.isMouseButtonDown(buttonID);
    }

    @Override
    public float GetAxisValue()
    {
        return GetButtonValue() ? 1.0f : 0.0f;
    }

    @Override
    public String GetDisplayName()
    {
        return "Mouse button " + buttonID;
    }

    @Override
    public boolean IsInputValid()
    {
        return buttonID >= 0;
    }

    @Override
    public void Serialize(DataOutputStream buf) throws IOException
    {
        buf.writeBoolean(invertValue);
        buf.writeInt(buttonID);
    }

    @Override
    public void Deserialize(DataInputStream buf) throws IOException
    {
        invertValue = buf.readBoolean();
        buttonID = buf.readInt();
    }

    @Override
    public InputType GetType()
    {
        return InputType.MOUSE_BUTTON;
    }

    @Override
    public int GetValue()
    {
        return buttonID;
    }
    
}
