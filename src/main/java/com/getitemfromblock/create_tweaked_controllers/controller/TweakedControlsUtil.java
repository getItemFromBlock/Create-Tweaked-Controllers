package com.getitemfromblock.create_tweaked_controllers.controller;

import com.getitemfromblock.create_tweaked_controllers.config.ModClientConfig;
import com.getitemfromblock.create_tweaked_controllers.config.ModKeyMappings;
import com.getitemfromblock.create_tweaked_controllers.input.GamepadInputs;
import com.getitemfromblock.create_tweaked_controllers.input.JoystickInputs;
import com.getitemfromblock.create_tweaked_controllers.input.MouseCursorHandler;
import com.simibubi.create.foundation.utility.ControlsUtil;

public class TweakedControlsUtil
{
    public static ControlProfile profile = new ControlProfile();
    public static ControllerRedstoneOutput output = new ControllerRedstoneOutput();
    private static boolean lastMouseKeyState = false;

    public static void GuiUpdate()
    {
        MouseCursorHandler.Update();
        if (ControlsUtil.isActuallyPressed(ModKeyMappings.KEY_MOUSE_RESET))
        {
            ModKeyMappings.KEY_MOUSE_RESET.setDown(false);
            MouseCursorHandler.ResetCenter();
        }
        JoystickInputs.GetControls();
        if (ModClientConfig.USE_CUSTOM_MAPPINGS.get())
        {
            FillInputs();
        }
        else
        {
            GamepadInputs.GetControls();
            FillGamepadInputs();
        }
    }

    public static void Update()
    {
        if (ModClientConfig.USE_CUSTOM_MAPPINGS.get())
        {
            if (ControlsUtil.isActuallyPressed(ModKeyMappings.KEY_MOUSE_FOCUS))
            {
                if (!lastMouseKeyState)
                {
                    MouseCursorHandler.ActivateMouseLock();
                }
                else
                {
                    MouseCursorHandler.Update();
                }
                lastMouseKeyState = true;
            }
            else if (lastMouseKeyState)
            {
                MouseCursorHandler.DeactivateMouseLock();
                lastMouseKeyState = false;
            }
            if (ControlsUtil.isActuallyPressed(ModKeyMappings.KEY_MOUSE_RESET))
            {
                ModKeyMappings.KEY_MOUSE_RESET.setDown(false);
                MouseCursorHandler.ResetCenter();
            }
            if (profile.hasJoystickInput)
            {
                JoystickInputs.GetControls();
            }
            FillInputs();
        }
        else
        {
            GamepadInputs.GetControls();
            FillGamepadInputs();
        }
        
    }

    public static void FillInputs()
    {
        profile.duplicatedKeys.forEach(key ->
        {
            key.setDown(false);
            while (key.consumeClick()) {};
        });
        for (int i = 0; i < GamepadInputs.buttons.length; i++)
        {
            GamepadInputs.buttons[i] = profile.GetButton(i);
        }
        for (int i = 0; i < GamepadInputs.axis.length; i++)
        {
            GamepadInputs.axis[i] = profile.GetAxis(i);
        }
        FillGamepadInputs();
    }

    public static void FillGamepadInputs()
    {
        for (int i = 0; i < output.buttons.length; i++)
        {
            output.buttons[i] = GamepadInputs.GetButton(i);
        }
        for (int i = 0; i < output.axis.length; i++)
        {
            if (i >= 4) // triggers
            {
                float v = (GamepadInputs.GetAxis(i) + 1) / 2;
                if (v < 0) v = 0;
                if (v > 1) v = 1;
                output.axis[i] = (byte)(Math.round(v * 15));
            }
            else // joystick axis
            {
                float v = GamepadInputs.GetAxis(i);
                boolean negative = v < 0;
                if (negative) v = -v;
                if (v < 0) v = 0;
                if (v > 1) v = 1;
                output.axis[i] = (byte)Math.round(v * 15);
                if (negative && output.axis[i] > 0) output.axis[i] = (byte)(output.axis[i] + 16);
            }
        }
    }
}
