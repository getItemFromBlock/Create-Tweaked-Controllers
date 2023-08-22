package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.item.ModItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModTab
{
    public static final CreativeModeTab MOD_TAB = new CreativeModeTab(CreateTweakedControllers.ID + ".base")
    {
        @Override
        public ItemStack makeIcon()
        {
            return ModItems.TWEAKED_LINKED_CONTROLLER.asStack();
        }
    };
    private static final CreateRegistrate REGISTRATE = CreateTweakedControllers.registrate();

    static {
        REGISTRATE.creativeModeTab(() -> MOD_TAB, "Create: Tweaked Controllers");
    }

    public static void register() {}
}