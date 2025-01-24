package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ColoredButton extends Button
{
    public Vector3f color = new Vector3f(1, 1, 1);

    public ColoredButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, Component p_93725_, OnPress p_93726_, Vector3f color)
    {
        super(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_);
        this.color = color;
    }

    public ColoredButton(int p_93728_, int p_93729_, int p_93730_, int p_93731_, Component p_93732_, OnPress p_93733_,
            OnTooltip p_93734_, Vector3f color)
        {
        super(p_93728_, p_93729_, p_93730_, p_93731_, p_93732_, p_93733_, p_93734_);
        this.color = color;
    }

    @Override
    public void renderButton(PoseStack ms, int x, int y, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(color.x(), color.y(), color.z(), this.alpha);
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(ms, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.blit(ms, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(ms, minecraft, x, y);
        int j = getFGColor();
        drawCenteredString(ms, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
    }
    
}
