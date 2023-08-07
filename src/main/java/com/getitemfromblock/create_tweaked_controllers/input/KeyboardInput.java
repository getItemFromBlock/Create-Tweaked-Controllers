package com.getitemfromblock.create_tweaked_controllers.input;

import org.lwjgl.glfw.GLFW;

import com.simibubi.create.AllKeys;

public class KeyboardInput implements GenericInput
{
    private int key = GLFW.GLFW_KEY_UNKNOWN;
    private boolean invertValue = false;

    public KeyboardInput(int key)
    {
        this.key = key;
    }

    @Override
    public boolean GetButtonValue()
    {
        // TODO
        // Minecraft.getInstance().options.keyMappings
        return invertValue ? !AllKeys.isKeyDown(key): AllKeys.isKeyDown(key);
    }

    @Override
    public float GetAxisValue()
    {
        return GetButtonValue() ? 1.0f : 0.0f;
    }

    @Override
    public String GetDisplayName()
    {
        return "Keyboard key " + GLFW.glfwGetKeyName(key, 0);
    }

    @Override
    public boolean IsInputValid()
    {
        return key != GLFW.GLFW_KEY_UNKNOWN;
    }
}
