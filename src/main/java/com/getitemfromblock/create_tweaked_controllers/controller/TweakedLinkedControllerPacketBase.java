package com.getitemfromblock.create_extended_controllers.controller.extended;

import java.util.function.Supplier;

import com.getitemfromblock.create_extended_controllers.ModItems;
import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;

public abstract class ExtendedLinkedControllerPacketBase extends SimplePacketBase
{

	private BlockPos lecternPos;

	public ExtendedLinkedControllerPacketBase(BlockPos lecternPos)
	{
		this.lecternPos = lecternPos;
	}

	public ExtendedLinkedControllerPacketBase(FriendlyByteBuf buffer)
	{
		if (buffer.readBoolean())
		{
			lecternPos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
		}
	}

	protected boolean inLectern()
	{
		return lecternPos != null;
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(inLectern());
		if (inLectern())
		{
			buffer.writeInt(lecternPos.getX());
			buffer.writeInt(lecternPos.getY());
			buffer.writeInt(lecternPos.getZ());
		}
	}

	@Override
	public void handle(Supplier<Context> context)
	{
		context.get().enqueueWork(() -> {
			ServerPlayer player = context.get().getSender();
			if (player == null)
				return;

			if (inLectern())
			{
				BlockEntity be = player.level.getBlockEntity(lecternPos);
				if (!(be instanceof ExtendedLecternControllerBlockEntity))
					return;
				handleLectern(player, (ExtendedLecternControllerBlockEntity) be);
			}
			else
			{
				ItemStack controller = player.getMainHandItem();
				if (!ModItems.EXTENDED_LINKED_CONTROLLER.isIn(controller))
				{
					controller = player.getOffhandItem();
					if (!ModItems.EXTENDED_LINKED_CONTROLLER.isIn(controller))
						return;
				}
				handleItem(player, controller);
			}
		});

		context.get().setPacketHandled(true);
	}

	protected abstract void handleItem(ServerPlayer player, ItemStack heldItem);
	protected abstract void handleLectern(ServerPlayer player, ExtendedLecternControllerBlockEntity lectern);

}
