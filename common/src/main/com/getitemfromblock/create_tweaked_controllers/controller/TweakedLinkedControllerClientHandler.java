package com.getitemfromblock.create_tweaked_controllers.controller;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.block.ModBlocks;
import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.config.ModClientConfig;
import com.getitemfromblock.create_tweaked_controllers.config.ModKeyMappings;
import com.getitemfromblock.create_tweaked_controllers.input.GamepadInputs;
import com.getitemfromblock.create_tweaked_controllers.input.MouseCursorHandler;
import com.getitemfromblock.create_tweaked_controllers.item.ModItems;
import com.getitemfromblock.create_tweaked_controllers.item.TweakedLinkedControllerItemRenderer;
import com.getitemfromblock.create_tweaked_controllers.packet.ModPackets;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerAxisPacket;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerBindPacket;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerButtonPacket;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerStopLecternPacket;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.ControlsUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class TweakedLinkedControllerClientHandler
{

    public static final IIngameOverlay OVERLAY = TweakedLinkedControllerClientHandler::renderOverlay;

    public static Mode MODE = Mode.IDLE;
    public static int PACKET_RATE = 5;
    public static short buttonStates = 0;
    public static int axisStates = 0;
    private static BlockPos lecternPos;
    private static BlockPos selectedLocation = BlockPos.ZERO;
    private static int buttonPacketCooldown = 0;
    private static int axisPacketCooldown = 0;
    private static boolean useLock = false;

    public static void toggleBindMode(BlockPos location)
    {
        if (MODE == Mode.IDLE)
        {
            MODE = Mode.BIND;
            selectedLocation = location;
            useLock = true;
        }
        else
        {
            MODE = Mode.IDLE;
            onReset();
        }
    }

    public static void toggle()
    {
        if (ModClientConfig.AUTO_RESET_MOUSE_FOCUS.get())
        {
            MouseCursorHandler.ResetCenter();
        }
        if (MODE == Mode.IDLE)
        {
            MODE = Mode.ACTIVE;
            lecternPos = null;
        }
        else
        {
            MODE = Mode.IDLE;
            onReset();
        }
    }

    public static void activateInLectern(BlockPos lecternAt)
    {
        if (ModClientConfig.AUTO_RESET_MOUSE_FOCUS.get())
        {
            MouseCursorHandler.ResetCenter();
        }
        if (MODE == Mode.IDLE)
        {
            MODE = Mode.ACTIVE;
            lecternPos = lecternAt;
        }
    }

    public static void deactivateInLectern()
    {
        if (ModClientConfig.AUTO_RESET_MOUSE_FOCUS.get())
        {
            MouseCursorHandler.ResetCenter();
        }
        if (MODE == Mode.ACTIVE && inLectern())
        {
            MODE = Mode.IDLE;
            onReset();
        }
    }

    public static boolean inLectern()
    {
        return lecternPos != null;
    }

    protected static void onReset()
    {
        TweakedControlsUtil.FreeFocus();
        MouseCursorHandler.DeactivateMouseLock(); // Make sure to free the camera when exiting the controller
        selectedLocation = BlockPos.ZERO;
        buttonPacketCooldown = 0;
        axisPacketCooldown = 0;
        if (inLectern())
            ModPackets.channel.sendToServer(new TweakedLinkedControllerStopLecternPacket(lecternPos));
        lecternPos = null;

        if (buttonStates != 0)
        {
            buttonStates = 0;
            ModPackets.channel.sendToServer(new TweakedLinkedControllerButtonPacket(buttonStates));
        }
        axisStates = 0;
        ModPackets.channel.sendToServer(new TweakedLinkedControllerAxisPacket(axisStates, null));
        TweakedLinkedControllerItemRenderer.resetButtons();
    }

    public static void tick()
    {
        TweakedLinkedControllerItemRenderer.earlyTick();
        if (MODE == Mode.IDLE)
            return;
        if (buttonPacketCooldown > 0)
            buttonPacketCooldown--;
        if (axisPacketCooldown > 0)
            axisPacketCooldown--;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ItemStack heldItem = player.getMainHandItem();

        if (player.isSpectator())
        {
            MODE = Mode.IDLE;
            onReset();
            return;
        }

        if (!inLectern() && !ModItems.TWEAKED_LINKED_CONTROLLER.isIn(heldItem))
        {
            heldItem = player.getOffhandItem();
            if (!ModItems.TWEAKED_LINKED_CONTROLLER.isIn(heldItem))
            {
                MODE = Mode.IDLE;
                onReset();
                return;
            }
        }

        if (inLectern() && ModBlocks.TWEAKED_LECTERN_CONTROLLER.get()
            .getBlockEntityOptional(mc.level, lecternPos)
            .map(be -> !be.isUsedBy(mc.player))
            .orElse(true))
        {
            deactivateInLectern();
            return;
        }

        if (mc.screen != null)
        {
            MODE = Mode.IDLE;
            onReset();
            return;
        }

        if (ModKeyMappings.KEY_CONTROLLER_EXIT.isDown() || InputConstants.isKeyDown(mc.getWindow()
            .getWindow(), GLFW.GLFW_KEY_ESCAPE))
        {
            MODE = Mode.IDLE;
            onReset();
            return;
        }
        boolean useFullPrec = lecternPos != null && ((TweakedLecternControllerBlockEntity)Minecraft.getInstance().level.getBlockEntity(lecternPos)).shouldUseFullPrecision();
        TweakedControlsUtil.Update(useFullPrec);
        TweakedLinkedControllerItemRenderer.tick();

        if (MODE == Mode.ACTIVE)
        {
            short pressedKeys = TweakedControlsUtil.output.EncodeButtons();
            if (pressedKeys != buttonStates)
            {
                if ((pressedKeys & ~buttonStates) != 0)
                {
                    AllSoundEvents.CONTROLLER_CLICK.playAt(player.level, player.blockPosition(), 1f, .75f, true);
                }
                if ((buttonStates & ~pressedKeys) != 0)
                {
                    AllSoundEvents.CONTROLLER_CLICK.playAt(player.level, player.blockPosition(), 1f, .5f, true);
                }
                ModPackets.channel.sendToServer(new TweakedLinkedControllerButtonPacket(pressedKeys, lecternPos));
                buttonPacketCooldown = PACKET_RATE;
            }
            if (buttonPacketCooldown == 0 && pressedKeys != 0)
            {
                ModPackets.channel.sendToServer(new TweakedLinkedControllerButtonPacket(pressedKeys, lecternPos));
                buttonPacketCooldown = PACKET_RATE;
            }
            buttonStates = pressedKeys;
            int axis = TweakedControlsUtil.output.EncodeAxis();
            if (useFullPrec)
            {
                ModPackets.channel.sendToServer(new TweakedLinkedControllerAxisPacket(TweakedControlsUtil.output.fullAxis, axis, lecternPos));
            }
            else
            {
                if (axis != axisStates)
                {
                    ModPackets.channel.sendToServer(new TweakedLinkedControllerAxisPacket(axis, lecternPos));
                    axisPacketCooldown = PACKET_RATE;
                }
                if (axisPacketCooldown == 0 && axis != 0)
                {
                    ModPackets.channel.sendToServer(new TweakedLinkedControllerAxisPacket(axis, lecternPos));
                    axisPacketCooldown = PACKET_RATE;
                }
                axisStates = axis;
            }
        }

        if (MODE == Mode.BIND)
        {
            VoxelShape shape = mc.level.getBlockState(selectedLocation)
                .getShape(mc.level, selectedLocation);
            if (!shape.isEmpty())
                CreateClient.OUTLINER.showAABB("controller", shape.bounds()
                    .move(selectedLocation))
                    .colored(0x0104FF)
                    .lineWidth(1 / 16f);
            if (!ControlsUtil.isActuallyPressed(Minecraft.getInstance().options.keyUse)) useLock = false;
            if (useLock) return;
            for (int i = 0; i < 15; i++)
            {
                if (!GamepadInputs.buttons[i]) continue;
                LinkBehaviour linkBehaviour = BlockEntityBehaviour.get(mc.level, selectedLocation, LinkBehaviour.TYPE);
                if (linkBehaviour != null)
                {
                    ModPackets.channel.sendToServer(new TweakedLinkedControllerBindPacket(i, selectedLocation));
                    CreateTweakedControllers.translate("tweaked_linked_controller.key_bound", GamepadInputs.GetButtonName(i)).sendStatus(mc.player);
                }
                MODE = Mode.IDLE;
                onReset();
                break;
            }
            for (int i = 0; i < 6; i++)
            {
                if ((i < 4 && Math.abs(GamepadInputs.axis[i]) > 0.8f) || (i >= 4 && GamepadInputs.axis[i] > 0))
                {
                    LinkBehaviour linkBehaviour = BlockEntityBehaviour.get(mc.level, selectedLocation, LinkBehaviour.TYPE);
                    if (linkBehaviour != null)
                    {
                        int a = i >= 4 ? i + 4 : i * 2 + (GamepadInputs.axis[i] < 0 ? 1 : 0);
                        ModPackets.channel.sendToServer(new TweakedLinkedControllerBindPacket(a + 15, selectedLocation));
                        CreateTweakedControllers.translate("tweaked_linked_controller.key_bound", GamepadInputs.GetAxisName(a)).sendStatus(mc.player);
                    }
                    MODE = Mode.IDLE;
                    onReset();
                    break;
                }
            }
        }
    }

    public static void renderOverlay(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width1,
        int height1) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui)
            return;

        if (MODE != Mode.BIND)
            return;

        poseStack.pushPose();
        Screen tooltipScreen = new Screen(Components.immutableEmpty()) {};
        tooltipScreen.init(mc, width1, height1);

        List<Component> list = new ArrayList<>();
        list.add(CreateTweakedControllers.translateDirect("tweaked_linked_controller.bind_mode")
            .withStyle(ChatFormatting.GOLD));
        
        list.add(CreateTweakedControllers.translateDirect("tweaked_linked_controller.press_keybind")
            .withStyle(ChatFormatting.GRAY));

        int width = 0;
        int height = list.size() * mc.font.lineHeight;
        for (Component iTextComponent : list)
            width = Math.max(width, mc.font.width(iTextComponent));
        int x = (width1 / 3) - width / 2;
        int y = height1 - height - 24;

        // TODO
        tooltipScreen.renderComponentTooltip(poseStack, list, x, y);

        poseStack.popPose();
    }

    public enum Mode
    {
        IDLE,
        ACTIVE,
        BIND
    }

}
