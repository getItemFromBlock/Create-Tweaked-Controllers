package com.getitemfromblock.create_extended_controllers;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModTab
{
	public static final CreativeModeTab MOD_TAB = new CreativeModeTab(CreateExtendedControllers.ID + ".base")
    {
		@Override
		public ItemStack makeIcon()
        {
            return ModItems.EXTENDED_LINKED_CONTROLLER.asStack();
        }
	};
	private static final CreateRegistrate REGISTRATE = CreateExtendedControllers.registrate();

	static {
		REGISTRATE.creativeModeTab(() -> MOD_TAB);
	}

	public static void register() {}
}