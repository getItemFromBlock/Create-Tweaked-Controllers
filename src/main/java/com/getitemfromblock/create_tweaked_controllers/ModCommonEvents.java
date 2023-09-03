package com.getitemfromblock.create_tweaked_controllers;

import javax.annotation.Nonnull;

import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerServerHandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ModCommonEvents
{
    
    @SubscribeEvent
    public static void onServerWorldTick(LevelTickEvent event)
    {
        if (event.phase == Phase.START || event.side == LogicalSide.CLIENT)
            return;
        Level world = event.level;
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
