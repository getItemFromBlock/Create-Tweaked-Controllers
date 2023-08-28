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

public class ControllerButtonRenderer implements ScreenElement
{
    public static final ResourceLocation BUTTON_ATLAS = CreateTweakedControllers.asResource("textures/gui/controller_buttons.png");
    public static final int BUTTON_ATLAS_SIZE = 64;
    public static final int BUTTON_WIDTH = 8;
    public static final int BUTTON_HEIGHT = 8;

    private static int x = 0, y = -1;
    private int buttonX, buttonY;

    public static final ControllerButtonRenderer
        B_UP = newRow(), // BUP
        B_LEFT = next(),
        B_DOWN = next(),
        B_RIGHT = next(),
        B_A = newRow(),
        B_B = next(),
        B_X = next(),
        B_Y = next(),
        B_SQUARE = next(),
        B_TRIANGLE = next(),
        B_CIRCLE = next(),
        B_CROSS = next(),
        B_START = newRow(),
        B_SELECT = next(),
        B_MIDDLE = next(),
        B_L = next(),
        B_R = newRow(),
        B_LJ = next(), // BLJ
        B_RJ = next();
    ;

    public ControllerButtonRenderer(int x, int y)
    {
        buttonX = x * BUTTON_WIDTH;
        buttonY = y * BUTTON_HEIGHT;
    }

    private static ControllerButtonRenderer next()
    {
        return new ControllerButtonRenderer(++x, y);
    }

    private static ControllerButtonRenderer newRow()
    {
        return new ControllerButtonRenderer(x = 0, ++y);
    }

    @OnlyIn(Dist.CLIENT)
    public void bind()
    {
        RenderSystem.setShaderTexture(0, BUTTON_ATLAS);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack matrixStack, int x, int y)
    {
        bind();
        GuiComponent.blit(matrixStack, x, y, 0, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ATLAS_SIZE, BUTTON_ATLAS_SIZE);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack matrixStack, int x, int y, GuiComponent component)
    {
        bind();
        component.blit(matrixStack, x, y, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, MultiBufferSource buffer, int color)
    {
        VertexConsumer builder = buffer.getBuffer(RenderType.textSeeThrough(BUTTON_ATLAS));
        Matrix4f matrix = ms.last().pose();
        Color rgb = new Color(color);
        int light = LightTexture.FULL_BRIGHT;

        Vec3 vec1 = new Vec3(0, 0, 0);
        Vec3 vec2 = new Vec3(0, 1, 0);
        Vec3 vec3 = new Vec3(1, 1, 0);
        Vec3 vec4 = new Vec3(1, 0, 0);

        float u1 = buttonX * 1f / BUTTON_ATLAS_SIZE;
        float u2 = (buttonX + BUTTON_WIDTH) * 1f / BUTTON_ATLAS_SIZE;
        float v1 = buttonY * 1f / BUTTON_ATLAS_SIZE;
        float v2 = (buttonY + BUTTON_HEIGHT) * 1f / BUTTON_ATLAS_SIZE;

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
        return new DelegatedStencilElement().withStencilRenderer((ms, w, h, alpha) -> this.render(ms, 0, 0)).withBounds(BUTTON_WIDTH, BUTTON_HEIGHT);
    }
}
