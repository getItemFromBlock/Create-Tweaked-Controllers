package com.getitemfromblock.create_extended_controllers.controller.extended;

import com.getitemfromblock.create_extended_controllers.ModMenuTypes;
import com.simibubi.create.foundation.gui.container.GhostItemContainer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ExtendedLinkedControllerMenu extends GhostItemContainer<ItemStack>
{

	public ExtendedLinkedControllerMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData)
	{
		super(type, id, inv, extraData);
	}

	public ExtendedLinkedControllerMenu(MenuType<?> type, int id, Inventory inv, ItemStack filterItem)
	{
		super(type, id, inv, filterItem);
	}

	public static ExtendedLinkedControllerMenu create(int id, Inventory inv, ItemStack filterItem)
	{
		return new ExtendedLinkedControllerMenu(ModMenuTypes.EXTENDED_LINKED_CONTROLLER.get(), id, inv, filterItem);
	}

	@Override
	protected ItemStack createOnClient(FriendlyByteBuf extraData)
	{
		return extraData.readItem();
	}

	@Override
	protected ItemStackHandler createGhostInventory()
	{
		return ExtendedLinkedControllerItem.getFrequencyItems(contentHolder);
	}

	protected static final int[] guiItemSlots =
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
	};

	@Override
	protected void addSlots()
	{
		addPlayerSlots(32, 194);
		
		int slot = 0;

		for (int index = 0; index < guiItemSlots.length; index += 2)
		{
			int x = guiItemSlots[index];
			int y = guiItemSlots[index + 1];
			for (int row = 0; row < 2; ++row)
				addSlot(new SlotItemHandler(ghostInventory, slot++, x, y + row * 18));
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
