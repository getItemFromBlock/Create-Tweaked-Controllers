package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.config.ModKeyMappings;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModClientStuff
{
    public static void onConstructor(IEventBus modEventBus, IEventBus forgeEventBus)
    {
        modEventBus.addListener(ModClientStuff::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event)
    {
        ModKeyMappings.init();
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create: Tweaked Controller's Tweaked Linked Controller", TweakedLinkedControllerClientHandler.OVERLAY);
    }
}
