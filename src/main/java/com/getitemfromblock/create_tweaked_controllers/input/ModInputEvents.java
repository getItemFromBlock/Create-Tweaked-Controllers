package com.getitemfromblock.create_tweaked_controllers.input;

import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class ModInputEvents
{
    @SubscribeEvent
    public static void onClickInput(InputEvent.InteractionKeyMappingTriggered event)
    {
        TweakedLinkedControllerClientHandler.deactivateInLectern();
    }
}
