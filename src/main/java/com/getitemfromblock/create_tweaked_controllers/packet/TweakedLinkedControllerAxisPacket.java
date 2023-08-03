package com.getitemfromblock.create_tweaked_controllers.packet;

import java.util.UUID;
import java.util.Vector;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerServerHandler;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import com.simibubi.create.content.logistics.RedstoneLinkNetworkHandler.Frequency;
import com.simibubi.create.foundation.utility.Couple;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TweakedLinkedControllerAxisPacket extends TweakedLinkedControllerPacketBase
{
	private float[] axes = {0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -1.0f};

	public TweakedLinkedControllerAxisPacket(float[] axesIn)
	{
		this(axesIn, null);
	}

	public TweakedLinkedControllerAxisPacket(float[] axesIn, BlockPos lecternPos)
	{
		super(lecternPos);
        if (axesIn == null) return;
        for (int i = 0; i < 6; i++)
        {
            axes[i] = axesIn[i];
        }
	}

	public TweakedLinkedControllerAxisPacket(FriendlyByteBuf buffer)
	{
		super(buffer);
		for (int i = 0; i < 6; i++)
			axes[i] = buffer.readFloat();
	}

	@Override
	public void write(FriendlyByteBuf buffer)
	{
		super.write(buffer);
		for (int i = 0; i < 6; i++)
            buffer.writeFloat(axes[i]);
	}

	@Override
	protected void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern)
	{
		if (lectern.isUsedBy(player))
			handleItem(player, lectern.getController());
	}

	@Override
	protected void handleItem(ServerPlayer player, ItemStack heldItem)
	{
		Level world = player.getCommandSenderWorld();
		UUID uniqueID = player.getUUID();
		BlockPos pos = player.blockPosition();

		if (player.isSpectator())
			return;
		
        Vector<Couple<Frequency>> axesCouples = new Vector<>(10);
        Vector<Integer> axesValues = new Vector<>(10);
        for (int i = 0; i < 10; ++i)
        {
            axesCouples.add(TweakedLinkedControllerItem.toFrequency(heldItem, i + 15));
            float dt;
			if (i < 8)
            {
                dt = (i % 2 == 1) ? -1 : 1;
                dt *= axes[i / 2];
            }
            else
            {
                dt = axes[i - 4];
				dt = (dt + 1) / 2;
            }
			if (dt < 0) dt = 0;
			if (dt > 1) dt = 1;
			axesValues.add((int)(dt * 15));
        }
		TweakedLinkedControllerServerHandler.receiveAxes(world, pos, uniqueID, axesCouples, axesValues);
	}

}
