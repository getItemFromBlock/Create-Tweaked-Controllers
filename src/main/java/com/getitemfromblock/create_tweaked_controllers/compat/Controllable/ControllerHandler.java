package com.getitemfromblock.create_tweaked_controllers.compat.Controllable;

import java.util.Map;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

import com.mrcrayfish.controllable.event.Value;
import com.mrcrayfish.controllable.event.ControllerEvents.GatherActions;
import com.mrcrayfish.controllable.event.ControllerEvents.Input;
import com.mrcrayfish.controllable.event.ControllerEvents.UpdateCamera;
import com.mrcrayfish.controllable.event.ControllerEvents.UpdateMovement;
import com.mrcrayfish.controllable.event.ControllerEvents;
import com.mrcrayfish.controllable.client.Action;
import com.mrcrayfish.controllable.client.ActionVisibility;
import com.mrcrayfish.controllable.client.binding.ButtonBinding;
import com.mrcrayfish.controllable.client.binding.ButtonBindings;
import com.mrcrayfish.controllable.client.input.Controller;

public class ControllerHandler
{
    private static Input ButtonEvent = new Input()
    {
        @Override
        public boolean handle(Controller arg0, Value<Integer> arg1, int arg2, boolean arg3)
        {
            return (TweakedLinkedControllerClientHandler.MODE != TweakedLinkedControllerClientHandler.Mode.IDLE && arg2 != ButtonBindings.USE_ITEM.getButton());
        }
    };

    private static UpdateMovement MoveEvent = new UpdateMovement()
    {
        @Override
        public boolean handle()
        {
            return (TweakedLinkedControllerClientHandler.MODE != TweakedLinkedControllerClientHandler.Mode.IDLE);
        }
    };

    private static UpdateCamera CameraEvent = new UpdateCamera()
    {
        @Override
        public boolean handle(Value<Float> arg0, Value<Float> arg1)
        {
            return (TweakedLinkedControllerClientHandler.MODE != TweakedLinkedControllerClientHandler.Mode.IDLE);
        }
    };

    private static GatherActions GatherEvent = new GatherActions()
    {
        @Override
        public void handle(Map<ButtonBinding, Action> actionMap, ActionVisibility arg1)
        {
            if(TweakedLinkedControllerClientHandler.MODE == TweakedLinkedControllerClientHandler.Mode.IDLE || arg1 == ActionVisibility.NONE)
                return;
            actionMap.clear();
            actionMap.put(ButtonBindings.USE_ITEM, new Action(CreateTweakedControllers.translateDirect("keybind.controller_exit"), Action.Side.LEFT));
        }
    };

    public static void Register()
    {
        ControllerEvents.INPUT.register(ButtonEvent);
        ControllerEvents.UPDATE_MOVEMENT.register(MoveEvent);
        ControllerEvents.UPDATE_CAMERA.register(CameraEvent);
        ControllerEvents.GATHER_ACTIONS.register(GatherEvent);
    }
    
}
