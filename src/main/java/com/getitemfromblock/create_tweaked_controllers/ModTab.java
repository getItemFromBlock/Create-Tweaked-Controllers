package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.item.ModItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTab
{
    private static final DeferredRegister<CreativeModeTab> REGISTER =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateTweakedControllers.ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = REGISTER.register("base",
    () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + CreateTweakedControllers.ID + ".base"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(ModItems.TWEAKED_LINKED_CONTROLLER::asStack)
        .build());

    public static void register(IEventBus modEventBus)
    {
        REGISTER.register(modEventBus);
    }
}