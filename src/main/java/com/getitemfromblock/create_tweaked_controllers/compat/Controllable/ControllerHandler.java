package com.getitemfromblock.create_tweaked_controllers.compat.Controllable;

import java.util.Map;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

import com.mrcrayfish.controllable.event.Value;
import com.mrcrayfish.controllable.event.ControllerEvents;
import com.mrcrayfish.controllable.client.Action;
import com.mrcrayfish.controllable.client.binding.ButtonBinding;
import com.mrcrayfish.controllable.client.binding.ButtonBindings;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ControllerHandler
{
    /*
    @SubscribeEvent
    public void onButtonInput(ControllerEvents.Button event)
    {
        if(event.getState() && event.getButton() != ButtonBindings.USE_ITEM.getButton() && TweakedLinkedControllerClientHandler.MODE != TweakedLinkedControllerClientHandler.Mode.IDLE)
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMoveEvent(ControllerEvents.UpdateMovement event)
    {
        if(TweakedLinkedControllerClientHandler.MODE == TweakedLinkedControllerClientHandler.Mode.IDLE)
            return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onTurnEvent(ControllerEvents.UpdateCamera event)
    {
        if(TweakedLinkedControllerClientHandler.MODE == TweakedLinkedControllerClientHandler.Mode.IDLE)
            return;
            
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onAvailableActions(ControllerEvents.GatherActions event)
    {
        if(TweakedLinkedControllerClientHandler.MODE == TweakedLinkedControllerClientHandler.Mode.IDLE)
            return;

        Map<ButtonBinding, Action> actionMap = event.getActions();
        actionMap.remove(ButtonBindings.ATTACK);
        actionMap.remove(ButtonBindings.OPEN_INVENTORY);
        actionMap.put(ButtonBindings.USE_ITEM, new Action(CreateTweakedControllers.translateDirect("keybind.controller_exit"), Action.Side.LEFT));
    }
    */
}
