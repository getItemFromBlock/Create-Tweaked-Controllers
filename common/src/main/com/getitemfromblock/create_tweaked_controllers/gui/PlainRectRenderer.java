package com.getitemfromblock.create_tweaked_controllers.gui;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlainRectRenderer implements ScreenElement
{
    public static final ResourceLocation BUTTON_ATLAS = CreateTweakedControllers.asResource("textures/gui/controller_buttons.png");
    public static final int BUTTON_ATLAS_SIZE = 64;

    public PlainRectRenderer()
    {
    }

    @OnlyIn(Dist.CLIENT)
    public static void bind()
    {
        RenderSystem.setShaderTexture(0, BUTTON_ATLAS);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack matrixStack, int x, int y)
    {
        bind();
        innerBlit(matrixStack, x, x + 1, y, y + 1, 0, 1, 1, 27, 27, BUTTON_ATLAS_SIZE, BUTTON_ATLAS_SIZE);
    }

    @OnlyIn(Dist.CLIENT)
    public static void render(PoseStack matrixStack, int x, int y, int width, int height)
    {
        bind();
        innerBlit(matrixStack, x, x + width, y, y + height, 0, 1, 1, 27, 27, BUTTON_ATLAS_SIZE, BUTTON_ATLAS_SIZE);
    }

    private static void innerBlit(PoseStack ms, int x, int dx, int y, int dy, int z, int tw, int th, float tx, float ty, int tex_w, int tex_h)
    {
        innerBlit(ms.last().pose(), x, dx, y, dy, z, (tx + 0.0F) / (float)tex_w, (tx + (float)tw) / (float)tex_w, (ty + 0.0F) / (float)tex_h, (ty + (float)th) / (float)tex_h);
    }

    // Took from net.minecraft.client.gui
    private static void innerBlit(Matrix4f p_93113_, int p_93114_, int p_93115_, int p_93116_, int p_93117_, int p_93118_, float p_93119_, float p_93120_, float p_93121_, float p_93122_)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(p_93113_, (float)p_93114_, (float)p_93117_, (float)p_93118_).uv(p_93119_, p_93122_).endVertex();
        bufferbuilder.vertex(p_93113_, (float)p_93115_, (float)p_93117_, (float)p_93118_).uv(p_93120_, p_93122_).endVertex();
        bufferbuilder.vertex(p_93113_, (float)p_93115_, (float)p_93116_, (float)p_93118_).uv(p_93120_, p_93121_).endVertex();
        bufferbuilder.vertex(p_93113_, (float)p_93114_, (float)p_93116_, (float)p_93118_).uv(p_93119_, p_93121_).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
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

        float u1 = 27.5f / BUTTON_ATLAS_SIZE;
        float u2 = 28.5f / BUTTON_ATLAS_SIZE;
        float v1 = 27.5f / BUTTON_ATLAS_SIZE;
        float v2 = 28.5f / BUTTON_ATLAS_SIZE;

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
        return new DelegatedStencilElement().withStencilRenderer((ms, w, h, alpha) -> this.render(ms, 0, 0)).withBounds(1, 1);
    }
}
