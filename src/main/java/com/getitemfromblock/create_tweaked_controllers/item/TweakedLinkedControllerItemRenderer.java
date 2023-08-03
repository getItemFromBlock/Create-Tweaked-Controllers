package com.getitemfromblock.create_tweaked_controllers.item;

import java.util.Vector;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerModel;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler.Mode;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.foundation.utility.animation.LerpedFloat.Chaser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TweakedLinkedControllerItemRenderer extends CustomRenderedItemModelRenderer<TweakedLinkedControllerModel>
{

	protected static final PartialModel BASE = new PartialModel(CreateTweakedControllers.asResource("item/tweaked_linked_controller/powered"));
	protected static final PartialModel BUTTON = new PartialModel(CreateTweakedControllers.asResource("item/tweaked_linked_controller/button"));
	protected static final PartialModel JOYSTICK = new PartialModel(CreateTweakedControllers.asResource("item/tweaked_linked_controller/joystick"));
	protected static final PartialModel TRIGGER = new PartialModel(CreateTweakedControllers.asResource("item/tweaked_linked_controller/trigger"));
	protected static final PartialModel BUTTON_X = new PartialModel(CreateTweakedControllers.asResource("item/tweaked_linked_controller/button_blue"));
	protected static final PartialModel BUTTON_Y = new PartialModel(CreateTweakedControllers.asResource("item/tweaked_linked_controller/button_yellow"));
	protected static final PartialModel BUTTON_A = new PartialModel(CreateTweakedControllers.asResource("item/tweaked_linked_controller/button_green"));
	protected static final PartialModel BUTTON_B = new PartialModel(CreateTweakedControllers.asResource("item/tweaked_linked_controller/button_red"));

	static LerpedFloat equipProgress;
	static Vector<LerpedFloat> buttons;
	static Vector<LerpedFloat> axes;

	static
	{
		equipProgress = LerpedFloat.linear()
			.startWithValue(0);
		buttons = new Vector<>(15);
		for (int i = 0; i < 15; i++)
			buttons.add(LerpedFloat.linear()
				.startWithValue(0));
		axes = new Vector<>(6);
		for (int i = 0; i < 6; i++)
			axes.add(LerpedFloat.linear()
				.startWithValue(i < 4 ? 0 : -1));
	}

	public static void tick()
	{
		if (Minecraft.getInstance().isPaused())
			return;

		boolean active = TweakedLinkedControllerClientHandler.MODE != Mode.IDLE;
		equipProgress.chase(active ? 1 : 0, .2f, Chaser.EXP);
		equipProgress.tickChaser();

		if (!active)
			return;

		for (int i = 0; i < buttons.size(); i++)
		{
			LerpedFloat lerpedFloat = buttons.get(i);
			lerpedFloat.chase(TweakedLinkedControllerClientHandler.currentlyPressed.contains(i) ? 1 : 0, .4f, Chaser.EXP);
			lerpedFloat.tickChaser();
		}
		for (int i = 0; i < axes.size(); i++)
		{
			LerpedFloat lerpedFloat = axes.get(i);
			lerpedFloat.chase(TweakedLinkedControllerClientHandler.axes[i], 1.0f, Chaser.LINEAR);
			lerpedFloat.tickChaser();

		}
	}

	public static void resetButtons()
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			buttons.get(i).startWithValue(0);
		}
		for (int i = 0; i < axes.size(); i++)
		{
			axes.get(i).startWithValue(i < 4 ? 0.0f : -1.0f);
		}
	}

	@Override
	protected void render(ItemStack stack, TweakedLinkedControllerModel model, PartialItemModelRenderer renderer,
		ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light,
		int overlay)
	{
		renderNormal(stack, model, renderer, transformType, ms, light);
	}

	protected static void renderNormal(ItemStack stack, TweakedLinkedControllerModel model,
	  	PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType, PoseStack ms,
  		int light)
	{
		render(stack, model, renderer, transformType, ms, light, RenderType.NORMAL, false, false);
	}

	public static void renderInLectern(ItemStack stack, TweakedLinkedControllerModel model,
	  	PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType, PoseStack ms,
  		int light, boolean active, boolean renderDepression)
	{
		render(stack, model, renderer, transformType, ms, light, RenderType.LECTERN, active, renderDepression);
	}

	private static final Vec3[] positionList =
	{
		new Vec3(3, 0.9, 11.5).multiply(1/16.0, 1/16.0, 1/16.0), // SHOULDER BUTTONS
		new Vec3(3, 0.9, 2.5).multiply(1/16.0, 1/16.0, 1/16.0),

		new Vec3(6, 1, 8.5).multiply(1/16.0, 1/16.0, 1/16.0), // FACE BUTTONS
		new Vec3(6, 1, 6.5).multiply(1/16.0, 1/16.0, 1/16.0),
		new Vec3(5, 1, 7.5).multiply(1/16.0, 1/16.0, 1/16.0),

		new Vec3(6, 0.5, 11.5).multiply(1/16.0, 1/16.0, 1/16.0), // JOYSTICK
		new Vec3(9, 0.5, 5.5).multiply(1/16.0, 1/16.0, 1/16.0),

		new Vec3(8, 1, 9.5).multiply(1/16.0, 1/16.0, 1/16.0), // DPAD
		new Vec3(9, 1, 8.5).multiply(1/16.0, 1/16.0, 1/16.0),
		new Vec3(10, 1, 9.5).multiply(1/16.0, 1/16.0, 1/16.0),
		new Vec3(9, 1, 10.5).multiply(1/16.0, 1/16.0, 1/16.0),

		new Vec3(3, -0.1, 11.5).multiply(1/16.0, 1/16.0, 1/16.0), // TRIGGERS
		new Vec3(3, -0.1, 2.5).multiply(1/16.0, 1/16.0, 1/16.0),
	};

	protected static void render(ItemStack stack, TweakedLinkedControllerModel model,
	  	PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType, PoseStack ms,
  		int light, RenderType renderType, boolean active, boolean renderDepression)
	{
		float pt = AnimationTickHolder.getPartialTicks();
		TransformStack msr = TransformStack.cast(ms);

		ms.pushPose();

		if (renderType == RenderType.NORMAL)
		{
			Minecraft mc = Minecraft.getInstance();
			boolean rightHanded = mc.options.mainHand == HumanoidArm.RIGHT;
			TransformType mainHand =
					rightHanded ? TransformType.FIRST_PERSON_RIGHT_HAND : TransformType.FIRST_PERSON_LEFT_HAND;
			TransformType offHand =
					rightHanded ? TransformType.FIRST_PERSON_LEFT_HAND : TransformType.FIRST_PERSON_RIGHT_HAND;

			active = false;
			boolean noControllerInMain = !ModItems.TWEAKED_LINKED_CONTROLLER.isIn(mc.player.getMainHandItem());

			if (transformType == mainHand || (transformType == offHand && noControllerInMain))
			{
				float equip = equipProgress.getValue(pt);
				int handModifier = transformType == TransformType.FIRST_PERSON_LEFT_HAND ? -1 : 1;
				msr.translate(0, equip / 4, equip / 4 * handModifier);
				msr.rotateY(equip * -30 * handModifier);
				msr.rotateZ(equip * -30);
				active = true;
			}

			if (transformType == TransformType.GUI)
			{
				if (stack == mc.player.getMainHandItem())
					active = true;
				if (stack == mc.player.getOffhandItem() && noControllerInMain)
					active = true;
			}

			active &= TweakedLinkedControllerClientHandler.MODE != Mode.IDLE;

			renderDepression = true;
		}

		renderer.render(active ? BASE.get() : model.getOriginalModel(), light);

		if (!active)
		{
			ms.popPose();
			return;
		}

		float s = 1 / 16f;
		float b = s * -.75f;
		int index = 0;
		if (renderType == RenderType.NORMAL && TweakedLinkedControllerClientHandler.MODE == Mode.BIND)
		{
			int i = (int) Mth.lerp((Mth.sin(AnimationTickHolder.getRenderTime() / 4f) + 1) / 2, 5, 15);
			light = i << 20;
		}

		ms.pushPose();
		BakedModel button = BUTTON_A.get();
		renderButton(renderer, ms, light, pt, button, b, index++, renderDepression, false);
		button = BUTTON_B.get();
		renderButton(renderer, ms, light, pt, button, b, index++, renderDepression, false);
		button = BUTTON_X.get();
		renderButton(renderer, ms, light, pt, button, b, index++, renderDepression, false);
		button = BUTTON_Y.get();
		renderButton(renderer, ms, light, pt, button, b, index++, renderDepression, false);
		button = TRIGGER.get();
		for (; index < 6; index++)
		{
			ms.pushPose();
			msr.translate(positionList[index - 4]);
			renderButton(renderer, ms, light, pt, button, b, index, renderDepression, true);
			ms.popPose();
		}
		button = BUTTON.get();
		for (; index < 15; index++)
		{
			if (index == 9 || index == 10) continue;
			ms.pushPose();
			msr.translate(positionList[index - 4]);
			renderButton(renderer, ms, light, pt, button, b, index, renderDepression, false);
			ms.popPose();
		}
		button = JOYSTICK.get();
		renderJoystick(renderer, ms, light, pt, button, b, renderDepression, false);
		renderJoystick(renderer, ms, light, pt, button, b, renderDepression, true);
		button = TRIGGER.get();
		renderTrigger(renderer, ms, light, pt, button, false);
		renderTrigger(renderer, ms, light, pt, button, true);
		
		ms.popPose();
		ms.popPose();
	}

	protected static void renderButton(PartialItemModelRenderer renderer, PoseStack ms, int light, float pt, BakedModel button,
		float b, int index, boolean renderDepression, boolean isSideway)
		{
			ms.pushPose();
			if (renderDepression)
			{
				float depression = b * buttons.get(index).getValue(pt);
				if (isSideway)
				{
					ms.translate(-depression, 0, 0);
				}
				else
				{
					ms.translate(0, depression, 0);
				}
			}
			renderer.renderSolid(button, light);
			ms.popPose();
	}

	protected static void renderTrigger(PartialItemModelRenderer renderer, PoseStack ms, int light, float pt, BakedModel trigger, boolean isRight)
		{
			ms.pushPose();
			final float delta = 1 / 16f * -0.75f;
			Vec3 pos = positionList[isRight ? 12 : 11];
			float value = axes.get(isRight ? 5 : 4).getValue(pt);
			value = (value + 1) / 2 * delta;
			ms.translate(pos.x - value, pos.y, pos.z);
			renderer.renderSolid(trigger, light);
			ms.popPose();
	}

	protected static void renderJoystick(PartialItemModelRenderer renderer, PoseStack ms, int light, float pt, BakedModel joystick,
		float b, boolean renderDepression, boolean isRight)
		{
			ms.pushPose();
			final double delta = 7.5/16;
			Vec3 pos = positionList[isRight ? 6 : 5].subtract(delta, delta, delta);
			ms.translate(pos.x, pos.y, pos.z);
			ms.pushPose();
			float x, y;
			if (isRight)
			{
				x = axes.get(2).getValue(pt);
				y = axes.get(3).getValue(pt);
			}
			else
			{
				x = axes.get(0).getValue(pt);
				y = axes.get(1).getValue(pt);
			}
			Vector3f axis = new Vector3f(-x, 0, -y);
			double angle = x * x + y * y;
			angle = Math.min(Math.sqrt(angle), 1.0) * 0.6f;
			axis.normalize();
			ms.mulPose(new Quaternion(axis, (float)angle, false));
			if (renderDepression)
			{
				float depression = b * buttons.get(isRight ? 10 : 9).getValue(pt);
				ms.translate(0, depression, 0);
			}
			renderer.renderSolid(joystick, light);
			ms.popPose();
			ms.popPose();
	}

	@Override
	public TweakedLinkedControllerModel createModel(BakedModel originalModel)
	{
		return new TweakedLinkedControllerModel(originalModel);
	}

	protected enum RenderType
	{
		NORMAL,
		LECTERN;
	}

}