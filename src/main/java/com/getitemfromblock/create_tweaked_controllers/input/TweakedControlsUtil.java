package com.getitemfromblock.create_tweaked_controllers.input;

import com.getitemfromblock.create_tweaked_controllers.config.ModClientConfig;
import com.getitemfromblock.create_tweaked_controllers.config.ModKeyMappings;
import com.getitemfromblock.create_tweaked_controllers.controller.ControllerRedstoneOutput;

public class TweakedControlsUtil
{
    public static ControlProfile profile = new ControlProfile();
    private static boolean lastMouseKeyState = false;

    public static void Update()
    {
        if (ModClientConfig.USE_CUSTOM_MAPPINGS.get())
        {
            if (ModKeyMappings.KEY_MOUSE_RESET.isDown())
            {
                ModKeyMappings.KEY_MOUSE_RESET.setDown(false);
                MouseCursorHandler.ResetCenter();
            }
            if (ModKeyMappings.KEY_MOUSE_FOCUS.isDown())
            {
                if (!lastMouseKeyState)
                {
                    MouseCursorHandler.StorePlayerRotations();
                }
                MouseCursorHandler.RestorePlayerRotation();
                lastMouseKeyState = true;
            }
            else
            {
                lastMouseKeyState = false;
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
        profile.duplicatedKeys.forEach((i, key) -> key.setDown(false));
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
        for (int i = 0; i < ControllerRedstoneOutput.buttons.length; i++)
        {
            ControllerRedstoneOutput.buttons[i] = GamepadInputs.GetButton(i);
        }
        for (int i = 0; i < ControllerRedstoneOutput.axes.length; i++)
        {
            if (i >= 4) // triggers
            {
                float v = (GamepadInputs.GetAxis(i) + 1) / 2;
                if (v < 0) v = 0;
                if (v > 1) v = 1;
                ControllerRedstoneOutput.axes[i] = (byte)(Math.round(v * 15));
            }
            else // joystick axis
            {
                float v = GamepadInputs.GetAxis(i);
                boolean negative = v < 0;
                if (negative) v = -v;
                if (v < 0) v = 0;
                if (v > 1) v = 1;
                ControllerRedstoneOutput.axes[i] = (byte)Math.round(v * 15);
                if (negative && ControllerRedstoneOutput.axes[i] > 0) ControllerRedstoneOutput.axes[i] += 16;
            }
        }
    }
}
