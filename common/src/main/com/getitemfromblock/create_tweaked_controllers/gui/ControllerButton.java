package com.getitemfromblock.create_tweaked_controllers.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;

import net.minecraft.network.chat.Component;

public class ControllerButton extends AbstractSimiWidget
{
    protected ScreenElement icon;
    public Vector3f color;
    public float colorFactor = 1.0f;
    protected int deltaX = 0;
    protected int deltaY = 0;

    public ControllerButton(int x, int y, ControllerButtonRenderer button, Vector3f color)
    {
        this(x, y, ControllerButtonRenderer.BUTTON_WIDTH, ControllerButtonRenderer.BUTTON_HEIGHT, button, color);
    }
    
    public ControllerButton(int x, int y, int w, int h, ScreenElement icon, Vector3f color)
    {
        super(x, y, w, h);
        this.active = false;
        this.icon = icon;
        this.color = color;
    }

    public void move(int dx, int dy)
    {
        deltaX = dx;
        deltaY = dy;
    }

    public void SetColorFactor(float value)
    {
        colorFactor = value;
    }

    @Override
    public void renderButton(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (visible)
        {
            RenderSystem.setShaderColor(color.x() * colorFactor, color.y() * colorFactor, color.z() * colorFactor, 1.0F);
            icon.render(matrixStack, x + deltaX, y + deltaY);
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
