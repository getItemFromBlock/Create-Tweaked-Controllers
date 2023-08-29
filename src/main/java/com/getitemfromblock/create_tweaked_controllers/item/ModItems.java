package com.getitemfromblock.create_tweaked_controllers.item;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.ModTab;

public class ModItems
{
    private static final
    CreateRegistrate REGISTRATE = CreateTweakedControllers.registrate().creativeModeTab(() -> ModTab.MOD_TAB);

    public static final ItemEntry<TweakedLinkedControllerItem> TWEAKED_LINKED_CONTROLLER =
        REGISTRATE.item("tweaked_linked_controller", TweakedLinkedControllerItem::new)
            .properties(p -> p.stacksTo(1))
            .model(AssetLookup.itemModelWithPartials())
            .register();

    public static void register() {}
}