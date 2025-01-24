package com.getitemfromblock.create_tweaked_controllers.compat.ComputerCraft;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.world.entity.player.Player;

public class TweakedLecternPeripheral extends ModSyncedPeripheral<TweakedLecternControllerBlockEntity>
{
    public TweakedLecternPeripheral(TweakedLecternControllerBlockEntity be)
    {
        super(be);
        be.AssignPeripheral(this);
    }

    @NotNull
    @LuaFunction
    public final boolean hasUser()
    {
        return blockEntity.hasUser();
    }

    @LuaFunction
    public final String getUserUUID()
    {
        UUID result = blockEntity.getUserUUID();
        return result == null ? null : result.toString();
    }

    @LuaFunction
    public final boolean getButton(int buttonIndex) throws LuaException
    {
        buttonIndex--;
        if (buttonIndex < 0 || buttonIndex > 14) throw new LuaException("Index out of range : [1,15]");
        return blockEntity.GetButton(buttonIndex);
    }

    @LuaFunction
    public final float getAxis(int axisIndex) throws LuaException
    {
        axisIndex--;
        if (axisIndex < 0 || axisIndex > 5) throw new LuaException("Index out of range : [1,6]");
        return blockEntity.GetAxis(axisIndex);
    }

    @LuaFunction
    public final void setFullPrecision(boolean value)
    {
        blockEntity.SetFullPrecision(value);
    }

    @NotNull
    @LuaFunction
    public final boolean isFullPrecision()
    {
        return blockEntity.shouldUseFullPrecision();
    }

    public void NotifyUseEvent(boolean use, Player player)
    {
        for (IComputerAccess computer : computers)
        {
            Object arg = player == null ? null : player.getUUID().toString();
            if (use)
            {
                computer.queueEvent("controller_start_using", arg);
            }
            else
            {
                computer.queueEvent("controller_stop_using", arg);
            }
        }
    }
    
    @NotNull
    @Override
    public String getType()
    {
        return "tweaked_controller";
    }
}