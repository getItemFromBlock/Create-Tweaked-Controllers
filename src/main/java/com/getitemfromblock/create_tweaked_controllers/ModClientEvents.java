package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.gui.ModConfigScreen;
import com.getitemfromblock.create_tweaked_controllers.input.MouseCursorHandler;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class ModClientEvents
{
    @SubscribeEvent(priority = EventPriority.HIGHEST) // We need to catch the inputs as early as possible to cancel them
    public static void onTick(ClientTickEvent event)
    {
        if (!isGameActive())
            return;

        //Level world = Minecraft.getInstance().level;
        if (event.phase == Phase.START || Minecraft.getInstance().screen != null)
        {
            TweakedLinkedControllerClientHandler.tick();
            return;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderTick(RenderTickEvent event) // Cancel player/camera rotation just before rendering
    {
        if (!isGameActive())
            return;
        MouseCursorHandler.CancelPlayerTurn();
    }

    protected static boolean isGameActive()
    {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents
    {
        @SubscribeEvent
        public static void onLoadComplete(FMLLoadCompleteEvent event)
        {
            ModContainer container = ModList.get()
                .getModContainerById(CreateTweakedControllers.ID)
                .orElseThrow(() -> new IllegalStateException("CreateTweakedControllers mod container missing on LoadComplete"));
            container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (mc, previousScreen) -> new ModConfigScreen(previousScreen)));
        }
    }
}
