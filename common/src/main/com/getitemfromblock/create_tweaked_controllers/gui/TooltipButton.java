package com.getitemfromblock.create_tweaked_controllers.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TooltipButton extends Button
{
    protected Component tooltip;

    public TooltipButton(Screen parent, int p_93728_, int p_93729_, int p_93730_, int p_93731_, Component p_93732_, OnPress p_93733_)
    {
        super(p_93728_, p_93729_, p_93730_, p_93731_, p_93732_, p_93733_, (b, ps, mx, my) -> {
            TooltipButton tb = (TooltipButton)b;
            if (tb == null || tb.tooltip == null) return;
            parent.renderTooltip(ps, tb.tooltip, mx, my);
        });
    }

    public void SetToolTipText(Component text)
    {
        tooltip = text;
    }
    
}
