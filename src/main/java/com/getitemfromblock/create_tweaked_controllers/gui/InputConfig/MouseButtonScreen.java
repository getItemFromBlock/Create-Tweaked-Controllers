package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.input.MouseButtonInput;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MouseButtonScreen extends GenericInputScreen
{
    public MouseButtonInput source;
    private Checkbox box;

    public MouseButtonScreen(Screen parent, Component name, MouseButtonInput s)
    {
        super(parent, name, s);
        source = s;
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int x, int y, float partialTicks)
    {
        super.renderWindow(graphics, x, y, partialTicks);
        source.invertValue = box.selected();
    }

    @Override
    protected void Populate()
    {
        box = new Checkbox(width / 2 - 60, height/2 - 10, 100, 20,
            CreateTweakedControllers.translateDirect("gui_config_invert"), source.invertValue);
        addRenderableWidget(box);
    }
    
}
