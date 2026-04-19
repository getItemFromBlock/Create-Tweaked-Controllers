package com.getitemfromblock.create_tweaked_controllers.config;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;


public class ModConfigs
{
    public static void register(ModContainer container)
    {
        container.registerConfig(ModConfig.Type.CLIENT, ModClientConfig.SPEC, CreateTweakedControllers.ID.replace("_", "") + "-client.toml");
    }
}
