package com.getitemfromblock.create_tweaked_controllers.controller;

import java.util.function.Supplier;

import com.getitemfromblock.create_tweaked_controllers.ModItems;
import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;

public abstract class TweakedLinkedControllerPacketBase extends SimplePacketBase
{

	private BlockPos lecternPos;

	public TweakedLinkedControllerPacketBase(BlockPos lecternPos)
	{
		this.lecternPos = lecternPos;
	}

	public TweakedLinkedControllerPacketBase(FriendlyByteBuf buffer)
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
				if (!(be instanceof TweakedLecternControllerBlockEntity))
					return;
				handleLectern(player, (TweakedLecternControllerBlockEntity) be);
			}
			else
			{
				ItemStack controller = player.getMainHandItem();
				if (!ModItems.TWEAKED_LINKED_CONTROLLER.isIn(controller))
				{
					controller = player.getOffhandItem();
					if (!ModItems.TWEAKED_LINKED_CONTROLLER.isIn(controller))
						return;
				}
				handleItem(player, controller);
			}
		});

		context.get().setPacketHandled(true);
	}

	protected abstract void handleItem(ServerPlayer player, ItemStack heldItem);
	protected abstract void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern);

}
