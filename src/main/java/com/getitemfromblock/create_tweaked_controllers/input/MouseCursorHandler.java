package com.getitemfromblock.create_tweaked_controllers.input;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.Blaze3D;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;

public class MouseCursorHandler
{
    public static Vec2 delta = null;
    public static Vec2 lastPos = null;
    public static Vec2 vel = null;
    private static MouseHandler m = null;
    private static double lastMouseEventTime;
    private static boolean mouseLockActive = false;
    private static float deltaT = 0;
    private static Vector2f savedRot = new Vector2f();

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
        Minecraft mc = Minecraft.getInstance();
        m = mc.mouseHandler;
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

    // The trick of setting the mouse sensitivity to -1/3 does not work anymore...
    public static void ActivateMouseLock()
    {
        LocalPlayer player = Minecraft.getInstance().player;
        savedRot.x = player.getXRot();
        savedRot.y = player.getYRot();
        mouseLockActive = true;
        lastPos = GetMousePos();
        lastMouseEventTime = Blaze3D.getTime();
    }

    public static void DeactivateMouseLock()
    {
        mouseLockActive = false;
    }

    public static void CancelPlayerTurn()
    {
        if (!mouseLockActive) return;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        player.turn((savedRot.y - player.getYRot()) / 0.15f, (savedRot.x - player.getXRot()) / 0.15f);
        player.xBob = savedRot.x;
        player.yBob = savedRot.y;
        player.xBobO = savedRot.x;
        player.yBobO = savedRot.y;
    }
}
