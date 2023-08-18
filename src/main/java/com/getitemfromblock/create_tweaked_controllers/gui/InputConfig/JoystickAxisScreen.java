package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.input.JoystickAxisInput;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;

public class JoystickAxisScreen extends GenericInputScreen
{
    public JoystickAxisInput source;

    public JoystickAxisScreen(Screen parent)
    {
        super(parent);
    }

    public JoystickAxisScreen(Screen parent, JoystickAxisInput source)
    {
        super(parent);
        this.source = source;
    }

    @Override
    protected void renderWindow(PoseStack mx, int x, int y, float partialTicks)
    {
        // TODO
    }

    @Override
    protected void Populate()
    {
        // TODO
    }
    
}
