package com.getitemfromblock.create_extended_controllers;

import com.getitemfromblock.create_extended_controllers.controller.extended.ExtendedLinkedControllerClientHandler;

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
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create Extended Controller's Extended Linked Controller", ExtendedLinkedControllerClientHandler.OVERLAY);
    }
}
