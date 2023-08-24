package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.input.KeyboardInput;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class KeyboardInputScreen extends GenericInputScreen
{
    public KeyboardInput source;
    private Checkbox box;

    public KeyboardInputScreen(Screen parent, Component name, KeyboardInput s)
    {
        super(parent, name, s);
        source = s;
    }

    @Override
    protected void renderWindow(PoseStack ms, int x, int y, float partialTicks)
    {
        super.renderWindow(ms, x, y, partialTicks);
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
