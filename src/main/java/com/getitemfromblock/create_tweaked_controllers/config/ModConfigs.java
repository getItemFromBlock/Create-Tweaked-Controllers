package com.getitemfromblock.create_tweaked_controllers.config;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ModConfigs
{
    public static void register(ModLoadingContext context)
    {
        context.registerConfig(ModConfig.Type.CLIENT, ModClientConfig.SPEC, CreateTweakedControllers.ID.replaceAll("_", "") + "-client.toml");
        //ModClientConfig.USE_CUSTOM_MAPPINGS.set(true);
        //ModClientConfig.SPEC.save();

    }
}
