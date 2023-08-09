package com.getitemfromblock.create_tweaked_controllers.config;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class ModKeyMappings
{
    public static KeyMapping KEY_MOUSE_FOCUS;
    public static KeyMapping KEY_MOUSE_RESET;

    public static void init()
    {
        KEY_MOUSE_FOCUS = registerKey("mouse_focus", KeyMapping.CATEGORY_GAMEPLAY, InputConstants.KEY_F);
        KEY_MOUSE_RESET = registerKey("mouse_reset", KeyMapping.CATEGORY_GAMEPLAY, InputConstants.KEY_R);
    }

    private static KeyMapping registerKey(String name, String category, int keycode)
    {
        final var key = new KeyMapping(CreateTweakedControllers.ID + ".keyinfo." + name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
