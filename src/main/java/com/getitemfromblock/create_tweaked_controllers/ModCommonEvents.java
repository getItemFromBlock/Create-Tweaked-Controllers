package com.getitemfromblock.create_tweaked_controllers;

import javax.annotation.Nonnull;

import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerServerHandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber
public class ModCommonEvents
{
    
    @SubscribeEvent
    public static void onServerWorldTick(LevelTickEvent.Post event)
    {
        Level world = event.getLevel();
        if (world.isClientSide())
            return;
        TweakedLinkedControllerServerHandler.tick(world);
    }

    // Extra check in case of a crash when the player is using a lectern controller
    @SubscribeEvent
    public static void onEntityJoinWorld(@Nonnull EntityJoinLevelEvent event)
    {
        if(event.getEntity() != null && event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();
            if (player.getPersistentData().contains("IsUsingLecternController"))
            {
                player.getPersistentData().remove("IsUsingLecternController");
            }
        }
    }
}
