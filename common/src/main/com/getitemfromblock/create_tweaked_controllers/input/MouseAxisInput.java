package com.getitemfromblock.create_tweaked_controllers.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.GenericInputScreen;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.MouseAxisScreen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class MouseAxisInput implements GenericInput
{
    public boolean useVelocity = false;
    public boolean isYAxis = false;
    public float minBound = 0.0f;
    public float maxBound = 1000.0f;

    public MouseAxisInput(boolean axis, float min, float max, boolean useVel)
    {
        this.isYAxis = axis;
        this.minBound = min;
        this.maxBound = max;
        this.useVelocity = useVel;
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
        if (!IsInputValid()) return 0;
        float v = isYAxis ? MouseCursorHandler.GetY(useVelocity) : MouseCursorHandler.GetX(useVelocity);
        v = (v - minBound) / (maxBound - minBound);
        if (v < 0) v = 0;
        if (v > 1) v = 1;
        return v;
    }

    @Override
    public MutableComponent GetDisplayName()
    {
        String a = "";
        if (minBound >= 0 && maxBound >= 0)
        {
            a = "+";
        }
        else if (minBound <= 0 && maxBound <= 0)
        {
            a = "-";
        }
        a = a + (isYAxis ? 'Y' : 'X');
        if (useVelocity)
        {
            return CreateTweakedControllers.translateDirect("gui_input_cursor_vel", a);
        }
        else
        {
            return CreateTweakedControllers.translateDirect("gui_input_cursor_pos", a);
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

    @Override
    public GenericInputScreen OpenConfigScreen(Screen previous, Component comp)
    {
        return new MouseAxisScreen(previous, comp, this);
    }

    public float GetRawInput()
    {
        if (!IsInputValid()) return 0;
        return isYAxis ? MouseCursorHandler.GetY(useVelocity) : MouseCursorHandler.GetX(useVelocity);
    }
    
}
