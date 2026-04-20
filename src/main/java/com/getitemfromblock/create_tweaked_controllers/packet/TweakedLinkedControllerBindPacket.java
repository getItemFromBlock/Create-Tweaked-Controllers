package com.getitemfromblock.create_tweaked_controllers.packet;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TweakedLinkedControllerBindPacket extends TweakedLinkedControllerPacketBase
{
    public static final Type<TweakedLinkedControllerBindPacket> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(CreateTweakedControllers.ID, "controller_bind"));

    public static final StreamCodec<FriendlyByteBuf, TweakedLinkedControllerBindPacket> STREAM_CODEC =
        StreamCodec.of(
            (buf, pkt) -> {
                writeBase(buf, pkt);
                buf.writeVarInt(pkt.button);
                buf.writeBlockPos(pkt.linkLocation);
            },
            buf -> {
                readBase(buf);
                int button = buf.readVarInt();
                BlockPos link = buf.readBlockPos();
                return new TweakedLinkedControllerBindPacket(button, link);
            });

    private final int button;
    private final BlockPos linkLocation;

    public TweakedLinkedControllerBindPacket(int button, BlockPos linkLocation)
    {
        super(null, false);
        this.button = button;
        this.linkLocation = linkLocation;
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    @Override
    protected void handleItem(ServerPlayer player, ItemStack heldItem)
    {
        if (player.isSpectator())
            return;

        ItemStackHandler frequencyItems = TweakedLinkedControllerItem.getFrequencyItems(heldItem);
        LinkBehaviour linkBehaviour = BlockEntityBehaviour.get(player.level(), linkLocation, LinkBehaviour.TYPE);
        if (linkBehaviour == null)
            return;

        linkBehaviour.getNetworkKey()
            .forEachWithContext((f, first) -> frequencyItems.setStackInSlot(button * 2 + (first ? 0 : 1), f.getStack()
                .copy()));

        // TODO Phase E: persist frequencyItems back to the controller stack via DataComponents instead of legacy NBT
        TweakedLinkedControllerItem.setFrequencyItems(heldItem, frequencyItems);
    }

    @Override
    protected void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern) {}
}
