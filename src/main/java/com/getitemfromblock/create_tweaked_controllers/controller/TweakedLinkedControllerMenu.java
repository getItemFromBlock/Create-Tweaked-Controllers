package com.getitemfromblock.create_tweaked_controllers.controller;

import com.getitemfromblock.create_tweaked_controllers.gui.ControllerItemSlot;
import com.getitemfromblock.create_tweaked_controllers.gui.ModMenuTypes;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import com.simibubi.create.foundation.gui.menu.GhostItemMenu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TweakedLinkedControllerMenu extends GhostItemMenu<ItemStack>
{
    private boolean isSecondPage = false;

    public TweakedLinkedControllerMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData)
    {
        super(type, id, inv, extraData);
    }

    public TweakedLinkedControllerMenu(MenuType<?> type, int id, Inventory inv, ItemStack filterItem)
    {
        super(type, id, inv, filterItem);
    }

    public static TweakedLinkedControllerMenu create(int id, Inventory inv, ItemStack filterItem)
    {
        return new TweakedLinkedControllerMenu(ModMenuTypes.TWEAKED_LINKED_CONTROLLER.get(), id, inv, filterItem);
    }

    public void SetPage(boolean second)
    {
        isSecondPage = second;
        int slotIndex = this.slots.size() - 50;
        for (int r = 0; r < 2; r++)
        {
            boolean isVisible = (isSecondPage && r == 1) || (!isSecondPage && r == 0);
            for (int index = 0; index < guiItemSlots[r].length; index += 2)
            {
                for (int row = 0; row < 2; ++row)
                {
                    ControllerItemSlot t = (ControllerItemSlot)(this.slots.get(slotIndex));
                    t.SetActive(isVisible);
                    slotIndex++;
                }
            }
        }
        //broadcastChanges();
    }

    @Override
    protected ItemStack createOnClient(FriendlyByteBuf extraData)
    {
        return extraData.readItem();
    }

    @Override
    protected ItemStackHandler createGhostInventory()
    {
        return TweakedLinkedControllerItem.getFrequencyItems(contentHolder);
    }

    protected static final int[][] guiItemSlots =
    {
        {
            36, 34,
            84, 34,
            60, 34,
            12, 34,
            167, 97,
            191, 97,
            131, 34,
            155, 34,
            179, 34,
            119, 97,
            143, 97,
            12, 97,
            84, 97,
            36, 97,
            60, 97
        },
        {
            48, 34,
            72, 34,
            96, 34,
            120, 34,
            48, 97,
            72, 97,
            96, 97,
            120, 97,
            191, 34,
            191, 97
        }
    };

    @Override
    protected void addSlots()
    {
        addPlayerSlots(32, 194);
        
        int slot = 0;
        for (int r = 0; r < 2; r++)
        {
            boolean isVisible = (isSecondPage && r == 1) || (!isSecondPage && r == 0);
            for (int index = 0; index < guiItemSlots[r].length; index += 2)
            {
                int x = guiItemSlots[r][index];
                int y = guiItemSlots[r][index + 1];
                for (int row = 0; row < 2; ++row)
                    addSlot(new ControllerItemSlot(ghostInventory, slot++, x, y + row * 18, isVisible));
            }
        }
    }

    @Override
    protected void saveData(ItemStack contentHolder)
    {
        contentHolder.getOrCreateTag()
            .put("Items", ghostInventory.serializeNBT());
    }

    @Override
    protected boolean allowRepeats()
    {
        return true;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player)
    {
        if (slotId == playerInventory.selected && clickTypeIn != ClickType.THROW)
            return;
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean stillValid(Player playerIn)
    {
        return playerInventory.getSelected() == contentHolder;
    }
}
