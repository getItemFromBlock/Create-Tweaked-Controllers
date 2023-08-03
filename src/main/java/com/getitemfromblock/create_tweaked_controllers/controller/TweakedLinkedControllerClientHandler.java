package com.getitemfromblock.create_tweaked_controllers.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.getitemfromblock.create_tweaked_controllers.ControllerInputs;
import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.block.ModBlocks;
import com.getitemfromblock.create_tweaked_controllers.item.ModItems;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItemRenderer;
import com.getitemfromblock.create_tweaked_controllers.packet.ModPackets;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerAxisPacket;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerBindPacket;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerInputPacket;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerStopLecternPacket;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.tileEntity.behaviour.linked.LinkBehaviour;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;

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
	public static float[] axes = {0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -1.0f};
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

	public static void ClearAxes()
	{
		for (int i = 0; i < axes.length; i++)
        {
            axes[i] = i < 4 ? 0.0f : -1.0f;
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
		selectedLocation = BlockPos.ZERO;
		packetCooldown = 0;
		if (inLectern())
			ModPackets.channel.sendToServer(new TweakedLinkedControllerStopLecternPacket(lecternPos));
		lecternPos = null;

		if (!currentlyPressed.isEmpty())
			ModPackets.channel.sendToServer(new TweakedLinkedControllerInputPacket(currentlyPressed, false));
		currentlyPressed.clear();
		ModPackets.channel.sendToServer(new TweakedLinkedControllerAxisPacket(null, null));
		ClearAxes();
			
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
		for (int i = 0; i < 6; i++)
		{
			axes[i] = controls.axis[i];
		}
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

			ModPackets.channel.sendToServer(new TweakedLinkedControllerAxisPacket(controls.axis, lecternPos));
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
					CreateTweakedControllers.translate("tweaked_linked_controller.key_bound", ControllerInputs.GetButtonName(integer)).sendStatus(mc.player);
				}
				MODE = Mode.IDLE;
				break;
			}
			if (MODE == Mode.BIND)
			{
				for (int i = 0; i < 6; i++)
				{
					if ((i < 4 && Math.abs(controls.axis[i]) > 0.8f) || (i >= 4 && controls.axis[i] > 0))
					{
						LinkBehaviour linkBehaviour = TileEntityBehaviour.get(mc.level, selectedLocation, LinkBehaviour.TYPE);
						if (linkBehaviour != null)
						{
							int a = i >= 4 ? i + 4 : i * 2 + (controls.axis[i] < 0 ? 1 : 0);
							ModPackets.channel.sendToServer(new TweakedLinkedControllerBindPacket(a + 15, selectedLocation));
							CreateTweakedControllers.translate("tweaked_linked_controller.key_bound", ControllerInputs.GetAxisName(a)).sendStatus(mc.player);
						}
						MODE = Mode.IDLE;
						break;
					}
				}
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
		Screen tooltipScreen = new Screen(Components.immutableEmpty()) {};
		tooltipScreen.init(mc, width1, height1);

		List<Component> list = new ArrayList<>();
		list.add(CreateTweakedControllers.translateDirect("tweaked_linked_controller.bind_mode")
			.withStyle(ChatFormatting.GOLD));
		
		list.add(CreateTweakedControllers.translateDirect("tweaked_linked_controller.press_keybind")
			.withStyle(ChatFormatting.GRAY));

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
