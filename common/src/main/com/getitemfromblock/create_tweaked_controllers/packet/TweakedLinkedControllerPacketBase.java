package com.getitemfromblock.create_tweaked_controllers.packet;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.item.ModItems;
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
    protected boolean useFullPrecision = false;

    public TweakedLinkedControllerPacketBase(BlockPos lecternPos)
    {
        this.lecternPos = lecternPos;
    }

    public TweakedLinkedControllerPacketBase(FriendlyByteBuf buffer)
    {
        byte val = buffer.readByte();
        if ((val & 0x1) != 0)
        {
            lecternPos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        useFullPrecision = (val & 0x2) != 0;
    }

    protected boolean inLectern()
    {
        return lecternPos != null;
    }

    @Override
    public void write(FriendlyByteBuf buffer)
    {
        byte mask = (byte) ((inLectern() ? 1 : 0) | (useFullPrecision ? 2 : 0));
        buffer.writeByte(mask);
        if (inLectern())
        {
            buffer.writeInt(lecternPos.getX());
            buffer.writeInt(lecternPos.getY());
            buffer.writeInt(lecternPos.getZ());
        }
    }

    @Override
    public boolean handle(Context context)
    {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
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
        return true;
    }

    protected abstract void handleItem(ServerPlayer player, ItemStack heldItem);
    protected abstract void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern);

}
