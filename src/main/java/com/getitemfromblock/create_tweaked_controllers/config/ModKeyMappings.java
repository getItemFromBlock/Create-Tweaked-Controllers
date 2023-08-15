package com.getitemfromblock.create_tweaked_controllers.config;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class ModKeyMappings
{
    public static KeyMapping KEY_MOUSE_FOCUS;
    public static KeyMapping KEY_MOUSE_RESET;
    public static KeyMapping KEY_CONTROLLER_EXIT;

    public static void init()
    {
        KEY_MOUSE_FOCUS = registerKey("mouse_focus", InputConstants.KEY_LALT);
        KEY_MOUSE_RESET = registerKey("mouse_reset", InputConstants.KEY_R);
        KEY_CONTROLLER_EXIT = registerKey("controller_exit", InputConstants.KEY_TAB);
    }

    private static KeyMapping registerKey(String name, int keycode)
    {
        final var key = new KeyMapping(CreateTweakedControllers.ID + ".keybind." + name, keycode, CreateTweakedControllers.NAME);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
