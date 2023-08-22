package com.getitemfromblock.create_tweaked_controllers.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.input.GenericInput;
import com.getitemfromblock.create_tweaked_controllers.input.JoystickAxisInput;
import com.getitemfromblock.create_tweaked_controllers.input.JoystickButtonInput;
import com.getitemfromblock.create_tweaked_controllers.input.KeyboardInput;
import com.getitemfromblock.create_tweaked_controllers.input.MouseAxisInput;
import com.getitemfromblock.create_tweaked_controllers.input.MouseButtonInput;
import com.getitemfromblock.create_tweaked_controllers.input.GenericInput.InputType;
import com.mojang.blaze3d.platform.InputConstants.Key;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class ControlProfile
{
    public GenericInput[] layout = new GenericInput[25];
    public boolean hasJoystickInput = false;
    public ArrayList<KeyMapping> duplicatedKeys = new ArrayList<KeyMapping>();
    
    public ControlProfile()
    {
        this(0);
    }

    public ControlProfile(int id)
    {
        Load(0);
        Save(0);
    }

    public void Load(int id)
    {
        Load("config/gamepad_profiles/gamepad_profile_" + id);
        UpdateProfileData();
    }

    public void Save(int id)
    {
        Save("config/gamepad_profiles/gamepad_profile_" + id);
    }

    static final int[] keys = 
        {
            GLFW.GLFW_KEY_SEMICOLON,
            GLFW.GLFW_KEY_P,
            GLFW.GLFW_KEY_L,
            GLFW.GLFW_KEY_O,
            GLFW.GLFW_KEY_U,
            GLFW.GLFW_KEY_I,
            GLFW.GLFW_KEY_R,
            GLFW.GLFW_KEY_Y,
            GLFW.GLFW_KEY_T,
            GLFW.GLFW_KEY_H,
            GLFW.GLFW_KEY_J,
            GLFW.GLFW_KEY_UP,
            GLFW.GLFW_KEY_RIGHT,
            GLFW.GLFW_KEY_DOWN,
            GLFW.GLFW_KEY_LEFT
        };

    public void InitDefaultLayout()
    {
        for (int i = 0; i < 15; i++)
        {
            if (i == 9 || i == 10)
            {
                layout[i] = new MouseButtonInput(i - 9);
            }
            else
            {
                layout[i] = new KeyboardInput(keys[i]);
            }
        }
        for (int i = 15; i < 25; i++)
        {
            if (i == 16 || i == 18 || i == 20 || i == 22)
            {
                if (i > 18)
                {
                    layout[i] = new MouseAxisInput(i == 22, 0.0f, -1000.0f, false);
                }
                else
                {
                    layout[i] = new KeyboardInput(i == 16 ? GLFW.GLFW_KEY_A : GLFW.GLFW_KEY_W);
                }
            }
            else if (i > 22)
            {
                layout[i] = new KeyboardInput(i == 23 ? GLFW.GLFW_KEY_B : GLFW.GLFW_KEY_N);
            }
            else
            {
                if (i > 18)
                {
                    layout[i] = new MouseAxisInput(i == 21, 0.0f, 1000.0f, false);
                }
                else
                {
                    layout[i] = new KeyboardInput(i == 15 ? GLFW.GLFW_KEY_D : GLFW.GLFW_KEY_S);
                }
            }
        }
    }

    public void Load(String path)
    {
        File f = new File(path);
        if(!f.exists() || f.isDirectory())
        {
            InitDefaultLayout();
            return;
        }
        else try
        {
            FileInputStream file = new FileInputStream(f);
            DataInputStream buf = new DataInputStream(file);
            for (int i = 0; i < layout.length; i++)
            {
                switch (InputType.GetType(buf.readByte()))
                {
                    case NONE:
                        layout[i] = null;
                        break;
                    case JOYSTICK_BUTTON:
                        layout[i] = new JoystickButtonInput();
                        break;
                    case JOYSTICK_AXIS:
                        layout[i] = new JoystickAxisInput();
                        break;
                    case MOUSE_BUTTON:
                        layout[i] = new MouseButtonInput();
                        break;
                    case MOUSE_AXIS:
                        layout[i] = new MouseAxisInput();
                        break;
                    case KEYBOARD_KEY:
                        layout[i] = new KeyboardInput();
                        break;
                    default:
                        throw new IOException("Corrupted Profile Data!");
                }
                if (layout[i] != null) layout[i].Deserialize(buf);
            }
            file.close();
        }
        catch (IOException e)
        {
            CreateTweakedControllers.log("Error loading controller profile \""+path+"\"!");
            for (StackTraceElement line : e.getStackTrace())
            {
                CreateTweakedControllers.log(line.toString());
            }
            return;
        }
    }

    public void Save(String path)
    {
        try
        {
            File f = new File(path);
            File p = f.getParentFile();
            if (!p.exists())
            {
                p.mkdirs();
            }
            f.createNewFile();
            FileOutputStream file = new FileOutputStream(f);
            DataOutputStream buf = new DataOutputStream(file);
            for (int i = 0; i < layout.length; i++)
            {
                if (layout[i] != null)
                {
                    buf.writeByte(InputType.GetValue(layout[i].GetType()));
                    layout[i].Serialize(buf);
                }
                else
                {
                    buf.writeByte(InputType.GetValue(InputType.NONE));
                }
            }
            file.close();
        }
        catch (IOException e)
        {
            CreateTweakedControllers.log("Error loading controller profile \""+path+"\"!");
            for (StackTraceElement line : e.getStackTrace())
            {
                CreateTweakedControllers.log(line.toString());
            }
            return;
        }
    }

    protected int GetUniqueKeyIndex(Key k)
    {
        switch (k.getType())
        {
            case MOUSE:
                return k.getValue();
            case KEYSYM:
                return k.getValue() + 512;
            default:
                return k.getValue() + 1024;
        }
    }

    protected int GetUniqueKeyIndex(GenericInput i)
    {
        switch (i.GetType())
        {
            case MOUSE_BUTTON:
                return i.GetValue();
            case MOUSE_AXIS:
                return i.GetValue() + 60;
            case KEYBOARD_KEY:
                return i.GetValue() + 512;
            case JOYSTICK_AXIS:
                return i.GetValue() + 64;
            default:
                return i.GetValue() + 128;
        }
    }

    public void UpdateProfileData()
    {
        duplicatedKeys.clear();
        hasJoystickInput = false;
        for (int i = 0; i < 25; i++)
        {
            if (layout[i] == null) continue;
            if (layout[i].GetType() != InputType.JOYSTICK_AXIS && layout[i].GetType() != InputType.JOYSTICK_BUTTON) continue;
            hasJoystickInput = true;
            break;
        }
        for (KeyMapping key : Minecraft.getInstance().options.keyMappings)
        {
            if (key.isUnbound()) continue;
            int id = GetUniqueKeyIndex(key.getKey());
            for (int i = 0; i < 25; i++)
            {
                if (layout[i] == null) continue;
                if (!layout[i].IsInputValid() || id != GetUniqueKeyIndex(layout[i])) continue;
                duplicatedKeys.add(key);
            }
        }
    }

    public boolean GetButton(int id)
    {
        return layout[id] != null ? layout[id].GetButtonValue() : false;
    }

    public float GetAxis(int id)
    {
        if (id < 4) // joystick axis
        {
            return (layout[15 + id * 2] != null ? layout[15 + id * 2].GetAxisValue() : 0)
                - (layout[16 + id * 2] != null ? layout[16 + id * 2].GetAxisValue() : 0);
        }
        else // trigger axis
        {
            return layout[id + 19] != null ? layout[id + 19].GetAxisValue() * 2 - 1 : -1;
        }
    }
}
