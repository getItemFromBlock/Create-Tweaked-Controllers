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
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = CreateTweakedControllers.ID, value = Dist.CLIENT)
public class ModClientEvents
{
    @SubscribeEvent(priority = EventPriority.HIGHEST) // We need to catch the inputs as early as possible to cancel them
    public static void onTick(ClientTickEvent.Pre event)
    {
        if (!isGameActive())
            return;
        TweakedLinkedControllerClientHandler.tick();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event)
    {

        MouseCursorHandler.AddScrollDelta(event.getScrollDeltaY());
        if (MouseCursorHandler.ShouldCancelScroll())
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onScreenMouseScrollPre(ScreenEvent.MouseScrolled.Pre event)
    {
        MouseCursorHandler.AddScrollDelta(event.getScrollDeltaY());
        if (MouseCursorHandler.ShouldCancelScroll())
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderTick(RenderFrameEvent.Pre event) // Cancel player/camera rotation just before rendering
    {
        if (!isGameActive())
            return;
        MouseCursorHandler.CancelPlayerTurn();
    }

    protected static boolean isGameActive()
    {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

    @EventBusSubscriber(modid = CreateTweakedControllers.ID, value = Dist.CLIENT)
    public static class ModBusEvents
    {
        @SubscribeEvent
        public static void onLoadComplete(FMLLoadCompleteEvent event)
        {
            ModContainer container = ModList.get()
                .getModContainerById(CreateTweakedControllers.ID)
                .orElseThrow(() -> new IllegalStateException("CreateTweakedControllers mod container missing on LoadComplete"));
            container.registerExtensionPoint(IConfigScreenFactory.class,
                (mc, previousScreen) -> new ModConfigScreen(previousScreen));
        }

        @SubscribeEvent
        public static void onRegisterGuiLayers(RegisterGuiLayersEvent event)
        {
            event.registerAbove(VanillaGuiLayers.HOTBAR,
                CreateTweakedControllers.asResource("controller_overlay"),
                TweakedLinkedControllerClientHandler.OVERLAY);
        }
    }
}
