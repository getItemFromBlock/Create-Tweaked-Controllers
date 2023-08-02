package com.getitemfromblock.create_tweaked_controllers.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.Vector;

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

public class TweakedLinkedControllerServerHandler
{
	public static WorldAttached<Map<UUID, Collection<TweakedManualFrequency>>> receivedInputs =
		new WorldAttached<>($ -> new HashMap<>());
	public static WorldAttached<Map<UUID, Vector<TweakedManualAxisFrequency>>> receivedAxes =
		new WorldAttached<>($ -> new HashMap<>());
	static final int TIMEOUT = 30;

	public static void tick(LevelAccessor world)
	{
		Map<UUID, Collection<TweakedManualFrequency>> map = receivedInputs.get(world);
		for (Iterator<Entry<UUID, Collection<TweakedManualFrequency>>> iterator = map.entrySet()
			.iterator(); iterator.hasNext();) {

			Entry<UUID, Collection<TweakedManualFrequency>> entry = iterator.next();
			Collection<TweakedManualFrequency> list = entry.getValue();

			for (Iterator<TweakedManualFrequency> entryIterator = list.iterator(); entryIterator.hasNext();)
			{
				TweakedManualFrequency TweakedManualFrequency = entryIterator.next();
				TweakedManualFrequency.decrement();
				if (!TweakedManualFrequency.isAlive())
				{
					Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(world, TweakedManualFrequency);
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
		Map<UUID, Collection<TweakedManualFrequency>> map = receivedInputs.get(world);
		Collection<TweakedManualFrequency> list = map.computeIfAbsent(uniqueID, $ -> new ArrayList<>());

		WithNext: for (Couple<Frequency> activated : collect)
		{
			for (Iterator<TweakedManualFrequency> iterator = list.iterator(); iterator.hasNext();)
			{
				TweakedManualFrequency entry = iterator.next();
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

			TweakedManualFrequency entry = new TweakedManualFrequency(pos, activated);
			Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, entry);
			list.add(entry);
			
			for (IRedstoneLinkable linkable : Create.REDSTONE_LINK_NETWORK_HANDLER.getNetworkOf(world, entry)) 
				if (linkable instanceof LinkBehaviour lb && lb.isListening())
					AllAdvancements.LINKED_CONTROLLER.awardTo(world.getPlayerByUUID(uniqueID));
		}
	}

	public static void receiveAxes(LevelAccessor world, BlockPos pos, UUID uniqueID, Vector<Couple<Frequency>> collect, Vector<Integer> values)
		{
		Map<UUID, Vector<TweakedManualAxisFrequency>> map = receivedAxes.get(world);
		Vector<TweakedManualAxisFrequency> list = map.computeIfAbsent(uniqueID, $ -> new Vector<>(10));

		WithNext: for (int i = 0; i < 10; i++)
		{
			for (Iterator<TweakedManualAxisFrequency> iterator = list.iterator(); iterator.hasNext();)
			{
				TweakedManualAxisFrequency entry = iterator.next();
				if (entry.getSecond()
					.equals(collect.get(i)))
				{
					entry.SetLevel(values.get(i));
					continue WithNext;
				}
			}

			TweakedManualAxisFrequency entry = new TweakedManualAxisFrequency(pos, values.get(i), collect.get(i));
			Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, entry);
			list.add(entry);
			
			for (IRedstoneLinkable linkable : Create.REDSTONE_LINK_NETWORK_HANDLER.getNetworkOf(world, entry)) 
				if (linkable instanceof LinkBehaviour lb && lb.isListening())
					AllAdvancements.LINKED_CONTROLLER.awardTo(world.getPlayerByUUID(uniqueID));
		}
	}

	static class TweakedManualFrequency extends IntAttached<Couple<Frequency>> implements IRedstoneLinkable
	{

		private BlockPos pos;

		public TweakedManualFrequency(BlockPos pos, Couple<Frequency> second)
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

	static class TweakedManualAxisFrequency extends IntAttached<Couple<Frequency>> implements IRedstoneLinkable
	{

		private BlockPos pos;
		private int level = 0;

		public TweakedManualAxisFrequency(BlockPos pos, int level, Couple<Frequency> second)
		{
			super(TIMEOUT, second);
			this.pos = pos;
			this.level = level;
		}

		public void updatePosition(BlockPos pos)
		{
			this.pos = pos;
			setFirst(TIMEOUT);
		}

		public void SetLevel(int level)
		{
			this.level = level;
		}

		@Override
		public int getTransmittedStrength()
		{
			return isAlive() ? level : 0;
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
		public void setReceivedStrength(int power){}

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
