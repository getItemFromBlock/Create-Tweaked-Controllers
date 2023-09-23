package com.getitemfromblock.create_tweaked_controllers.packet;

import java.util.UUID;
import java.util.ArrayList;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.controller.ControllerRedstoneOutput;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerServerHandler;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import com.simibubi.create.foundation.utility.Couple;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TweakedLinkedControllerAxisPacket extends TweakedLinkedControllerPacketBase
{
    private int axis = 0;
    private float fullAxis[];

    public TweakedLinkedControllerAxisPacket(int axisIn)
    {
        this(axisIn, null);
    }

    public TweakedLinkedControllerAxisPacket(int axisIn, BlockPos lecternPos)
    {
        super(lecternPos);
        axis = axisIn;
    }

    public TweakedLinkedControllerAxisPacket(float[] axisIn, int axisL, BlockPos lecternPos)
    {
        super(lecternPos);
        useFullPrecision = true;
        fullAxis = new float[6];
        axis = axisL;
        for (byte i = 0; i < 6; i++)
        {
            fullAxis[i] = axisIn[i];
        }
    }

    public TweakedLinkedControllerAxisPacket(FriendlyByteBuf buffer)
    {
        super(buffer);
        if (useFullPrecision)
        {
            fullAxis = new float[6];
            for (byte i = 0; i < 6; i++)
            {
                fullAxis[i] = buffer.readFloat();
            }
        }
        axis = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer)
    {
        super.write(buffer);
        if (useFullPrecision)
        {
            for (byte i = 0; i < 6; i++)
            {
                buffer.writeFloat(fullAxis[i]);
            }
        }
        buffer.writeInt(axis);
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
            axisCouples.add(TweakedLinkedControllerItem.toFrequency(heldItem, i + 15));
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
            axisValues.add(dt);
        }
        TweakedLinkedControllerServerHandler.ReceiveAxis(world, pos, uniqueID, axisCouples, axisValues);
    }

}
