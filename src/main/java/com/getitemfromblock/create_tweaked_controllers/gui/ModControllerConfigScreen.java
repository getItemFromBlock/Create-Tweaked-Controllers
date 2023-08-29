package com.getitemfromblock.create_tweaked_controllers.gui;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.config.ModClientConfig;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedControlsUtil;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.ColoredButton;
import com.getitemfromblock.create_tweaked_controllers.gui.InputConfig.InputList;
import com.getitemfromblock.create_tweaked_controllers.input.GamepadInputs;
import com.getitemfromblock.create_tweaked_controllers.input.JoystickAxisInput;
import com.getitemfromblock.create_tweaked_controllers.input.JoystickButtonInput;
import com.getitemfromblock.create_tweaked_controllers.input.JoystickInputs;
import com.getitemfromblock.create_tweaked_controllers.input.KeyboardInput;
import com.getitemfromblock.create_tweaked_controllers.input.MouseAxisInput;
import com.getitemfromblock.create_tweaked_controllers.input.MouseButtonInput;
import com.getitemfromblock.create_tweaked_controllers.input.MouseCursorHandler;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class ModControllerConfigScreen extends AbstractSimiScreen
{
    protected final Screen parent;
    protected ModGuiTextures background;
    private JoystickIcon lStick;
    private JoystickIcon rStick;
    private ControllerButton[] controllerButtons;
    private PlainRect[] triggerAxis;

    private int selectedInput = -1;
    private InputList inputBindsList;

    public ModControllerConfigScreen(Screen p)
    {
        parent = p;
        background = ModGuiTextures.CONTROLLER_BACKGROUND;
    }

    @Override
    protected void init()
    {
        super.init();
        Populate();
    }

    @Override
    public void tick()
    {
    }

    @Override
    public boolean mouseClicked(double x, double y, int button)
    {
        if (selectedInput != -1)
        {
            TweakedControlsUtil.profile.layout[selectedInput] = new MouseButtonInput(button);
            selectedInput = -1;
            TweakedControlsUtil.profile.UpdateProfileData();
            return true;
        }
        else
        {
            return super.mouseClicked(x, y, button);
        }
    }

    @Override
    public boolean keyPressed(int key, int scan, int modif)
    {
        if (selectedInput != -1)
        {
            if (key == 256)
            {
                TweakedControlsUtil.profile.layout[selectedInput] = null;
            }
            else
            {
                TweakedControlsUtil.profile.layout[selectedInput] = new KeyboardInput(key);
            }
            TweakedControlsUtil.profile.UpdateProfileData();
            selectedInput = -1;
            return true;
        }
        else
        {
            return super.keyPressed(key, scan, modif);
        }
    }

    @Override
    protected void renderWindow(PoseStack ms, int mouseX, int mouseY, float partialTicks)
    {
        TweakedControlsUtil.GuiUpdate();
        if (selectedInput != -1)
        {
            if (selectedInput != -1 && (HandleMouseMovement() || HandleJoystickButtons() || HandleJoystickAxis()))
            {
                selectedInput = -1;
                TweakedControlsUtil.profile.UpdateProfileData();
            }
        }
        
        inputBindsList.render(ms, mouseX, mouseY, partialTicks);
        int x = (width - background.width) / 2;
        int y =  10;
        background.render(ms, x, y, this);
        Vec2 v = new Vec2(GamepadInputs.axis[0], GamepadInputs.axis[1]);
        if (v.lengthSquared() > 1)
            v = v.normalized();
        int vx = (int)(v.x * 10);
        int vy = (int)(v.y * 10);
        lStick.move(vx, vy);
        controllerButtons[9].move(vx, vy);
        v = new Vec2(GamepadInputs.axis[2], GamepadInputs.axis[3]);
        if (v.lengthSquared() > 1)
            v = v.normalized();
        vx = (int)(v.x * 10);
        vy = (int)(v.y * 10);
        rStick.move(vx, vy);
        controllerButtons[10].move(vx, vy);
        for (int i = 0; i < triggerAxis.length; i++)
        {
            triggerAxis[i].SetValue((GamepadInputs.axis[i + 4] + 1) / 2);
        }
        for (int i = 0; i < controllerButtons.length; i++)
        {
            controllerButtons[i].SetColorFactor(GamepadInputs.buttons[i] ? 1.0f : 50/255.0f);
        }
        if (selectedInput != -1)
        {
            float val = (Mth.sin((float)Blaze3D.getTime() * Mth.PI * 4) + 1) / 2;
            if (selectedInput < 15)
            {
                controllerButtons[selectedInput].SetColorFactor(Mth.lerp(val, 50/255.0f, 1.0f));
            }
            else if (selectedInput < 23)
            {
                vx = 0;
                vy = 0;
                switch (selectedInput)
                {
                    case 15:
                        vx = (int)(val * 10);
                        break;
                    case 16:
                        vx = (int)(-val * 10);
                        break;
                    case 17:
                        vy = (int)(val * 10);
                        break;
                    case 18:
                        vy = (int)(-val * 10);
                        break;
                    case 19:
                        vx = (int)(val * 10);
                        break;
                    case 20:
                        vx = (int)(-val * 10);
                        break;
                    case 21:
                        vy = (int)(val * 10);
                        break;
                    default:
                    vy = (int)(-val * 10);
                        break;
                }
                if (selectedInput < 19)
                {
                    lStick.move(vx, vy);
                    controllerButtons[9].move(vx, vy);
                }
                else
                {
                    rStick.move(vx, vy);
                    controllerButtons[10].move(vx, vy);
                }
            }
            else
            {
                triggerAxis[selectedInput-23].SetValue(val);
            }
        }
    }

    public void SetActiveInput(int index)
    {
        selectedInput = index;
        MouseCursorHandler.ResetCenter();
        JoystickInputs.StoreAxisValues();
    }

    public int GetActiveInput()
    {
        return selectedInput;
    }

    private boolean HandleMouseMovement()
    {
        float deltaM = MouseCursorHandler.GetX(false);
        float deltaM2 = MouseCursorHandler.GetY(false);
        boolean isY = false;
        if (Math.abs(deltaM) < Math.abs(deltaM2))
        {
            deltaM = deltaM2;
            isY = true;
        }
        if (Math.abs(deltaM) > 250)
        {
            TweakedControlsUtil.profile.layout[selectedInput] = new MouseAxisInput(isY, 0, Math.copySign(1000.0f, deltaM), false);
            return true;
        }
        return false;
    }

    private boolean HandleJoystickButtons()
    {
        if (!JoystickInputs.HasJoystick()) return false;
        int val = JoystickInputs.GetFirstButton();
        if (val < 0) return false;
        TweakedControlsUtil.profile.layout[selectedInput] = new JoystickButtonInput(val);
        return true;
    }

    private boolean HandleJoystickAxis()
    {
        if (!JoystickInputs.HasJoystick()) return false;
        int val = JoystickInputs.GetFirstAxis();
        if (val < 0) return false;
        float start = JoystickInputs.GetStoredAxis(val);
        float dest = JoystickInputs.GetAxis(val);
        if (Math.abs(start) > 0.5f) // Probably a trigger
        {
            start = Math.copySign(1.0f, start);
            dest = -start;
        }
        else // Probably a joystick
        {
            start = 0.0f;
            dest = Math.copySign(1.0f, dest);
        }
        TweakedControlsUtil.profile.layout[selectedInput] = new JoystickAxisInput(val, start, dest);
        return true;
    }

    private void Populate()
    {
        inputBindsList = new InputList(this, minecraft);
        addWidget(inputBindsList);
        addRenderableWidget(new Button(this.width / 2 - 155, this.height - 29, 90, 20, CommonComponents.GUI_DONE, (p_193996_) -> {
            ScreenOpener.open(parent);
        }));
        addRenderableWidget(new ColoredButton(this.width / 2 - 155 + 100, this.height - 29, 90, 20, CreateTweakedControllers.translateDirect("gui_config_reset_all"), (p_193999_) -> {
            TweakedControlsUtil.profile.InitDefaultLayout();
            TweakedControlsUtil.profile.UpdateProfileData();
        }, new Vector3f(1.0f, 0.3f, 0.3f)));
        addRenderableWidget(new ColoredButton(this.width / 2 - 155 + 200, this.height - 29, 40, 20, CreateTweakedControllers.translateDirect("gui_config_save"), (p_193996_) -> {
            TweakedControlsUtil.profile.Save(0);
        }, new Vector3f(0.5f, 0.5f, 1.0f)));
        addRenderableWidget(new ColoredButton(this.width / 2 - 155 + 250, this.height - 29, 40, 20, CreateTweakedControllers.translateDirect("gui_config_load"), (p_193996_) -> {
            TweakedControlsUtil.profile.Load(0);
        }, new Vector3f(0.5f, 0.5f, 1.0f)));
        int x = (width - background.width) / 2;
        int y = 10;
        lStick = new JoystickIcon(x + 21, y + 34, ModIcons.I_LEFT_JOYSTICK);
        rStick = new JoystickIcon(x + 86, y + 66, ModIcons.I_RIGHT_JOYSTICK);
        addRenderableOnly(lStick);
        addRenderableOnly(rStick);
        controllerButtons = new ControllerButton[15];
        switch (ModClientConfig.CONTROLLER_LAYOUT_TYPE.get())
        {
            case NINTENDO:
                controllerButtons[0] = new ControllerButton(x + 113, y + 49, ControllerButtonRenderer.B_B, new Vector3f(1.0f,1.0f,0));
                controllerButtons[1] = new ControllerButton(x + 124, y + 38, ControllerButtonRenderer.B_A, new Vector3f(1.0f,0,0));
                controllerButtons[2] = new ControllerButton(x + 102, y + 38, ControllerButtonRenderer.B_Y, new Vector3f(0,1.0f,0));
                controllerButtons[3] = new ControllerButton(x + 113, y + 27, ControllerButtonRenderer.B_X, new Vector3f(0,0.25f,1.0f));
                break;
            
            case PLAYSTATION:
                controllerButtons[0] = new ControllerButton(x + 113, y + 49, ControllerButtonRenderer.B_CROSS, new Vector3f(0.49f,0.706f,0.914f));
                controllerButtons[1] = new ControllerButton(x + 124, y + 38, ControllerButtonRenderer.B_CIRCLE, new Vector3f(1.0f,0.4f,0.4f));
                controllerButtons[2] = new ControllerButton(x + 102, y + 38, ControllerButtonRenderer.B_SQUARE, new Vector3f(1.0f,0.412f,0.973f));
                controllerButtons[3] = new ControllerButton(x + 113, y + 27, ControllerButtonRenderer.B_TRIANGLE, new Vector3f(0.243f,0.89f,0.631f));
                break;
        
            default:
                controllerButtons[0] = new ControllerButton(x + 113, y + 49, ControllerButtonRenderer.B_A, new Vector3f(0,1,0));
                controllerButtons[1] = new ControllerButton(x + 124, y + 38, ControllerButtonRenderer.B_B, new Vector3f(1,0,0));
                controllerButtons[2] = new ControllerButton(x + 102, y + 38, ControllerButtonRenderer.B_X, new Vector3f(0,0,1));
                controllerButtons[3] = new ControllerButton(x + 113, y + 27, ControllerButtonRenderer.B_Y, new Vector3f(1,1,0));
                break;
        }
        controllerButtons[4] = new ControllerButton(x + 38, y + 1, ControllerButtonRenderer.B_L, new Vector3f(1,0,0));
        controllerButtons[5] = new ControllerButton(x + 100, y + 1, ControllerButtonRenderer.B_R, new Vector3f(1,0,0));
        controllerButtons[6] = new ControllerButton(x + 58, y + 38, ControllerButtonRenderer.B_SELECT, new Vector3f(1,0,0));
        controllerButtons[7] = new ControllerButton(x + 80, y + 38, ControllerButtonRenderer.B_START, new Vector3f(1,0,0));
        controllerButtons[8] = new ControllerButton(x + 69, y + 27, ControllerButtonRenderer.B_MIDDLE, new Vector3f(1,0,0));
        controllerButtons[9] = new ControllerButton(x + 26, y + 39, ControllerButtonRenderer.B_LJ, new Vector3f(1,0,0));
        controllerButtons[10] = new ControllerButton(x + 91, y + 71, ControllerButtonRenderer.B_RJ, new Vector3f(1,0,0));
        controllerButtons[11] = new ControllerButton(x + 47, y + 60, ControllerButtonRenderer.B_UP, new Vector3f(1,0,0));
        controllerButtons[12] = new ControllerButton(x + 58, y + 71, ControllerButtonRenderer.B_RIGHT, new Vector3f(1,0,0));
        controllerButtons[13] = new ControllerButton(x + 47, y + 82, ControllerButtonRenderer.B_DOWN, new Vector3f(1,0,0));
        controllerButtons[14] = new ControllerButton(x + 36, y + 71, ControllerButtonRenderer.B_LEFT, new Vector3f(1,0,0));
        for (int i = 0; i < controllerButtons.length; i++)
        {
            addRenderableOnly(controllerButtons[i]);
        }
        triggerAxis = new PlainRect[2];
        triggerAxis[0] = new PlainRect(x + 2, y + 3, 0, 5, x + 2, y + 3, 32, 5, new Vector3f(1,0,0));
        triggerAxis[1] = new PlainRect(x + 145, y + 3, 0, 5, x + 145, y + 3, -32, 5, new Vector3f(1,0,0));
        for (int i = 0; i < triggerAxis.length; i++)
        {
            addRenderableOnly(triggerAxis[i]);
        }
    }

    public boolean isPauseScreen()
    {
        return true;
    }
        
}
