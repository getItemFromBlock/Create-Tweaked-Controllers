package com.getitemfromblock.create_tweaked_controllers.packet;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class TweakedLinkedControllerStopLecternPacket extends TweakedLinkedControllerPacketBase
{
    public static final Type<TweakedLinkedControllerStopLecternPacket> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(CreateTweakedControllers.ID, "controller_stop_lectern"));

    public static final StreamCodec<FriendlyByteBuf, TweakedLinkedControllerStopLecternPacket> STREAM_CODEC =
        StreamCodec.of(
            (buf, pkt) -> writeBase(buf, pkt),
            buf -> {
                BaseFields b = readBase(buf);
                return new TweakedLinkedControllerStopLecternPacket(b.lecternPos());
            });

    public TweakedLinkedControllerStopLecternPacket(BlockPos lecternPos)
    {
        super(lecternPos, false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    @Override
    protected void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern)
    {
        lectern.tryStopUsing(player);
    }

    @Override
    protected void handleItem(ServerPlayer player, ItemStack heldItem) {}
}
