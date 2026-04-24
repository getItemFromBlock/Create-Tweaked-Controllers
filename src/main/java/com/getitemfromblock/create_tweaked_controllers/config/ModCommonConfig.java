package com.getitemfromblock.create_tweaked_controllers.config;

import net.neoforged.neoforge.common.ModConfigSpec;


public class ModCommonConfig
{
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> DISABLE_FLICKER_LIMIT;

    static {
        BUILDER.push("Configs for Create: Tweaked Controllers");

        DISABLE_FLICKER_LIMIT = BUILDER.comment("If true, this prevents kinetic blocks from breaking when being updated too fast, default is true")
                .define("disable_flicker_limit", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}