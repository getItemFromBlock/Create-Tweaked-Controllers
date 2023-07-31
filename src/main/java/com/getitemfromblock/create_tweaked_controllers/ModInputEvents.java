package com.getitemfromblock.create_extended_controllers;

import com.getitemfromblock.create_extended_controllers.controller.extended.ExtendedLinkedControllerClientHandler;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.ClickInputEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ModInputEvents
{
    @SubscribeEvent
	public static void onClickInput(ClickInputEvent event)
    {
        ExtendedLinkedControllerClientHandler.deactivateInLectern();
    }
}
