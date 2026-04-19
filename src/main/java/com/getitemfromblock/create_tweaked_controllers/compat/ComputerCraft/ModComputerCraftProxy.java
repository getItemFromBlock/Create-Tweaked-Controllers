package com.getitemfromblock.create_tweaked_controllers.compat.ComputerCraft;

import java.util.function.Function;

import com.getitemfromblock.create_tweaked_controllers.ModBlockEntityTypes;
import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.FallbackComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ModComputerCraftProxy
{
    private static Function<SmartBlockEntity, ? extends AbstractComputerBehaviour> fallbackFactory;
    private static Function<SmartBlockEntity, ? extends AbstractComputerBehaviour> computerFactory;

    public static void register()
    {
        fallbackFactory = FallbackComputerBehaviour::new;
        if (ModList.get().isLoaded("computercraft"))
        {
            registerWithDependency();
        }
    }

    private static void registerWithDependency()
    {
        computerFactory = ModComputerBehavior::new;
    }

    public static AbstractComputerBehaviour behaviour(SmartBlockEntity sbe)
    {
        if (computerFactory == null)
            return fallbackFactory.apply(sbe);
        return computerFactory.apply(sbe);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        if (!ModList.get().isLoaded("computercraft"))
            return;
        event.registerBlockEntity(
            PeripheralCapability.get(),
            ModBlockEntityTypes.TWEAKED_LECTERN_CONTROLLER.get(),
            (be, ctx) -> be.computerBehaviour.getPeripheralCapability());
    }
}
