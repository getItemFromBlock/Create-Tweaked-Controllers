package com.getitemfromblock.create_tweaked_controllers.input;

import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

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
        TweakedLinkedControllerClientHandler.deactivateInLectern();
    }
}
