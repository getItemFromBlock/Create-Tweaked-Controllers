package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.input.MouseButtonInput;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;

public class MouseButtonScreen extends GenericInputScreen
{
    public MouseButtonInput source;

    public MouseButtonScreen(Screen parent)
    {
        super(parent);
    }

    public MouseButtonScreen(Screen parent, MouseButtonInput source)
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
