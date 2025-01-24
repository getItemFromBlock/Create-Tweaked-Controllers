package com.getitemfromblock.create_tweaked_controllers.compat.ComputerCraft;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

public class ModComputerBehavior extends AbstractComputerBehaviour
{

    protected static final Capability<IPeripheral> PERIPHERAL_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    LazyOptional<IPeripheral> peripheral;
    NonNullSupplier<IPeripheral> peripheralSupplier;

    public ModComputerBehavior(SmartBlockEntity te)
    {
        super(te);
        this.peripheralSupplier = getPeripheralFor(te);
    }

    public static NonNullSupplier<IPeripheral> getPeripheralFor(SmartBlockEntity be)
    {
        if (be instanceof TweakedLecternControllerBlockEntity tlcbe)
            return () -> new TweakedLecternPeripheral(tlcbe);

        throw new IllegalArgumentException("No peripheral available for " + be.getType()
            .getRegistryName());
    }

    @Override
    public <T> boolean isPeripheralCap(Capability<T> cap)
    {
        return cap == PERIPHERAL_CAPABILITY;
    }

    @Override
    public <T> LazyOptional<T> getPeripheralCapability()
    {
        if (peripheral == null || !peripheral.isPresent())
            peripheral = LazyOptional.of(peripheralSupplier);
        return peripheral.cast();
    }

    @Override
    public void removePeripheral()
    {
        if (peripheral != null)
            peripheral.invalidate();
    }
    
}
