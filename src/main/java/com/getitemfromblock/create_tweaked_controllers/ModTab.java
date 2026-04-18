package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.item.ModItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = CreateTweakedControllers.ID)
public class ModTab
{
    private static final DeferredRegister<CreativeModeTab> REGISTER =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateTweakedControllers.ID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB = REGISTER.register("base",
    () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + CreateTweakedControllers.ID + ".base"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(ModItems.TWEAKED_LINKED_CONTROLLER::asStack)
        .displayItems((displayParams, output) ->
        {
            output.accept(ModItems.TWEAKED_LINKED_CONTROLLER.get());
        })
        .build());

    public static void register(IEventBus modEventBus)
    {
        REGISTER.register(modEventBus);
    }
}