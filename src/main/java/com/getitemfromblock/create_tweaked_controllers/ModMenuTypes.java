package com.getitemfromblock.create_extended_controllers;

import com.getitemfromblock.create_extended_controllers.controller.extended.ExtendedLinkedControllerMenu;
import com.getitemfromblock.create_extended_controllers.controller.extended.ExtendedLinkedControllerScreen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.MenuBuilder.ForgeMenuFactory;
import com.tterrag.registrate.builders.MenuBuilder.ScreenFactory;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ModMenuTypes
{
    private static final
	CreateRegistrate REGISTRATE = CreateExtendedControllers.registrate();

    public static final MenuEntry<ExtendedLinkedControllerMenu> EXTENDED_LINKED_CONTROLLER =
		register("extended_linked_controller", ExtendedLinkedControllerMenu::new, () -> ExtendedLinkedControllerScreen::new);

        private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
		String name, ForgeMenuFactory<C> factory, NonNullSupplier<ScreenFactory<C, S>> screenFactory) {
		return REGISTRATE
			.menu(name, factory, screenFactory)
			.register();
	}

	public static void register() {}
}
