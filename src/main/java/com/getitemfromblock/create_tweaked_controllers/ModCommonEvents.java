package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerServerHandler;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ModCommonEvents
{
    
    @SubscribeEvent
	public static void onServerWorldTick(WorldTickEvent event)
    {
		if (event.phase == Phase.START || event.side == LogicalSide.CLIENT)
			return;
		Level world = event.world;
		TweakedLinkedControllerServerHandler.tick(world);
	}
}
