package com.getitemfromblock.create_extended_controllers.controller.extended;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.IRedstoneLinkable;
import com.simibubi.create.content.logistics.RedstoneLinkNetworkHandler.Frequency;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.tileEntity.behaviour.linked.LinkBehaviour;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.WorldAttached;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public class ExtendedLinkedControllerServerHandler
{

	public static WorldAttached<Map<UUID, Collection<ExtendedManualFrequency>>> receivedInputs =
		new WorldAttached<>($ -> new HashMap<>());
	static final int TIMEOUT = 30;

	public static void tick(LevelAccessor world)
	{
		Map<UUID, Collection<ExtendedManualFrequency>> map = receivedInputs.get(world);
		for (Iterator<Entry<UUID, Collection<ExtendedManualFrequency>>> iterator = map.entrySet()
			.iterator(); iterator.hasNext();) {

			Entry<UUID, Collection<ExtendedManualFrequency>> entry = iterator.next();
			Collection<ExtendedManualFrequency> list = entry.getValue();

			for (Iterator<ExtendedManualFrequency> entryIterator = list.iterator(); entryIterator.hasNext();)
			{
				ExtendedManualFrequency ExtendedManualFrequency = entryIterator.next();
				ExtendedManualFrequency.decrement();
				if (!ExtendedManualFrequency.isAlive())
				{
					Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(world, ExtendedManualFrequency);
					entryIterator.remove();
				}
			}

			if (list.isEmpty())
				iterator.remove();
		}
	}

	public static void receivePressed(LevelAccessor world, BlockPos pos, UUID uniqueID, List<Couple<Frequency>> collect,
		boolean pressed)
		{
		Map<UUID, Collection<ExtendedManualFrequency>> map = receivedInputs.get(world);
		Collection<ExtendedManualFrequency> list = map.computeIfAbsent(uniqueID, $ -> new ArrayList<>());

		WithNext: for (Couple<Frequency> activated : collect)
		{
			for (Iterator<ExtendedManualFrequency> iterator = list.iterator(); iterator.hasNext();)
			{
				ExtendedManualFrequency entry = iterator.next();
				if (entry.getSecond()
					.equals(activated)) {
					if (!pressed)
						entry.setFirst(0);
					else
						entry.updatePosition(pos);
					continue WithNext;
				}
			}

			if (!pressed)
				continue;

			ExtendedManualFrequency entry = new ExtendedManualFrequency(pos, activated);
			Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, entry);
			list.add(entry);
			
			for (IRedstoneLinkable linkable : Create.REDSTONE_LINK_NETWORK_HANDLER.getNetworkOf(world, entry)) 
				if (linkable instanceof LinkBehaviour lb && lb.isListening())
					AllAdvancements.LINKED_CONTROLLER.awardTo(world.getPlayerByUUID(uniqueID));
		}
	}

	static class ExtendedManualFrequency extends IntAttached<Couple<Frequency>> implements IRedstoneLinkable
	{

		private BlockPos pos;

		public ExtendedManualFrequency(BlockPos pos, Couple<Frequency> second)
		{
			super(TIMEOUT, second);
			this.pos = pos;
		}

		public void updatePosition(BlockPos pos)
		{
			this.pos = pos;
			setFirst(TIMEOUT);
		}

		@Override
		public int getTransmittedStrength()
		{
			return isAlive() ? 15 : 0;
		}

		@Override
		public boolean isAlive()
		{
			return getFirst() > 0;
		}

		@Override
		public BlockPos getLocation()
		{
			return pos;
		}

		@Override
		public void setReceivedStrength(int power) {}

		@Override
		public boolean isListening()
		{
			return false;
		}

		@Override
		public Couple<Frequency> getNetworkKey()
		{
			return getSecond();
		}

	}

}
