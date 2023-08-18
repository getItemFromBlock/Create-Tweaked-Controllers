package com.getitemfromblock.create_tweaked_controllers.gui;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedControlsUtil;
import com.getitemfromblock.create_tweaked_controllers.input.GamepadInputs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.phys.Vec2;

public class ModControllerConfigScreen extends AbstractSimiScreen
{
    protected final Screen parent;
    protected boolean returnOnClose;
	protected ModGuiTextures background;
	private JoystickIcon lStick;
	private JoystickIcon rStick;
	private ControllerButton[] controllerButtons;
	private PlainRect[] triggerAxis;

    public ModControllerConfigScreen(Screen parent)
    {
        this.parent = parent;
        this.returnOnClose = true;
		this.background = ModGuiTextures.CONTROLLER_BACKGROUND;
    }

    @Override
    protected void init()
    {
        super.init();
        this.returnOnClose = true;
        this.Populate();
    }

    @Override
    protected void renderWindow(PoseStack ms, int mouseX, int mouseY, float partialTicks)
    {
        int x = (width - background.width) / 2;
		int y =  30;
		TweakedControlsUtil.Update();
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
		font.draw(ms, title, x + 15, y + 4, 0xFFFFFF);
    }

    private void Populate()
    {
        int yStart = this.height / 4 + 40;
        int center = this.width / 2;
        int bHeight = 20;
        int bLongWidth = 200;
        this.addRenderableWidget(new Button(center - 100, yStart + 92, bLongWidth, bHeight, CreateTweakedControllers.translateDirect("menu.return", new Object[0]), ($) -> {
            this.linkTo(this.parent);
        }));
		int x = (width - background.width) / 2;
		int y = 30;
		lStick = new JoystickIcon(x + 21, y + 34, ModIcons.I_LEFT_JOYSTICK);
		rStick = new JoystickIcon(x + 86, y + 66, ModIcons.I_RIGHT_JOYSTICK);
		addRenderableOnly(lStick);
		addRenderableOnly(rStick);
		controllerButtons = new ControllerButton[15];
		controllerButtons[0] = new ControllerButton(x + 113, y + 49, ControllerButtonRenderer.B_A, new Vector3f(0,1,0));
		controllerButtons[1] = new ControllerButton(x + 124, y + 38, ControllerButtonRenderer.B_B, new Vector3f(1,0,0));
		controllerButtons[2] = new ControllerButton(x + 102, y + 38, ControllerButtonRenderer.B_X, new Vector3f(0,0,1));
		controllerButtons[3] = new ControllerButton(x + 113, y + 27, ControllerButtonRenderer.B_Y, new Vector3f(1,1,0));
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

   private void linkTo(Screen screen)
   {
      this.returnOnClose = false;
      ScreenOpener.open(screen);
   }

   public boolean isPauseScreen()
   {
      return true;
   }
        
}
