package com.getitemfromblock.create_tweaked_controllers.gui;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DigitIconRenderer implements ScreenElement
{
    public static final ResourceLocation DIGIT_ATLAS = CreateTweakedControllers.asResource("textures/gui/digits.png");
    public static final int DIGIT_ATLAS_SIZE = 32;
    public static final int DIGIT_WIDTH = 6;
    public static final int DIGIT_HEIGHT = 10;

    private static int x = 0, y = -1;
    private int digitX, digitY;

    public static final DigitIconRenderer
        D_NUMBERS[] = 
        {
            newRow(),
            next(),
            next(),
            next(),
            next(),
            newRow(),
            next(),
            next(),
            next(),
            next()
        },
        D_EMPTY = newRow(),
        D_DASH = next(),
        D_CROSS = next();
    ;

    public DigitIconRenderer(int x, int y)
    {
        digitX = x * DIGIT_WIDTH;
        digitY = y * DIGIT_HEIGHT;
    }

    private static DigitIconRenderer next()
    {
        return new DigitIconRenderer(++x, y);
    }

    private static DigitIconRenderer newRow()
    {
        return new DigitIconRenderer(x = 0, ++y);
    }

    @OnlyIn(Dist.CLIENT)
    public void bind()
    {
        RenderSystem.setShaderTexture(0, DIGIT_ATLAS);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack matrixStack, int x, int y)
    {
        bind();
        GuiComponent.blit(matrixStack, x, y, 0, digitX, digitY, DIGIT_WIDTH, DIGIT_HEIGHT, DIGIT_ATLAS_SIZE, DIGIT_ATLAS_SIZE);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack matrixStack, int x, int y, GuiComponent component)
    {
        bind();
        component.blit(matrixStack, x, y, digitX, digitY, DIGIT_WIDTH, DIGIT_HEIGHT);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, MultiBufferSource buffer, int color)
    {
        VertexConsumer builder = buffer.getBuffer(RenderType.textSeeThrough(DIGIT_ATLAS));
        Matrix4f matrix = ms.last().pose();
        Color rgb = new Color(color);
        int light = LightTexture.FULL_BRIGHT;

        Vec3 vec1 = new Vec3(0, 0, 0);
        Vec3 vec2 = new Vec3(0, 1, 0);
        Vec3 vec3 = new Vec3(1, 1, 0);
        Vec3 vec4 = new Vec3(1, 0, 0);

        float u1 = digitX * 1f / DIGIT_ATLAS_SIZE;
        float u2 = (digitX + DIGIT_WIDTH) * 1f / DIGIT_ATLAS_SIZE;
        float v1 = digitY * 1f / DIGIT_ATLAS_SIZE;
        float v2 = (digitY + DIGIT_HEIGHT) * 1f / DIGIT_ATLAS_SIZE;

        vertex(builder, matrix, vec1, rgb, u1, v1, light);
        vertex(builder, matrix, vec2, rgb, u1, v2, light);
        vertex(builder, matrix, vec3, rgb, u2, v2, light);
        vertex(builder, matrix, vec4, rgb, u2, v1, light);
    }

    @OnlyIn(Dist.CLIENT)
    private void vertex(VertexConsumer builder, Matrix4f matrix, Vec3 vec, Color rgb, float u, float v, int light)
    {
        builder.vertex(matrix, (float) vec.x, (float) vec.y, (float) vec.z)
            .color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), 255)
            .uv(u, v)
            .uv2(light)
            .endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public DelegatedStencilElement asStencil()
    {
        return new DelegatedStencilElement().withStencilRenderer((ms, w, h, alpha) -> this.render(ms, 0, 0)).withBounds(DIGIT_WIDTH, DIGIT_HEIGHT);
    }
}
