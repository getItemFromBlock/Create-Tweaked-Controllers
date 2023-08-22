package com.getitemfromblock.create_tweaked_controllers.input;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.Blaze3D;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.world.phys.Vec2;

public class MouseCursorHandler
{
    public static Vec2 delta = null;
    public static Vec2 lastPos = null;
    public static Vec2 vel = null;
    private static MouseHandler m = null;
    private static Options o = null;
    private static double lastMouseEventTime;
    private static float deltaT = 0;
    private static double sensitivity = 0.5;

    public static Vec2 GetMousePos()
    {
        if (m.isMouseGrabbed())
        {
            return new Vec2((float)m.xpos(), (float)m.ypos());
        }
        else
        {
            double[] x,y;
            x = new double[1];
            y = new double[1];
            GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), x, y);
            return new Vec2((float)x[0], (float)y[0]);
        }
    }

    public static void ResetCenter()
    {
        delta = new Vec2(0, 0);
        vel = new Vec2(0, 0);
        lastPos = GetMousePos();
    }

    public static void InitValues()
    {
        delta = new Vec2(0, 0);
        vel = new Vec2(0, 0);
        m = Minecraft.getInstance().mouseHandler;
        o = Minecraft.getInstance().options;
        lastPos = GetMousePos();
        lastMouseEventTime = Blaze3D.getTime();
    }

    public static void Update()
    {
        double d0 = Blaze3D.getTime();
        deltaT = (float)(d0 - lastMouseEventTime);
        lastMouseEventTime = d0;
        Vec2 tmp = GetMousePos();
        vel = new Vec2(tmp.x - lastPos.x, tmp.y - lastPos.y);
        delta = delta.add(vel);
        vel = vel.scale(1/deltaT);
        lastPos = tmp;
    }

    public static float GetX(boolean useVelocity)
    {
        if (useVelocity)
        {
            return vel.x;
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
            return vel.y;
        }
        else
        {
            return delta.y;
        }
    }

    public static void ActivateMouseLock()
    {
        if (o.sensitivity != -1/3.0)
        {
            sensitivity = o.sensitivity;
        }
        o.sensitivity = -1/3.0;
        lastPos = GetMousePos();
        lastMouseEventTime = Blaze3D.getTime();
    }

    public static void DeactivateMouseLock()
    {
        o.sensitivity = sensitivity;
    }
}
