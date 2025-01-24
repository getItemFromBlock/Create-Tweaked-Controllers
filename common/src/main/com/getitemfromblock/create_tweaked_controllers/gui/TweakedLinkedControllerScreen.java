package com.getitemfromblock.create_tweaked_controllers.gui;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.config.ModClientConfig;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedControlsUtil;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerMenu;
import com.getitemfromblock.create_tweaked_controllers.input.GamepadInputs;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class TweakedLinkedControllerScreen extends AbstractSimiContainerScreen<TweakedLinkedControllerMenu>
{

    protected ModGuiTextures background0;
    protected ModGuiTextures background1;
    private List<Rect2i> extraAreas = Collections.emptyList();

    private IconButton resetButton;
    private IconButton confirmButton;
    private IconButton refreshButton;
    private IconButton firstTabButton;
    private IconButton secondTabButton;
    private JoystickIcon lStick;
    private JoystickIcon rStick;
    private DigitIcon controllerDigits[];
    private DigitIcon axisDigits[];
    private boolean isSecondPage = false;

    public TweakedLinkedControllerScreen(TweakedLinkedControllerMenu menu, Inventory inv, Component title)
    {
        super(menu, inv, title);
        this.background0 = ModGuiTextures.TWEAKED_LINKED_CONTROLLER_0;
        this.background1 = ModGuiTextures.TWEAKED_LINKED_CONTROLLER_1;
    }

    private static final int[] axisDigitPositions =
    {
        19, 54,
        19, 64,
        19, 117,
        19, 127,
        162, 53,
        162, 116
    };

    @Override
    protected void init()
    {
        setWindowSize(background0.width, background0.height + 4 + PLAYER_INVENTORY.height);
        setWindowOffset(1, 0);
        super.init();

        int x = leftPos;
        int y = topPos;
        
        resetButton = new IconButton(x + background0.width - 62, y + background0.height - 24, AllIcons.I_TRASH);
        resetButton.withCallback(() -> {
            menu.clearContents();
            menu.sendClearPacket();
        });
        confirmButton = new IconButton(x + background0.width - 33, y + background0.height - 24, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            minecraft.player.closeContainer();
        });
        refreshButton = new IconButton(x + background0.width - 91, y + background0.height - 24, AllIcons.I_REFRESH);
        refreshButton.withCallback(() -> {
            GamepadInputs.SearchGamepad();
        });
        refreshButton.setToolTip(CreateTweakedControllers.translateDirect("gui_button_refresh"));
        firstTabButton = new IconButton(x + 17, y + background0.height - 27, ModIcons.I_BUTTON);
        firstTabButton.withCallback(() -> {
            this.isSecondPage = false;
            menu.SetPage(this.isSecondPage);
        });
        firstTabButton.setToolTip(CreateTweakedControllers.translateDirect("gui_tab_button"));
        secondTabButton = new IconButton(x + 42, y + background0.height - 27, ModIcons.I_AXES);
        secondTabButton.withCallback(() -> {
            this.isSecondPage = true;
            menu.SetPage(this.isSecondPage);
        });
        secondTabButton.setToolTip(CreateTweakedControllers.translateDirect("gui_tab_axis"));
        lStick = new JoystickIcon(x + 16, y + 26, ModIcons.I_LEFT_JOYSTICK);
        rStick = new JoystickIcon(x + 16, y + 89, ModIcons.I_RIGHT_JOYSTICK);
        controllerDigits = new DigitIcon[2];
        for (int i = 0; i < controllerDigits.length; i++)
        {
            controllerDigits[i] = new DigitIcon(x + 107 + i * 6, y + 151, DigitIconRenderer.D_DASH, new Vector3f(1, 0, 0));
            addRenderableOnly(controllerDigits[i]);
        }
        axisDigits = new DigitIcon[18];
        for (int i = 0; i < axisDigits.length; i++)
        {
            axisDigits[i] = new DigitIcon(x + axisDigitPositions[i/3*2] + (i % 3) * 6, y + axisDigitPositions[i/3*2 + 1], DigitIconRenderer.D_DASH, new Vector3f(1, 0, 0));
            addRenderableOnly(axisDigits[i]);
        }

        addRenderableWidget(resetButton);
        addRenderableWidget(confirmButton);
        addRenderableWidget(refreshButton);
        addRenderableWidget(firstTabButton);
        addRenderableWidget(secondTabButton);
        addRenderableOnly(lStick);
        addRenderableOnly(rStick);

        extraAreas = ImmutableList.of(new Rect2i(x + background0.width + 4, y + background0.height - 44, 64, 56));
    }

    @Override
    protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY)
    {
        int invX = getLeftOfCentered(PLAYER_INVENTORY.width);
        int invY = topPos + background0.height + 4;
        renderPlayerInventory(ms, invX, invY);

        int x = leftPos;
        int y = topPos;
        TweakedControlsUtil.Update();
        if (isSecondPage)
        {
            background1.render(ms, x, y, this);
            Vec2 v = new Vec2(GamepadInputs.axis[0], GamepadInputs.axis[1]);
            if (v.lengthSquared() > 1)
                v = v.normalized();
            lStick.move((int)(v.x * 10), (int)(v.y * 10));
            v = new Vec2(GamepadInputs.axis[2], GamepadInputs.axis[3]);
            if (v.lengthSquared() > 1)
                v = v.normalized();
            rStick.move((int)(v.x * 10), (int)(v.y * 10));
            lStick.visible = true;
            rStick.visible = true;
            for (int i = 0; i < 6; i++)
            {
                float value = i < 4 ? Math.abs(GamepadInputs.axis[i]) : (GamepadInputs.axis[i] + 1) / 2;
                if (value < 0) value = 0;
                if (value > 1) value = 1;
                int index = Math.round(value * 15);
                if (i < 4 && GamepadInputs.axis[i] < 0 && index != 0)
                {
                    axisDigits[i*3].setIcon(DigitIconRenderer.D_DASH);
                }
                else
                {
                    axisDigits[i*3].setIcon(DigitIconRenderer.D_EMPTY);
                }
                axisDigits[i*3+1].setIcon(DigitIconRenderer.D_NUMBERS[index/10]);
                axisDigits[i*3+2].setIcon(DigitIconRenderer.D_NUMBERS[index%10]);
                for (int j = 0; j < 3; j++)
                {
                    axisDigits[i*3+j].visible = true;
                }
            }
        }
        else
        {
            background0.render(ms, x, y, this);
            lStick.visible = false;
            rStick.visible = false;
            for (int i = 0; i < axisDigits.length; i++)
            {
                axisDigits[i].visible = false;
            }
        }
        MutableComponent text;
        int index = GamepadInputs.GetGamepadIndex();
        if (ModClientConfig.USE_CUSTOM_MAPPINGS.get())
        {
            text = CreateTweakedControllers.translateDirect("gui_gamepad_custom");
            controllerDigits[0].setIcon(DigitIconRenderer.D_NUMBERS[0]);
            controllerDigits[1].setIcon(DigitIconRenderer.D_NUMBERS[0]);
        }
        else if (index < 0)
        {
            text = CreateTweakedControllers.translateDirect("gui_gamepad_unavailable");
            controllerDigits[0].setIcon(DigitIconRenderer.D_DASH);
            controllerDigits[1].setIcon(DigitIconRenderer.D_DASH);
        }
        else
        {
            text = CreateTweakedControllers.translateDirect("gui_gamepad_selected", "" + index);
            controllerDigits[0].setIcon(DigitIconRenderer.D_NUMBERS[index/10]);
            controllerDigits[1].setIcon(DigitIconRenderer.D_NUMBERS[index%10]);
        }
        controllerDigits[0].setToolTip(text);
        controllerDigits[1].setToolTip(text);
        font.draw(ms, title, x + 15, y + 4, 0xFFFFFF);

        GuiGameElement.of(menu.contentHolder).<GuiGameElement
            .GuiRenderBuilder>at(x + background0.width - 4, y + background0.height - 56, -200)
            .scale(5)
            .render(ms);
    }

    @Override
    protected void containerTick()
    {
        if (!menu.player.getMainHandItem()
            .equals(menu.contentHolder, false))
            menu.player.closeContainer();

        super.containerTick();
    }

    @Override
    protected void renderTooltip(PoseStack ms, int x, int y)
    {
        if (!menu.getCarried()
            .isEmpty() || this.hoveredSlot == null || this.hoveredSlot.hasItem()
            || hoveredSlot.container == menu.playerInventory)
        {
            super.renderTooltip(ms, x, y);
            return;
        }
        renderComponentTooltip(ms, addToTooltip(new LinkedList<>(), hoveredSlot.getSlotIndex()), x, y, font);
    }

    @Override
    public List<Component> getTooltipFromItem(ItemStack stack)
    {
        List<Component> list = super.getTooltipFromItem(stack);
        if (hoveredSlot.container == menu.playerInventory)
            return list;
        return hoveredSlot != null ? addToTooltip(list, hoveredSlot.getSlotIndex()) : list;
    }

    private List<Component> addToTooltip(List<Component> list, int slot)
    {
        if (slot < 0 || slot >= 50)
            return list;
        if (slot >= 30)
        {
            list.add(CreateTweakedControllers.translateDirect("tweaked_linked_controller.frequency_slot_" + ((slot % 2) + 1),
                GamepadInputs.GetAxisName((slot - 30) / 2)).withStyle(ChatFormatting.GOLD));
        }
        else
        {
            list.add(CreateTweakedControllers.translateDirect("tweaked_linked_controller.frequency_slot_" + ((slot % 2) + 1),
                GamepadInputs.GetButtonName(slot / 2)).withStyle(ChatFormatting.GOLD));
        }
        
        return list;
    }

    @Override
    public List<Rect2i> getExtraAreas()
    {
        return extraAreas;
    }

}
