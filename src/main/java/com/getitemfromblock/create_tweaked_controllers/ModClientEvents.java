package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.api.distmarker.Dist;

@EventBusSubscriber(Dist.CLIENT)
public class ModClientEvents
{
    @SubscribeEvent
	public static void onTick(ClientTickEvent event)
    {
		if (!isGameActive())
			return;

		//Level world = Minecraft.getInstance().level;
		if (event.phase == Phase.START)
        {
			TweakedLinkedControllerClientHandler.tick();
			return;
		}
    }

    protected static boolean isGameActive()
    {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}
}
