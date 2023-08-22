package com.getitemfromblock.create_tweaked_controllers.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;

import net.minecraft.network.chat.Component;

public class PlainRect extends AbstractSimiWidget
{
    public Vector3f color;
    public float extendValue = 0.0f;
    protected int endX;
    protected int endY;
    protected int endW;
    protected int endH;
    
    public PlainRect(int x, int y, int w, int h, int ex, int ey, int ew, int eh, Vector3f color)
    {
        super(x, y, w, h);
        this.active = false;
        this.color = color;
        this.endX = ex;
        this.endY = ey;
        this.endW = ew;
        this.endH = eh;
    }

    public void SetValue(float value)
    {
        extendValue = value;
    }

    private static int Lerp(int a, int b, float v)
    {
        return a + Math.round((b - a) * v);
    }

    @Override
    public void renderButton(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (visible)
        {
            int ix = Lerp(x, endX, extendValue);
            int iy = Lerp(y, endY, extendValue);
            int iw = Lerp(width, endW, extendValue);
            int ih = Lerp(height, endH, extendValue);
            if (iw == 0 || ih == 0) return;
            RenderSystem.setShaderColor(color.x(), color.y(), color.z(), 1.0F);
            boolean reverse = (iw < 0) ^ (ih < 0);
            if (reverse)
            {
                if (iw < 0)
                {
                    PlainRectRenderer.render(matrixStack, ix + iw, iy, -iw, ih);
                }
                else
                {
                    PlainRectRenderer.render(matrixStack, ix, iy + ih, iw, -ih);
                }
            }
            else
            {
                PlainRectRenderer.render(matrixStack, ix, iy, iw, ih);
            }
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

    @Override
    public boolean mouseClicked(double p_93641_, double p_93642_, int p_93643_)
    {
        return false;
    }
}
