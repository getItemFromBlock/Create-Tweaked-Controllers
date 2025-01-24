package com.getitemfromblock.create_tweaked_controllers.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.WorldAttached;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public class TweakedLinkedControllerServerHandler
{
    public static WorldAttached<Map<UUID, Collection<TweakedManualFrequency>>> receivedInputs =
        new WorldAttached<>($ -> new HashMap<>());
    public static WorldAttached<Map<UUID, ArrayList<TweakedManualAxisFrequency>>> receivedAxes =
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

        Map<UUID, ArrayList<TweakedManualAxisFrequency>> map2 = receivedAxes.get(world);
        for (Iterator<Entry<UUID, ArrayList<TweakedManualAxisFrequency>>> iterator = map2.entrySet()
            .iterator(); iterator.hasNext();) {

            Entry<UUID, ArrayList<TweakedManualAxisFrequency>> entry = iterator.next();
            ArrayList<TweakedManualAxisFrequency> list = entry.getValue();

            for (Iterator<TweakedManualAxisFrequency> entryIterator = list.iterator(); entryIterator.hasNext();)
            {
                TweakedManualAxisFrequency TweakedManualAxisFrequency = entryIterator.next();
                TweakedManualAxisFrequency.decrement();
                if (!TweakedManualAxisFrequency.isAlive())
                {
                    Create.REDSTONE_LINK_NETWORK_HANDLER.removeFromNetwork(world, TweakedManualAxisFrequency);
                    entryIterator.remove();
                }
                else
                {
                    Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(world, TweakedManualAxisFrequency);
                }
            }

            if (list.isEmpty())
                iterator.remove();
        }
    }

    public static void ReceivePressed(LevelAccessor world, BlockPos pos, UUID uniqueID, ArrayList<Couple<Frequency>> collect, ArrayList<Boolean> values)
        {
        Map<UUID, Collection<TweakedManualFrequency>> map = receivedInputs.get(world);
        Collection<TweakedManualFrequency> list = map.computeIfAbsent(uniqueID, $ -> new ArrayList<>());

        WithNext: for (int i = 0; i < collect.size(); i++)
        {
            for (Iterator<TweakedManualFrequency> iterator = list.iterator(); iterator.hasNext();)
            {
                TweakedManualFrequency entry = iterator.next();
                if (entry.getSecond()
                    .equals(collect.get(i)))
                {
                    if (!values.get(i))
                        entry.setFirst(0);
                    else
                        entry.updatePosition(pos);
                    continue WithNext;
                }
            }

            if (!values.get(i))
                continue;

            TweakedManualFrequency entry = new TweakedManualFrequency(pos, collect.get(i));
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, entry);
            list.add(entry);
            
            for (IRedstoneLinkable linkable : Create.REDSTONE_LINK_NETWORK_HANDLER.getNetworkOf(world, entry)) 
                if (linkable instanceof LinkBehaviour lb && lb.isListening())
                    AllAdvancements.LINKED_CONTROLLER.awardTo(world.getPlayerByUUID(uniqueID));
        }
    }

    public static void ReceiveAxis(LevelAccessor world, BlockPos pos, UUID uniqueID, ArrayList<Couple<Frequency>> collect, ArrayList<Byte> values)
        {
        Map<UUID, ArrayList<TweakedManualAxisFrequency>> map = receivedAxes.get(world);
        ArrayList<TweakedManualAxisFrequency> list = map.computeIfAbsent(uniqueID, $ -> new ArrayList<>(10));
        WithNext: for (int i = 0; i < collect.size(); i++)
        {
            for (Iterator<TweakedManualAxisFrequency> iterator = list.iterator(); iterator.hasNext();)
            {
                TweakedManualAxisFrequency entry = iterator.next();
                if (entry.getSecond()
                    .equals(collect.get(i)))
                {
                    entry.SetLevel(values.get(i));
                    entry.updatePosition(pos);
                    continue WithNext;
                }
            }

            TweakedManualAxisFrequency entry = new TweakedManualAxisFrequency(pos, values.get(i), collect.get(i));
            Create.REDSTONE_LINK_NETWORK_HANDLER.addToNetwork(world, entry);
            list.add(entry);
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
