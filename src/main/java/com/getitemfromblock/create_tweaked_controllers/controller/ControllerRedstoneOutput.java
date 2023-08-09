package com.getitemfromblock.create_tweaked_controllers.controller;

public class ControllerRedstoneOutput
{
    public static boolean[] buttons = new boolean[15];
    public static byte[] axis = new byte[6];

    public static void Clear()
    {
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = false;
        }
        for (int i = 0; i < axis.length; i++)
        {
            axis[i] = 0;
        }
    }

    public static short EncodeButtons() // Returns buttons packed in a short
    {
        short result = 0;
        for (int i = 0; i < buttons.length; i++)
        {
            if (!buttons[i]) continue;
            result |= 1 << i;
        }
        return result;
    }

    public static void DecodeButtons(short value) // Decode buttons from packed value
    {
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = ((value & 1 << i) != 0);
        }
    }

    public static int EncodeAxis() // Returns axis packed in an integer
    {
        int result = 0;
        for (int i = 0; i < axis.length; i++)
        {
            if (i < 4)
            {
                result |= axis[i] << i*5; // each joystick axis uses at most 5 bit (1 sign bit + 4 value bits)
            }
            else
            {
                result |= axis[i] << (i == 4 ? 20 : 24); // trigger axis are always positive
            }
        }
        return result;
    }

    public static void DecodeAxis(int value) // Decode axis from packed value
    {
        for (int i = 0; i < axis.length; i++)
        {
            if (i < 4)
            {
                axis[i] = (byte)((value & 0x1f << i*5) >> i*5);
            }
            else
            {
                int dec = i == 4 ? 20 : 24;
                axis[i] = (byte)((value & 0xf << dec) >> dec);
            }
        }
    }
}
