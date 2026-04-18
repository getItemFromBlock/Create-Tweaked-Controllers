package com.getitemfromblock.create_tweaked_controllers.input;

import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;


@EventBusSubscriber(Dist.CLIENT)
public class ModInputEvents
{
    @SubscribeEvent
    public static void onClickInput(InputEvent.InteractionKeyMappingTriggered event)
    {
        TweakedLinkedControllerClientHandler.deactivateInLectern();
    }
}
