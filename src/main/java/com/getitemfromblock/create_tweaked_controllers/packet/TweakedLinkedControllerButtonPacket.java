package com.getitemfromblock.create_tweaked_controllers.packet;

import java.util.ArrayList;
import java.util.UUID;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.controller.ControllerRedstoneOutput;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerServerHandler;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import net.createmod.catnip.data.Couple;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TweakedLinkedControllerButtonPacket extends TweakedLinkedControllerPacketBase
{

    private short buttonStates = 0;

    public TweakedLinkedControllerButtonPacket(short buttons)
    {
        this(buttons, null);
    }

    public TweakedLinkedControllerButtonPacket(short button, BlockPos lecternPos)
    {
        super(lecternPos);
        this.buttonStates = button;
    }

    public TweakedLinkedControllerButtonPacket(FriendlyByteBuf buffer)
    {
        super(buffer);
        buttonStates = buffer.readShort();
    }

    @Override
    public void write(FriendlyByteBuf buffer)
    {
        super.write(buffer);
        buffer.writeShort(buttonStates);
    }

    @Override
    protected void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern)
    {
        if (lectern.isUsedBy(player))
        {
            handleItem(player, lectern.getController());
            lectern.ReceiveButtonStates(buttonStates);
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
        output.DecodeButtons(buttonStates);
        ArrayList<Couple<Frequency>> buttonCouples = new ArrayList<>(15);
        ArrayList<Boolean> buttonValues = new ArrayList<>(15);
        for (int i = 0; i < 15; ++i)
        {
            boolean buttonValue = (buttonStates & (1 << i)) != 0;
            Couple<Frequency> targetFreq = TweakedLinkedControllerItem.toFrequency(heldItem, i);
            int target = buttonCouples.indexOf(targetFreq);
            if (target >= 0)
            {
                boolean other = buttonValues.get(target);
                buttonValues.set(target, other || buttonValue);
                continue;
            }
            buttonCouples.add(targetFreq);
            buttonValues.add(buttonValue);
        }
        TweakedLinkedControllerServerHandler.ReceivePressed(world, pos, uniqueID, buttonCouples, buttonValues);
    }

}
