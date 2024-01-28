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

    public static final byte CURRENT_VERSION_MAJOR = (byte) 0x01;
    public static final byte CURRENT_VERSION_MINOR = (byte) 0x00;
    
    public ControlProfile()
    {
        //this(ControlType.KEYBOARD_MOUSE);
        this(0);
    }

    public ControlProfile(ControlType type)
    {
        CheckProfileUpgrade();
        Load(type);
        Save(type);
    }

    public ControlProfile(int id)
    {
        Load(0);
        Save(0);
    }

    public void Load(ControlType type)
    {
        Load("config/gamepad_profiles/gamepad_profile_" + type.toString(), type);
        UpdateProfileData();
    }

    public void Save(ControlType type)
    {
        Save("config/gamepad_profiles/gamepad_profile_" + type.toString());
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

    private void CheckProfileUpgrade()
    {
        File f = new File("config/gamepad_profiles/gamepad_profile_0");
        if(!f.exists() || f.isDirectory())
        {
            return;
        }
        Load("config/gamepad_profiles/gamepad_profile_0", ControlType.CUSTOM_0, false);
        Save(ControlType.CUSTOM_0);
        try
        {
            f.delete();
        }
        catch (Exception e)
        {
            CreateTweakedControllers.error("Error upgrading old controller profile: could not delete old file \"config/gamepad_profiles/gamepad_profile_0\"!");
            CreateTweakedControllers.error(e.getMessage());
            for (StackTraceElement line : e.getStackTrace())
            {
                CreateTweakedControllers.error(line.toString());
            }
            return;
        }
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

    public void InitDefaultLayout(ControlType fallbackType)
    {
        if (fallbackType == ControlType.JOYSTICK)
        {
            InitDefaultGamepadLayout();
        }
        else
        {
            InitDefaultKeyboardLayout();
        }
    }

    public void InitDefaultKeyboardLayout()
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

    public void InitDefaultGamepadLayout()
    {
        for (int i = 0; i < 15; i++)
        {
            layout[i] = new JoystickButtonInput(i);
        }
        for (int i = 15; i < 25; i++)
        {
            if (i == 16 || i == 18 || i == 20 || i == 22)
            {
                layout[i] = new JoystickAxisInput(i - 15, 0.05f, -1.0f);
            }
            else if (i > 22)
            {
                layout[i] = new JoystickAxisInput(i - 15, -1.0f, 1.0f);
            }
            else
            {
                layout[i] = new JoystickAxisInput(i - 15, 0.05f, 1.0f);
            }
        }
    }

    private void Load(String path, ControlType fallbackType)
    {
        Load(path, fallbackType, true);
    }

    static final byte[] headerNameData = 
        {
            'C',
            'T',
            'C',
            'P',
            'R',
            'F',
        };

    private void Load(String path, ControlType fallbackType, boolean hasHeader)
    {
        File f = new File(path);
        if(!f.exists() || f.isDirectory())
        {
            InitDefaultLayout(fallbackType);
            return;
        }
        else try
        {
            FileInputStream file = new FileInputStream(f);
            DataInputStream buf = new DataInputStream(file);
            int version = 0;
            if (hasHeader)
            {
                byte[] header = buf.readNBytes(8);
                boolean valid = header.length == 8;
                for (int i = 0; i < 6 && valid; i++)
                {
                    if (header[i] != headerNameData[i])
                    {
                        valid = false;
                    }
                }
                if (!valid)
                {
                    file.close();
                    throw new IOException("Corrupted Profile Data!");
                }
                version = 256 * header[6] + header[7];
            }
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
            CreateTweakedControllers.error("Error loading controller profile \""+path+"\"!");
            for (StackTraceElement line : e.getStackTrace())
            {
                CreateTweakedControllers.error(line.toString());
            }
            return;
        }
    }

    private void Load(String path)
    {
        File f = new File(path);
        if(!f.exists() || f.isDirectory())
        {
            InitDefaultLayout(ControlType.KEYBOARD_MOUSE);
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
            CreateTweakedControllers.error("Error loading controller profile \""+path+"\"!");
            for (StackTraceElement line : e.getStackTrace())
            {
                CreateTweakedControllers.error(line.toString());
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
            /*
            for (int i = 0; i < headerNameData.length; i++)
            {
                buf.writeByte(headerNameData[i]);
            }
            buf.writeByte(CURRENT_VERSION_MAJOR);
            buf.writeByte(CURRENT_VERSION_MINOR);
            */
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
            CreateTweakedControllers.error("Error loading controller profile \""+path+"\"!");
            for (StackTraceElement line : e.getStackTrace())
            {
                CreateTweakedControllers.error(line.toString());
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
