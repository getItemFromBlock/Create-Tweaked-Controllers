package com.getitemfromblock.create_extended_controllers.controller.extended;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.getitemfromblock.create_extended_controllers.ModItems;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class ExtendedLecternControllerRenderer extends SafeTileEntityRenderer<ExtendedLecternControllerBlockEntity>
{

	public ExtendedLecternControllerRenderer(BlockEntityRendererProvider.Context context)
	{
	}

	@Override
	protected void renderSafe(ExtendedLecternControllerBlockEntity be, float partialTicks, PoseStack ms,
  		MultiBufferSource buffer, int light, int overlay)
		{

		ItemStack stack = ModItems.EXTENDED_LINKED_CONTROLLER.asStack();
		TransformType transformType = TransformType.NONE;
		ExtendedLinkedControllerModel mainModel = (ExtendedLinkedControllerModel) Minecraft.getInstance()
			.getItemRenderer()
			.getModel(stack, be.getLevel(), null, 0);
		PartialItemModelRenderer renderer = PartialItemModelRenderer.of(stack, transformType, ms, buffer, overlay);
		boolean active = be.hasUser();
		boolean renderDepression = be.isUsedBy(Minecraft.getInstance().player);

		Direction facing = be.getBlockState().getValue(ExtendedLecternControllerBlock.FACING);
		TransformStack msr = TransformStack.cast(ms);

		ms.pushPose();
		msr.translate(0.5, 1.45, 0.5);
		msr.rotateY(AngleHelper.horizontalAngle(facing) - 90);
		msr.translate(0.28, 0, 0);
		msr.rotateZ(-22.0);
		ExtendedLinkedControllerItemRenderer.renderInLectern(stack, mainModel, renderer, transformType, ms, light, active, renderDepression);
		ms.popPose();
	}

}
