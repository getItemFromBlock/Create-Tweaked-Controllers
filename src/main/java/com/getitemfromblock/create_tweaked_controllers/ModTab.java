package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.item.ItemDisplay;
import com.getitemfromblock.create_tweaked_controllers.item.ModItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTab
{
    private static final DeferredRegister<CreativeModeTab> TAB =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateTweakedControllers.ID);

    public static void register(IEventBus modEventBus)
    {
        TAB.register("base",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + CreateTweakedControllers.ID + ".base"))
                        .icon(() -> new ItemStack((ItemLike) ModItems.TWEAKED_LINKED_CONTROLLER))
                        .displayItems(new ItemDisplay.ItemDisplayImpl())
                        .build());
        TAB.register(modEventBus);
    }
}