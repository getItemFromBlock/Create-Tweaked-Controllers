package com.getitemfromblock.create_tweaked_controllers.packet;

import java.util.ArrayList;
import java.util.UUID;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.controller.ControllerRedstoneOutput;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerServerHandler;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import net.createmod.catnip.data.Couple;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TweakedLinkedControllerAxisPacket extends TweakedLinkedControllerPacketBase
{
    public static final Type<TweakedLinkedControllerAxisPacket> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(CreateTweakedControllers.ID, "controller_axis"));

    public static final StreamCodec<FriendlyByteBuf, TweakedLinkedControllerAxisPacket> STREAM_CODEC =
        StreamCodec.of(
            (buf, pkt) -> {
                writeBase(buf, pkt);
                if (pkt.useFullPrecision)
                {
                    for (byte i = 0; i < 6; i++)
                        buf.writeFloat(pkt.fullAxis[i]);
                }
                buf.writeInt(pkt.axis);
            },
            buf -> {
                BaseFields b = readBase(buf);
                float[] full = null;
                if (b.useFullPrecision())
                {
                    full = new float[6];
                    for (byte i = 0; i < 6; i++)
                        full[i] = buf.readFloat();
                }
                int axisVal = buf.readInt();
                if (b.useFullPrecision())
                    return new TweakedLinkedControllerAxisPacket(full, axisVal, b.lecternPos());
                return new TweakedLinkedControllerAxisPacket(axisVal, b.lecternPos());
            });

    private final int axis;
    private final float[] fullAxis;

    public TweakedLinkedControllerAxisPacket(int axisIn)
    {
        this(axisIn, null);
    }

    public TweakedLinkedControllerAxisPacket(int axisIn, BlockPos lecternPos)
    {
        super(lecternPos, false);
        this.axis = axisIn;
        this.fullAxis = null;
    }

    public TweakedLinkedControllerAxisPacket(float[] axisIn, int axisL, BlockPos lecternPos)
    {
        super(lecternPos, true);
        this.axis = axisL;
        this.fullAxis = new float[6];
        for (byte i = 0; i < 6; i++)
            this.fullAxis[i] = axisIn[i];
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    @Override
    protected void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern)
    {
        if (lectern.isUsedBy(player))
        {
            handleItem(player, lectern.getController());
            if (useFullPrecision)
            {
                lectern.ReceiveFullStates(fullAxis);
            }
            else
            {
                lectern.ReceiveAxisStates(axis);
            }
        }
    }

    @Override
    protected void handleItem(ServerPlayer player, ItemStack heldItem)
    {
        Level world = player.getCommandSenderWorld();
        UUID uniqueID = player.getUUID();
        BlockPos pos = player.blockPosition();

        if (player.isSpectator())
            return;

        ControllerRedstoneOutput output = new ControllerRedstoneOutput();
        output.DecodeAxis(axis);
        ArrayList<Couple<Frequency>> axisCouples = new ArrayList<>(10);
        ArrayList<Byte> axisValues = new ArrayList<>(10);
        for (byte i = 0; i < 10; ++i)
        {
            byte dt = 0;
            if (i < 8)
            {
                boolean hasHighBit = (output.axis[i/2] & 0x10) != 0;
                if ((i % 2 == 1) == hasHighBit)
                {
                    dt = (byte)(output.axis[i/2] & 0x0f);
                }
            }
            else
            {
                dt = output.axis[i - 4];
            }
            Couple<Frequency> targetFreq = TweakedLinkedControllerItem.toFrequency(heldItem, i + 15);
            int target = axisCouples.indexOf(targetFreq);
            if (target >= 0)
            {
                byte other = axisValues.get(target);
                axisValues.set(target, dt > other ? dt : other);
                continue;
            }
            axisCouples.add(targetFreq);
            axisValues.add(dt);
        }
        TweakedLinkedControllerServerHandler.ReceiveAxis(world, pos, uniqueID, axisCouples, axisValues);
    }
}
