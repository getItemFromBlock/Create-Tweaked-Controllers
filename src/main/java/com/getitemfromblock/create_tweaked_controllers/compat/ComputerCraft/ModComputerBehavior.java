package com.getitemfromblock.create_tweaked_controllers.compat.ComputerCraft;

import java.util.function.Supplier;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import dan200.computercraft.api.peripheral.IPeripheral;

public class ModComputerBehavior extends AbstractComputerBehaviour
{
    private IPeripheral peripheral;
    private final Supplier<IPeripheral> peripheralSupplier;
    private final SmartBlockEntity be;

    public ModComputerBehavior(SmartBlockEntity te)
    {
        super(te);
        this.peripheralSupplier = getPeripheralFor(te);
        this.be = te;
    }

    public static Supplier<IPeripheral> getPeripheralFor(SmartBlockEntity be)
    {
        if (be instanceof TweakedLecternControllerBlockEntity tlcbe)
            return () -> new TweakedLecternPeripheral(tlcbe);

        throw new IllegalArgumentException("No peripheral available for " + be.getType()
            .getClass().getName());
    }

    @Override
    public IPeripheral getPeripheralCapability()
    {
        if (peripheral == null)
            peripheral = peripheralSupplier.get();
        return peripheral;
    }

    @Override
    public void removePeripheral()
    {
        if (peripheral != null && getWorld() != null)
            getWorld().invalidateCapabilities(be.getBlockPos());
    }
}
