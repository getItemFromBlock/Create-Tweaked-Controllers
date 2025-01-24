package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.compat.Controllable.ControllerHandler;
import com.getitemfromblock.create_tweaked_controllers.config.ModKeyMappings;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;
import com.getitemfromblock.create_tweaked_controllers.input.MouseCursorHandler;

import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModClientStuff
{
    private static boolean controllableLoaded = false;

    public static boolean isControllableLoaded()
    {
        return controllableLoaded;
    }
    
    public static void onConstructor(IEventBus modEventBus, IEventBus forgeEventBus)
    {
        modEventBus.addListener(ModClientStuff::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event)
    {
        if(ModList.get().isLoaded("controllable"))
        {
            controllableLoaded = true;
            MinecraftForge.EVENT_BUS.register(new ControllerHandler());
        }
        MouseCursorHandler.InitValues();
        ModKeyMappings.init();
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create: Tweaked Controller's Tweaked Linked Controller", TweakedLinkedControllerClientHandler.OVERLAY);
    }
}
