package com.getitemfromblock.create_tweaked_controllers.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.GenericInputScreen;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.MouseWheelScreen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class MouseWheelInput implements GenericInput
{
    public float minBound = 0.0f;
    public float maxBound = 1.0f;

    public MouseWheelInput(float min, float max)
    {
        this.minBound = min;
        this.maxBound = max;
    }

    public MouseWheelInput()
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
        float v = (float)MouseCursorHandler.GetScrollDelta();
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
        return CreateTweakedControllers.translateDirect("gui_input_mouse_wheel", a);

    }

    @Override
    public boolean IsInputValid()
    {
        return minBound != maxBound;
    }

    @Override
    public void Serialize(DataOutputStream buf) throws IOException
    {
        buf.writeFloat(minBound);
        buf.writeFloat(maxBound);
    }

    @Override
    public void Deserialize(DataInputStream buf) throws IOException
    {
        minBound = buf.readFloat();
        maxBound = buf.readFloat();
    }

    @Override
    public InputType GetType()
    {
        return InputType.MOUSE_WHEEL;
    }

    @Override
    public int GetValue()
    {
        return 2;
    }

    @Override
    public GenericInputScreen OpenConfigScreen(Screen previous, Component comp)
    {
        return new MouseWheelScreen(previous, comp, this);
    }

    public float GetRawInput()
    {
        if (!IsInputValid()) return 0;
        return (float)MouseCursorHandler.GetScrollDelta();
    }

}
