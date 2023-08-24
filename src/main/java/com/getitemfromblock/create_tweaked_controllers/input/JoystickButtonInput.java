package com.getitemfromblock.create_tweaked_controllers.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.GenericInputScreen;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.JoystickButtonScreen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class JoystickButtonInput implements GenericInput
{
    public int buttonID = -1;
    public boolean invertValue = false;

    public JoystickButtonInput(int buttonID)
    {
        this.buttonID = buttonID;
    }

    public JoystickButtonInput()
    {
    }

    @Override
    public boolean GetButtonValue()
    {
        if (!IsInputValid()) return invertValue;
        return invertValue ? !JoystickInputs.GetButton(buttonID) : JoystickInputs.GetButton(buttonID);
    }

    @Override
    public float GetAxisValue()
    {
        return GetButtonValue() ? 1.0f : 0.0f;
    }

    @Override
    public MutableComponent GetDisplayName()
    {
        return CreateTweakedControllers.translateDirect("gui_input_joystick_button", ""+buttonID);
    }

    @Override
    public boolean IsInputValid()
    {
        return buttonID < JoystickInputs.GetButtonCount() && buttonID >= 0;
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
        return InputType.JOYSTICK_BUTTON;
    }

    @Override
    public int GetValue()
    {
        return buttonID;
    }

    @Override
    public GenericInputScreen OpenConfigScreen(Screen previous, Component comp)
    {
        return new JoystickButtonScreen(previous, comp, this);
    }
    
}
