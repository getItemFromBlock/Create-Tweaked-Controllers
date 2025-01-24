package com.getitemfromblock.create_tweaked_controllers.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.GenericInputScreen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface GenericInput
{
    // Returns true if input is active
    boolean GetButtonValue();

    // Returns a float between 0 and 1
    float GetAxisValue();

    MutableComponent GetDisplayName();

    boolean IsInputValid();

    void Serialize(DataOutputStream buf) throws IOException;

    void Deserialize(DataInputStream buf) throws IOException;

    GenericInputScreen OpenConfigScreen(Screen previous, Component name);

    InputType GetType();

    int GetValue();

    enum InputType
    {
        NONE,
        JOYSTICK_BUTTON,
        JOYSTICK_AXIS,
        MOUSE_BUTTON,
        MOUSE_AXIS,
        KEYBOARD_KEY;

        public static InputType GetType(byte v)
        {
            return values()[v];
        }

        public static byte GetValue(InputType v)
        {
            return (byte)v.ordinal();
        }
    }
}
