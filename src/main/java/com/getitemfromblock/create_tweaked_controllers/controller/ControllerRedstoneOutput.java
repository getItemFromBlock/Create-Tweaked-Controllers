package com.getitemfromblock.create_tweaked_controllers.controller;

public class ControllerRedstoneOutput
{
    public static boolean[] buttons = new boolean[15];
    public static byte[] axes = new byte[6];

    public static void Clear()
    {
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = false;
        }
        for (int i = 0; i < axes.length; i++)
        {
            axes[i] = 0;
        }
    }
}
