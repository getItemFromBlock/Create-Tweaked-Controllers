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

public class ModIcons implements ScreenElement
{

    public static final ResourceLocation ICON_ATLAS = CreateTweakedControllers.asResource("textures/gui/icons.png");
    public static final int ICON_ATLAS_SIZE = 32;

    private static int x = 0, y = -1;
    private int iconX;
    private int iconY;

    public static final ModIcons
        I_BUTTON = newRow(),
        I_AXES = next(),
        I_LEFT_JOYSTICK = newRow(),
        I_RIGHT_JOYSTICK = next();
    ;

    public ModIcons(int x, int y)
    {
        iconX = x * 16;
        iconY = y * 16;
    }

    private static ModIcons next()
    {
        return new ModIcons(++x, y);
    }

    private static ModIcons newRow()
    {
        return new ModIcons(x = 0, ++y);
    }

    @OnlyIn(Dist.CLIENT)
    public void bind()
    {
        RenderSystem.setShaderTexture(0, ICON_ATLAS);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack matrixStack, int x, int y)
    {
        bind();
        GuiComponent.blit(matrixStack, x, y, 0, iconX, iconY, 16, 16, ICON_ATLAS_SIZE, ICON_ATLAS_SIZE);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack matrixStack, int x, int y, GuiComponent component)
    {
        bind();
        component.blit(matrixStack, x, y, iconX, iconY, 16, 16);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, MultiBufferSource buffer, int color)
    {
        VertexConsumer builder = buffer.getBuffer(RenderType.textSeeThrough(ICON_ATLAS));
        Matrix4f matrix = ms.last().pose();
        Color rgb = new Color(color);
        int light = LightTexture.FULL_BRIGHT;

        Vec3 vec1 = new Vec3(0, 0, 0);
        Vec3 vec2 = new Vec3(0, 1, 0);
        Vec3 vec3 = new Vec3(1, 1, 0);
        Vec3 vec4 = new Vec3(1, 0, 0);

        float u1 = iconX * 1f / ICON_ATLAS_SIZE;
        float u2 = (iconX + 16) * 1f / ICON_ATLAS_SIZE;
        float v1 = iconY * 1f / ICON_ATLAS_SIZE;
        float v2 = (iconY + 16) * 1f / ICON_ATLAS_SIZE;

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
        return new DelegatedStencilElement().withStencilRenderer((ms, w, h, alpha) -> this.render(ms, 0, 0)).withBounds(16, 16);
    }

}
