package com.getitemfromblock.create_tweaked_controllers.input;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.GenericInputScreen;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.KeyboardInputScreen;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.AllKeys;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class KeyboardInput implements GenericInput
{
    public int key = GLFW.GLFW_KEY_UNKNOWN;
    public boolean invertValue = false;
    public boolean springbackEnabled = true;
    public AxisSpringback springback = new AxisSpringback();

    public KeyboardInput(int key)
    {
        this.key = key;
    }

    public KeyboardInput()
    {
    }

    @Override
    public boolean GetButtonValue()
    {
        if (!IsInputValid()) return invertValue;
        return invertValue ? !AllKeys.isKeyDown(key): AllKeys.isKeyDown(key);
    }

    @Override
    public float GetAxisValue()
    {
        if (springbackEnabled) {
            float target = GetButtonValue() ? 1.0f : 0.0f;
            return springback.smooth(target);
        } else {
            return GetButtonValue() ? 1.0f : 0.0f;
        }
    }

    @Override
    public MutableComponent GetDisplayName()
    {
        return key >= 0 ? InputConstants.getKey(key, 0).getDisplayName().plainCopy() : CreateTweakedControllers.translateDirectRaw("key.keyboard.unknown");
    }

    @Override
    public boolean IsInputValid()
    {
        return key != GLFW.GLFW_KEY_UNKNOWN;
    }

    @Override
    public boolean IsDataCoherent()
    {
        return key > GLFW.GLFW_KEY_UNKNOWN && key <= GLFW.GLFW_KEY_LAST;
    }

    @Override
    public void Serialize(DataOutputStream buf) throws IOException
    {
        buf.writeBoolean(invertValue);
        buf.writeInt(key);
        buf.writeBoolean(springbackEnabled);
        buf.writeBoolean(springback.isExponential);
        buf.writeBoolean(springback.useGlobalRate);
        buf.writeFloat(springback.globalRate);
        buf.writeFloat(springback.riseRate);
        buf.writeFloat(springback.fallRate);
        buf.writeFloat(springback.holdTime);
    }

    @Override
    public void Deserialize(DataInputStream buf) throws IOException
    {
        invertValue = buf.readBoolean();
        key = buf.readInt();
        springbackEnabled = buf.readBoolean();
        springback.isExponential = buf.readBoolean();
        springback.useGlobalRate = buf.readBoolean();
        springback.globalRate = buf.readFloat();
        springback.riseRate = buf.readFloat();
        springback.fallRate = buf.readFloat();
        springback.holdTime = buf.readFloat();
    }

    @Override
    public InputType GetType()
    {
        return InputType.KEYBOARD_KEY;
    }

    @Override
    public int GetValue()
    {
        return key;
    }

    @Override
    public GenericInputScreen OpenConfigScreen(Screen previous, Component comp)
    {
        return new KeyboardInputScreen(previous, comp, this);
    }

}
