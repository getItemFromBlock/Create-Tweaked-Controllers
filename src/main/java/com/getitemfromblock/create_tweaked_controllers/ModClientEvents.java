package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.gui.ModConfigScreen;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;

@EventBusSubscriber(Dist.CLIENT)
public class ModClientEvents
{
    @SubscribeEvent(priority = EventPriority.HIGHEST) // We need to catch the inputs as early as possible to cancel them
	public static void onTick(ClientTickEvent event)
    {
		if (!isGameActive())
			return;

		//Level world = Minecraft.getInstance().level;
		if (event.phase == Phase.START || Minecraft.getInstance().screen != null)
        {
			TweakedLinkedControllerClientHandler.tick();
			return;
		}
    }

	protected static boolean isGameActive()
    {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}

	@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static class ModBusEvents
	{
		@SubscribeEvent
		public static void onLoadComplete(FMLLoadCompleteEvent event)
		{
			ModContainer container = ModList.get()
				.getModContainerById(CreateTweakedControllers.ID)
				.orElseThrow(() -> new IllegalStateException("CreateTweakedControllers mod container missing on LoadComplete"));
			container.registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
				() -> new ConfigGuiHandler.ConfigGuiFactory(
					(mc, previousScreen) -> new ModConfigScreen(previousScreen)));
		}
	}
}
