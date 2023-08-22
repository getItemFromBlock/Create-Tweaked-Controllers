package com.getitemfromblock.create_tweaked_controllers.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;

import net.minecraft.network.chat.Component;

public class DigitIcon extends AbstractSimiWidget
{
    protected ScreenElement icon;
    public Vector3f color;

    public DigitIcon(int x, int y, DigitIconRenderer digit, Vector3f color)
    {
        this(x, y, DigitIconRenderer.DIGIT_WIDTH, DigitIconRenderer.DIGIT_HEIGHT, digit, color);
    }
    
    public DigitIcon(int x, int y, int w, int h, ScreenElement icon, Vector3f color)
    {
        super(x, y, w, h);
        this.active = false;
        this.icon = icon;
        this.color = color;
    }

    @Override
    public void renderButton(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (visible)
        {
            RenderSystem.setShaderColor(color.x(), color.y(), color.z(), 1.0F);
            icon.render(matrixStack, x, y);
        }
    }

    public void setToolTip(Component text)
    {
        toolTip.clear();
        toolTip.add(text);
    }

    public void setColor(Vector3f color)
    {
        this.color = color;
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
