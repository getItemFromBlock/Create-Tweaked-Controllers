package com.getitemfromblock.create_tweaked_controllers.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.GenericInputScreen;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.JoystickAxisScreen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class JoystickAxisInput implements GenericInput
{
    public int axisID = -1;
    public float minBound = 0.0f;
    public float maxBound = 1.0f;

    public JoystickAxisInput(int axisID)
    {
        this.axisID = axisID;
    }

    public JoystickAxisInput()
    {
    }

    public JoystickAxisInput(int axisID, float min, float max)
    {
        this.axisID = axisID;
        this.minBound = min;
        this.maxBound = max;
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
        float v = (JoystickInputs.GetAxis(axisID) - minBound) / (maxBound - minBound);
        if (v < 0) v = 0;
        if (v > 1) v = 1;
        return v;
    }

    @Override
    public MutableComponent GetDisplayName()
    {
        if (minBound >= 0 && maxBound >= 0)
        {
            return CreateTweakedControllers.translateDirect("gui_input_joystick_axis", "+"+axisID);
        }
        else if (minBound <= 0 && maxBound <= 0)
        {
            return CreateTweakedControllers.translateDirect("gui_input_joystick_axis", "-"+axisID);
        }
        else
        {
            return CreateTweakedControllers.translateDirect("gui_input_joystick_axis", ""+axisID);
        }
    }

    @Override
    public boolean IsInputValid()
    {
        return axisID < JoystickInputs.GetAxisCount() && axisID >= 0 && minBound != maxBound;
    }

    @Override
    public void Serialize(DataOutputStream buf) throws IOException
    {
        buf.writeFloat(minBound);
        buf.writeFloat(maxBound);
        buf.writeInt(axisID);
    }

    @Override
    public void Deserialize(DataInputStream buf) throws IOException
    {
        minBound = buf.readFloat();
        maxBound = buf.readFloat();
        axisID = buf.readInt();
    }

    @Override
    public InputType GetType()
    {
        return InputType.JOYSTICK_AXIS;
    }

    @Override
    public int GetValue()
    {
        return axisID;
    }

    @Override
    public GenericInputScreen OpenConfigScreen(Screen previous, Component comp)
    {
        return new JoystickAxisScreen(previous, comp, this);
    }

    public float GetRawInput()
    {
        if (!IsInputValid()) return 0;
        return JoystickInputs.GetAxis(axisID);
    }
    
}
