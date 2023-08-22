package com.getitemfromblock.create_tweaked_controllers.packet;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class TweakedLinkedControllerStopLecternPacket extends TweakedLinkedControllerPacketBase
{

    public TweakedLinkedControllerStopLecternPacket(FriendlyByteBuf buffer)
    {
        super(buffer);
    }

    public TweakedLinkedControllerStopLecternPacket(BlockPos lecternPos)
    {
        super(lecternPos);
    }

    @Override
    protected void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern)
    {
        lectern.tryStopUsing(player);
    }

    @Override
    protected void handleItem(ServerPlayer player, ItemStack heldItem)
    {
    }

}
