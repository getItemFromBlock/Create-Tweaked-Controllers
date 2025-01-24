package com.getitemfromblock.create_tweaked_controllers.gui;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ControllerItemSlot extends SlotItemHandler
{
    protected boolean active = true;

    public ControllerItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    public ControllerItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean active)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.active = active;
    }

    @Override
    public boolean isActive()
    {
        return active;
    }

    public void SetActive(boolean active)
    {
        this.active = active;
    }
    
}
