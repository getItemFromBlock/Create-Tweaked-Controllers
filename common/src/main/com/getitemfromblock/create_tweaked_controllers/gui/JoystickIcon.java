package com.getitemfromblock.create_tweaked_controllers.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;

import net.minecraft.network.chat.Component;

public class JoystickIcon extends AbstractSimiWidget
{

    protected ScreenElement icon;
    protected int deltaX = 0;
    protected int deltaY = 0;

    public JoystickIcon(int x, int y, ScreenElement icon)
    {
        this(x, y, 18, 18, icon);
    }
    
    public JoystickIcon(int x, int y, int w, int h, ScreenElement icon)
    {
        super(x, y, w, h);
        this.active = false;
        this.icon = icon;
    }

    public void move(int dx, int dy)
    {
        deltaX = dx;
        deltaY = dy;
    }

    @Override
    public void renderButton(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (visible)
        {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            icon.render(matrixStack, x + 1 + deltaX, y + 1 + deltaY);
        }
    }

    public void setToolTip(Component text)
    {
        toolTip.clear();
        toolTip.add(text);
    }

    public void setIcon(ScreenElement icon)
    {
        this.icon = icon;
    }

    @Override
    public boolean mouseClicked(double p_93641_, double p_93642_, int p_93643_)
    {
      return false;
    }
}
