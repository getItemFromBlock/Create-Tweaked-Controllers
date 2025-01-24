package com.getitemfromblock.create_tweaked_controllers.gui;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerMenu;
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
    CreateRegistrate REGISTRATE = CreateTweakedControllers.registrate();

    public static final MenuEntry<TweakedLinkedControllerMenu> TWEAKED_LINKED_CONTROLLER =
        register("tweaked_linked_controller", TweakedLinkedControllerMenu::new, () -> TweakedLinkedControllerScreen::new);

        private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
        String name, ForgeMenuFactory<C> factory, NonNullSupplier<ScreenFactory<C, S>> screenFactory) {
        return REGISTRATE
            .menu(name, factory, screenFactory)
            .register();
    }

    public static void register() {}
}
