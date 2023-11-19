package com.getitemfromblock.create_tweaked_controllers.item;

import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.ModTab;

public class ModItems
{

    static
    {
        CreateTweakedControllers.registrate().setCreativeTab(ModTab.MOD_TAB);
    }

    public static final ItemEntry<TweakedLinkedControllerItem> TWEAKED_LINKED_CONTROLLER =
        CreateTweakedControllers.registrate().item("tweaked_linked_controller", TweakedLinkedControllerItem::new)
            .properties(p -> p.stacksTo(1))
            .model(AssetLookup.itemModelWithPartials())
            .register();

    public static void register() {}
}