package com.getitemfromblock.create_tweaked_controllers.compat.ComputerCraft;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.simibubi.create.AllPackets;
import com.simibubi.create.compat.computercraft.AttachedComputerPacket;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraftforge.network.PacketDistributor;

public abstract class ModSyncedPeripheral<T extends SmartBlockEntity> implements IPeripheral
{
    protected final T blockEntity;
    protected final CopyOnWriteArrayList<IComputerAccess> computers = new CopyOnWriteArrayList<IComputerAccess>();

    public ModSyncedPeripheral(T blockEntity)
    {
        this.blockEntity = blockEntity;
    }

    @Override
    public void attach(@NotNull IComputerAccess computer)
    {
        boolean found = false;
        for (var comp : computers)
        {
            if (comp.getID() == computer.getID())
            {
                found = true;
                break;
            }
        }
        if (!found)
        {
            computers.add(computer);
        }
        updateBlockEntity();
    }

    @Override
    public void detach(@NotNull IComputerAccess computer)
    {
        computers.remove(computer);
        updateBlockEntity();
    }

    private void updateBlockEntity()
    {
        boolean hasAttachedComputer = computers.size() > 0;

        blockEntity.getBehaviour(ModComputerBehavior.TYPE).setHasAttachedComputer(hasAttachedComputer);
        AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new AttachedComputerPacket(blockEntity.getBlockPos(), hasAttachedComputer));
    }

    @Override
    public boolean equals(@Nullable IPeripheral other)
    {
        return this == other;
    }

}
