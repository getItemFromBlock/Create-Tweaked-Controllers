package com.getitemfromblock.create_extended_controllers.controller.extended;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ExtendedLinkedControllerStopLecternPacket extends ExtendedLinkedControllerPacketBase
{

	public ExtendedLinkedControllerStopLecternPacket(FriendlyByteBuf buffer)
	{
		super(buffer);
	}

	public ExtendedLinkedControllerStopLecternPacket(BlockPos lecternPos)
	{
		super(lecternPos);
	}

	@Override
	protected void handleLectern(ServerPlayer player, ExtendedLecternControllerBlockEntity lectern)
	{
		lectern.tryStopUsing(player);
	}

	@Override
	protected void handleItem(ServerPlayer player, ItemStack heldItem)
	{
	}

}
