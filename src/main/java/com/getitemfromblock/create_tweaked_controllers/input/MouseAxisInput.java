package com.getitemfromblock.create_tweaked_controllers.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MouseAxisInput implements GenericInput
{
    private boolean useVelocity = false;
    private boolean isYAxis = false;
    private float minBound = 0.0f;
    private float maxBound = 1000.0f;

    public MouseAxisInput(boolean axis, float min, float max)
    {
        this.isYAxis = axis;
        this.minBound = min;
        this.maxBound = max;
    }
    
    public MouseAxisInput()
    {
    }

    @Override
    public boolean GetButtonValue()
    {
        return GetAxisValue() >= 0.5f;
    }

    @Override
    public float GetAxisValue()
    {
        float v = isYAxis ? MouseCursorHandler.GetY(useVelocity) : MouseCursorHandler.GetX(useVelocity);
        v = (v - minBound) / (maxBound - minBound);
        if (v < 0) v = 0;
        if (v > 1) v = 1;
        return v;
    }

    @Override
    public String GetDisplayName()
    {
        if (useVelocity)
        {
            return isYAxis ? "Mouse Y velocity" : "Mouse X velocity";
        }
        else
        {
            return isYAxis ? "Mouse Y position" : "Mouse X position";
        }
    }

    @Override
    public boolean IsInputValid()
    {
        return minBound != maxBound;
    }

    @Override
    public void Serialize(DataOutputStream buf) throws IOException
    {
        byte val = (byte)((useVelocity ? 0x1 : 0) | (isYAxis ? 0x2 : 0));
        buf.writeByte(val);
        buf.writeFloat(minBound);
        buf.writeFloat(maxBound);
    }

    @Override
    public void Deserialize(DataInputStream buf) throws IOException
    {
        Byte val = buf.readByte();
        useVelocity = (val & 0x1) != 0;
        isYAxis = (val & 0x2) != 0;
        minBound = buf.readFloat();
        maxBound = buf.readFloat();
    }

    @Override
    public InputType GetType()
    {
        return InputType.MOUSE_AXIS;
    }

    @Override
    public int GetValue()
    {
        return isYAxis ? 1 : 0;
    }
    
}
