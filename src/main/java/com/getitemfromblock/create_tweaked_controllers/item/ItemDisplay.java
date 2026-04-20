package com.getitemfromblock.create_tweaked_controllers.item;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;

public class ItemDisplay
{
    public static class ItemDisplayImpl implements CreativeModeTab.DisplayItemsGenerator
    {
        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output)
        {
            output.accept(ModItems.TWEAKED_LINKED_CONTROLLER, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}