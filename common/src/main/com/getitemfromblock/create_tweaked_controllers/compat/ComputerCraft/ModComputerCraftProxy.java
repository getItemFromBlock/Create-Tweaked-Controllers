package com.getitemfromblock.create_tweaked_controllers.compat.ComputerCraft;

import java.util.function.Function;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.FallbackComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import net.minecraftforge.fml.ModList;

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
        /* Comment if computercraft.implementation is not in the source set */
        computerFactory = ModComputerBehavior::new;
    }

    public static AbstractComputerBehaviour behaviour(SmartBlockEntity sbe)
    {
		if (computerFactory == null)
			return fallbackFactory.apply(sbe);
		return computerFactory.apply(sbe);
	}
}