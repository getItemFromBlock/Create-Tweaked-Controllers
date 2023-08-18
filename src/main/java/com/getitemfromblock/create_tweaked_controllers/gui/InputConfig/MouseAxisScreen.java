package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.input.MouseAxisInput;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;

public class MouseAxisScreen extends GenericInputScreen
{
    public MouseAxisInput source;

    public MouseAxisScreen(Screen parent)
    {
        super(parent);
    }

    public MouseAxisScreen(Screen parent, MouseAxisInput source)
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
