package com.getitemfromblock.create_tweaked_controllers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> USE_CUSTOM_MAPPINGS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TOGGLE_MOUSE_FOCUS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> AUTO_RESET_MOUSE_FOCUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> CONFIG_BUTTON_MAIN_MENU_ROW;
    public static final ForgeConfigSpec.ConfigValue<Integer> CONFIG_BUTTON_MAIN_MENU_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Integer> CONFIG_BUTTON_INGAME_MENU_ROW;
    public static final ForgeConfigSpec.ConfigValue<Integer> CONFIG_BUTTON_INGAME_MENU_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<ControllerLayoutType> CONTROLLER_LAYOUT_TYPE;

    static {
        BUILDER.push("Configs for Create: Tweaked Controllers");

        USE_CUSTOM_MAPPINGS = BUILDER.comment("Wether or not to use custom axis/button mappings, default is false")
            .define("use_custom_mappings", false);
        TOGGLE_MOUSE_FOCUS = BUILDER.comment("Does the mouse cursor focus key acts as toggle instead of hold, default is false")
            .define("toggle_mouse_focus", false);
        AUTO_RESET_MOUSE_FOCUS = BUILDER.comment("Does the mouse cursor inputs are automatically reset when the controller item is put down, default is true")
            .define("auto_reset_mouse_focus", true);
        CONFIG_BUTTON_MAIN_MENU_ROW = BUILDER.comment("Row of the Controller Settings button in the main menu, default is 2")
            .defineInRange("config_button_main_menu_row", 2 , 0, 4);
        CONFIG_BUTTON_MAIN_MENU_OFFSET = BUILDER.comment("X Offset of the Controller Settings button in the main menu, default is 4")
            .defineInRange("config_button_main_menu_offset", 4, Integer.MIN_VALUE, Integer.MAX_VALUE);
        CONFIG_BUTTON_INGAME_MENU_ROW = BUILDER.comment("Row of the Controller Settings button in the ingame pause menu, default is 3")
            .defineInRange("config_button_ingame_menu_row", 3 , 0, 5);
        CONFIG_BUTTON_INGAME_MENU_OFFSET = BUILDER.comment("X Offset of the Controller Settings button in the ingame pause menu, default is 4")
            .defineInRange("config_button_main_ingame_offset", 4, Integer.MIN_VALUE, Integer.MAX_VALUE);
        CONTROLLER_LAYOUT_TYPE = BUILDER.comment("What is the layout used for the display controller in the config menus, default is XBOX")
            .defineEnum("controller_layout_type", ControllerLayoutType.XBOX, ControllerLayoutType.values());

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public enum ControllerLayoutType
    {
        XBOX,
        NINTENDO,
        PLAYSTATION;
    }
}
