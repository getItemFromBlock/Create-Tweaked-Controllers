package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.input.KeyboardInput;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;

public class KeyboardInputScreen extends GenericInputScreen
{
    public KeyboardInput source;

    public KeyboardInputScreen(Screen parent)
    {
        super(parent);
    }

    public KeyboardInputScreen(Screen parent, KeyboardInput source)
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
