package com.getitemfromblock.create_tweaked_controllers.item;

import java.util.function.UnaryOperator;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentType.Builder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents
{
    private static final DeferredRegister.DataComponents DATA_COMPONENTS =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CreateTweakedControllers.ID);

    public static final DataComponentType<ItemContainerContents> TWEAKED_CONTROLLER_ITEMS = register(
        "tweaked_controller_items",
        builder -> builder
            .persistent(ItemContainerContents.CODEC)
            .networkSynchronized(ItemContainerContents.STREAM_CODEC));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<Builder<T>> builder)
    {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    public static void register(IEventBus modEventBus)
    {
        DATA_COMPONENTS.register(modEventBus);
    }
}
