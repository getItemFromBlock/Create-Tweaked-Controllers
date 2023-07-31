package com.getitemfromblock.create_tweaked_controllers.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.lwjgl.glfw.GLFW;

import com.getitemfromblock.create_tweaked_controllers.ControllerInputs;
import com.getitemfromblock.create_tweaked_controllers.ModBlocks;
import com.getitemfromblock.create_tweaked_controllers.ModItems;
import com.getitemfromblock.create_tweaked_controllers.ModPackets;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.tileEntity.behaviour.linked.LinkBehaviour;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
//import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class TweakedLinkedControllerClientHandler
{

	public static final IIngameOverlay OVERLAY = TweakedLinkedControllerClientHandler::renderOverlay;

	public static Mode MODE = Mode.IDLE;
	public static int PACKET_RATE = 5;
	public static Collection<Integer> currentlyPressed = new HashSet<>();
	private static BlockPos lecternPos;
	private static BlockPos selectedLocation = BlockPos.ZERO;
	private static int packetCooldown;

	public static void toggleBindMode(BlockPos location)
	{
		if (MODE == Mode.IDLE)
		{
			MODE = Mode.BIND;
			selectedLocation = location;
		}
		else
		{
			MODE = Mode.IDLE;
			onReset();
		}
	}

	public static void toggle()
	{
		if (MODE == Mode.IDLE)
		{
			MODE = Mode.ACTIVE;
			lecternPos = null;
		}
		else
		{
			MODE = Mode.IDLE;
			onReset();
		}
	}

	public static void activateInLectern(BlockPos lecternAt)
	{
		if (MODE == Mode.IDLE)
		{
			MODE = Mode.ACTIVE;
			lecternPos = lecternAt;
		}
	}

	public static void deactivateInLectern()
	{
		if (MODE == Mode.ACTIVE && inLectern())
		{
			MODE = Mode.IDLE;
			onReset();
		}
	}

	public static boolean inLectern()
	{
		return lecternPos != null;
	}

	protected static void onReset()
	{
		packetCooldown = 0;
		selectedLocation = BlockPos.ZERO;

		if (inLectern())
			ModPackets.channel.sendToServer(new TweakedLinkedControllerStopLecternPacket(lecternPos));
		lecternPos = null;

		if (!currentlyPressed.isEmpty())
			ModPackets.channel.sendToServer(new TweakedLinkedControllerInputPacket(currentlyPressed, false));
		currentlyPressed.clear();

		TweakedLinkedControllerItemRenderer.resetButtons();
	}

	public static void tick()
	{
		TweakedLinkedControllerItemRenderer.tick();

		if (MODE == Mode.IDLE)
			return;
		if (packetCooldown > 0)
			packetCooldown--;

		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		ItemStack heldItem = player.getMainHandItem();

		if (player.isSpectator())
		{
			MODE = Mode.IDLE;
			onReset();
			return;
		}

		if (!inLectern() && !ModItems.TWEAKED_LINKED_CONTROLLER.isIn(heldItem))
		{
			heldItem = player.getOffhandItem();
			if (!ModItems.TWEAKED_LINKED_CONTROLLER.isIn(heldItem))
			{
				MODE = Mode.IDLE;
				onReset();
				return;
			}
		}

		if (inLectern() && ModBlocks.TWEAKED_LECTERN_CONTROLLER.get()
			.getTileEntityOptional(mc.level, lecternPos)
			.map(be -> !be.isUsedBy(mc.player))
			.orElse(true)) {
			deactivateInLectern();
			return;
		}

		if (mc.screen != null)
		{
			MODE = Mode.IDLE;
			onReset();
			return;
		}

		if (InputConstants.isKeyDown(mc.getWindow()
			.getWindow(), GLFW.GLFW_KEY_ESCAPE))
			{
			MODE = Mode.IDLE;
			onReset();
			return;
		}
		ControllerInputs controls = new ControllerInputs();
		TweakedControlsUtil.GetControls(controls);
		Collection<Integer> pressedKeys = new HashSet<>();
		for (int i = 0; i < controls.buttons.length; i++)
		{
			if (TweakedControlsUtil.IsPressed(controls, i))
				pressedKeys.add(i);
		}

		Collection<Integer> newKeys = new HashSet<>(pressedKeys);
		Collection<Integer> releasedKeys = currentlyPressed;
		newKeys.removeAll(releasedKeys);
		releasedKeys.removeAll(pressedKeys);

		if (MODE == Mode.ACTIVE)
		{
			// Released Keys
			if (!releasedKeys.isEmpty())
			{
				ModPackets.channel.sendToServer(new TweakedLinkedControllerInputPacket(releasedKeys, false, lecternPos));
				AllSoundEvents.CONTROLLER_CLICK.playAt(player.level, player.blockPosition(), 1f, .5f, true);
			}

			// Newly Pressed Keys
			if (!newKeys.isEmpty())
			{
				ModPackets.channel.sendToServer(new TweakedLinkedControllerInputPacket(newKeys, true, lecternPos));
				packetCooldown = PACKET_RATE;
				AllSoundEvents.CONTROLLER_CLICK.playAt(player.level, player.blockPosition(), 1f, .75f, true);
			}

			// Keepalive Pressed Keys
			if (packetCooldown == 0)
			{
				if (!pressedKeys.isEmpty())
				{
					ModPackets.channel.sendToServer(new TweakedLinkedControllerInputPacket(pressedKeys, true, lecternPos));
					packetCooldown = PACKET_RATE;
				}
			}
		}

		if (MODE == Mode.BIND)
		{
			VoxelShape shape = mc.level.getBlockState(selectedLocation)
				.getShape(mc.level, selectedLocation);
			if (!shape.isEmpty())
				CreateClient.OUTLINER.showAABB("controller", shape.bounds()
					.move(selectedLocation))
					.colored(0x0104FF)
					.lineWidth(1 / 16f);

			for (Integer integer : newKeys)
			{
				LinkBehaviour linkBehaviour = TileEntityBehaviour.get(mc.level, selectedLocation, LinkBehaviour.TYPE);
				if (linkBehaviour != null)
				{
					ModPackets.channel.sendToServer(new TweakedLinkedControllerBindPacket(integer, selectedLocation));
					Lang.translate("linked_controller.key_bound", controls.GetButtonName(integer)).sendStatus(mc.player);
				}
				MODE = Mode.IDLE;
				break;
			}
		}

		currentlyPressed = pressedKeys;
		controls.Empty();
	}

	public static void renderOverlay(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width1,
		int height1) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.options.hideGui)
			return;

		if (MODE != Mode.BIND)
			return;

		poseStack.pushPose();
		Screen tooltipScreen = new Screen(Components.immutableEmpty()) {
		};
		tooltipScreen.init(mc, width1, height1);

		List<Component> list = new ArrayList<>();
		list.add(Lang.translateDirect("linked_controller.bind_mode")
			.withStyle(ChatFormatting.GOLD));
		
		//	list.addAll(TooltipHelper.cutTextComponent(Lang.translateDirect("linked_controller.press_keybind", keys),
		//	ChatFormatting.GRAY, ChatFormatting.GRAY));

		int width = 0;
		int height = list.size() * mc.font.lineHeight;
		for (Component iTextComponent : list)
			width = Math.max(width, mc.font.width(iTextComponent));
		int x = (width1 / 3) - width / 2;
		int y = height1 - height - 24;

		// TODO
		tooltipScreen.renderComponentTooltip(poseStack, list, x, y);

		poseStack.popPose();
	}

	public enum Mode
	{
		IDLE,
		ACTIVE,
		BIND
	}

}
