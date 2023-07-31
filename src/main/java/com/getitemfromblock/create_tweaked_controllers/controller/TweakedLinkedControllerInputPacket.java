package com.getitemfromblock.create_extended_controllers.controller.extended;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExtendedLinkedControllerInputPacket extends ExtendedLinkedControllerPacketBase
{

	private Collection<Integer> activatedButtons;
	private boolean press;

	public ExtendedLinkedControllerInputPacket(Collection<Integer> activatedButtons, boolean press)
	{
		this(activatedButtons, press, null);
	}

	public ExtendedLinkedControllerInputPacket(Collection<Integer> activatedButtons, boolean press, BlockPos lecternPos)
	{
		super(lecternPos);
		this.activatedButtons = activatedButtons;
		this.press = press;
	}

	public ExtendedLinkedControllerInputPacket(FriendlyByteBuf buffer)
	{
		super(buffer);
		activatedButtons = new ArrayList<>();
		press = buffer.readBoolean();
		int size = buffer.readVarInt();
		for (int i = 0; i < size; i++)
			activatedButtons.add(buffer.readVarInt());
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		super.write(buffer);
		buffer.writeBoolean(press);
		buffer.writeVarInt(activatedButtons.size());
		activatedButtons.forEach(buffer::writeVarInt);
	}

	@Override
	protected void handleLectern(ServerPlayer player, ExtendedLecternControllerBlockEntity lectern)
	{
		if (lectern.isUsedBy(player))
			handleItem(player, lectern.getController());
	}

	@Override
	protected void handleItem(ServerPlayer player, ItemStack heldItem)
	{
		Level world = player.getCommandSenderWorld();
		UUID uniqueID = player.getUUID();
		BlockPos pos = player.blockPosition();

		if (player.isSpectator() && press)
			return;
		
		ExtendedLinkedControllerServerHandler.receivePressed(world, pos, uniqueID, activatedButtons.stream()
			.map(i -> ExtendedLinkedControllerItem.toFrequency(heldItem, i))
			.collect(Collectors.toList()), press);
	}

}
