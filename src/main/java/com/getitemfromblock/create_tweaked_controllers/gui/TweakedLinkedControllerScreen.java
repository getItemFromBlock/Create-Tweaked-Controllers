package com.getitemfromblock.create_tweaked_controllers.gui;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.getitemfromblock.create_tweaked_controllers.ControllerInputs;
import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedControlsUtil;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLinkedControllerMenu;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.container.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
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
	private IconButton firstTabButton;
	private IconButton secondTabButton;
	private JoystickIcon lStick;
	private JoystickIcon rStick;
	private boolean isSecondPage = false;
	private ControllerInputs inputs = new ControllerInputs();

	public TweakedLinkedControllerScreen(TweakedLinkedControllerMenu menu, Inventory inv, Component title)
	{
		super(menu, inv, title);
		this.background0 = ModGuiTextures.TWEAKED_LINKED_CONTROLLER_0;
		this.background1 = ModGuiTextures.TWEAKED_LINKED_CONTROLLER_1;
	}

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
		firstTabButton = new IconButton(x + 17, y + background0.height - 27, ModIcons.I_BUTTON);
		firstTabButton.withCallback(() -> {
			this.isSecondPage = false;
			menu.SetPage(this.isSecondPage);
		});
		secondTabButton = new IconButton(x + 42, y + background0.height - 27, ModIcons.I_AXES);
		secondTabButton.withCallback(() -> {
			this.isSecondPage = true;
			menu.SetPage(this.isSecondPage);
		});
		lStick = new JoystickIcon(x + 16, y + 38, ModIcons.I_LEFT_JOYSTICK);
		rStick = new JoystickIcon(x + 16, y + 101, ModIcons.I_RIGHT_JOYSTICK);

		addRenderableWidget(resetButton);
		addRenderableWidget(confirmButton);
		addRenderableWidget(firstTabButton);
		addRenderableWidget(secondTabButton);
		addRenderableWidget(lStick);
		addRenderableWidget(rStick);

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

		if (isSecondPage)
		{
			background1.render(ms, x, y, this);
			TweakedControlsUtil.GetControls(inputs);
			Vec2 v = new Vec2(inputs.axis[0], inputs.axis[1]);
			if (v.lengthSquared() > 1) v = v.normalized();
			lStick.move((int)(v.x * 10), (int)(v.y * 10));
			v = new Vec2(inputs.axis[2], inputs.axis[3]);
			if (v.lengthSquared() > 1) v = v.normalized();
			rStick.move((int)(v.x * 10), (int)(v.y * 10));
			lStick.visible = true;
			lStick.active = true;
			rStick.visible = true;
			rStick.active = true;
		}
		else
		{
			background0.render(ms, x, y, this);
			lStick.visible = false;
			lStick.active = false;
			rStick.visible = false;
			rStick.active = false;
		}
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
				ControllerInputs.GetAxisName((slot - 30) / 2)).withStyle(ChatFormatting.GOLD));
		}
		else
		{
			list.add(CreateTweakedControllers.translateDirect("tweaked_linked_controller.frequency_slot_" + ((slot % 2) + 1),
				ControllerInputs.GetButtonName(slot / 2)).withStyle(ChatFormatting.GOLD));
		}
		
		return list;
	}

	@Override
	public List<Rect2i> getExtraAreas()
	{
		return extraAreas;
	}

}
