package com.getitemfromblock.create_extended_controllers;

import com.getitemfromblock.create_extended_controllers.controller.extended.ExtendedLinkedControllerItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import static com.simibubi.create.content.AllSections.CURIOSITIES;

public class ModItems
{
    private static final
	CreateRegistrate REGISTRATE = CreateExtendedControllers.registrate().creativeModeTab(() -> ModTab.MOD_TAB);

	static {
		REGISTRATE.startSection(CURIOSITIES);
	}

    public static final ItemEntry<ExtendedLinkedControllerItem> EXTENDED_LINKED_CONTROLLER =
		REGISTRATE.item("extended_linked_controller", ExtendedLinkedControllerItem::new)
			.properties(p -> p.stacksTo(1))
			.model(AssetLookup.itemModelWithPartials())
			.register();

    public static void register() {}
}