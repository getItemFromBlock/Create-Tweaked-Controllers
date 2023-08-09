package com.getitemfromblock.create_tweaked_controllers.input;

import com.mojang.blaze3d.Blaze3D;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;

public class MouseCursorHandler
{
    public static Vec2 delta = null;
    private static MouseHandler m = null;
    private static double lastMouseEventTime;
    private static float deltaT = 0;
    private static Vec2 a, b;

    public static void ResetCenter()
    {
        CheckValues();
        delta = new Vec2(0, 0);
    }

    private static void CheckValues()
    {
        if (delta == null)
        {
            delta = new Vec2(0, 0);
            m = Minecraft.getInstance().mouseHandler;
            lastMouseEventTime = Blaze3D.getTime();
        }
    }

    public static void Update()
    {
        double d0 = Blaze3D.getTime();
        deltaT = (float)(d0 - lastMouseEventTime);
        lastMouseEventTime = d0;
        delta = new Vec2((float)m.getXVelocity() + delta.x, (float)m.getYVelocity() + delta.y);
    }

    public static float GetX(boolean useVelocity)
    {
        if (useVelocity)
        {
            CheckValues();
            return (float)m.getXVelocity() / deltaT;
        }
        else
        {
            return delta.x;
        }
    }

    public static float GetY(boolean useVelocity)
    {
        if (useVelocity)
        {
            CheckValues();
            return (float)m.getYVelocity() / deltaT;
        }
        else
        {
            return delta.y;
        }
    }

    public static void RestorePlayerRotation()
    {
        Player p = Minecraft.getInstance().player;
        p.setXRot(a.x);
        p.setYRot(a.y);
        p.xRotO = b.x;
        p.yRotO = b.y;
    }

    public static void StorePlayerRotations()
    {
        Player p = Minecraft.getInstance().player;
        a = new Vec2(p.getXRot(), p.getYRot());
        b = new Vec2(p.xRotO, p.yRotO);
    }
}
