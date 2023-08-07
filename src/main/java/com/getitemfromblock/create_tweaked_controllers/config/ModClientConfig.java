package com.getitemfromblock.create_tweaked_controllers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> USE_CUSTOM_MAPPINGS;
    //public static final ForgeConfigSpec.ConfigValue<Integer> CITRINE_ORE_VEIN_SIZE;

    static {
        BUILDER.push("Configs for Create: Tweaked Controllers - You may want to use the in-game menu instead");

        USE_CUSTOM_MAPPINGS = BUILDER.comment("Wether or not to use custom axis/button mappings")
                .define("use_custom_mappings", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
