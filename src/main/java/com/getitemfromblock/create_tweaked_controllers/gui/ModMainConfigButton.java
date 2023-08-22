package com.getitemfromblock.create_tweaked_controllers.gui;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableObject;

import com.getitemfromblock.create_tweaked_controllers.config.ModClientConfig;
import com.getitemfromblock.create_tweaked_controllers.item.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.Components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class ModMainConfigButton extends Button
{
    public static final ItemStack ICON = ModItems.TWEAKED_LINKED_CONTROLLER.asStack(); // TODO

    public ModMainConfigButton(int x, int y)
    {
        super(x, y, 20, 20, Components.immutableEmpty(), ModMainConfigButton::click);
    }

    @Override
    public void renderBg(PoseStack ms, Minecraft mc, int mouseX, int mouseY)
    {
        Minecraft.getInstance().getItemRenderer().renderGuiItem(ICON, x + 2, y + 2);
    }

    public static void click(Button b)
    {
        ScreenOpener.open(new ModConfigScreen(Minecraft.getInstance().screen));
    }

    public static class SingleMenuRow
    {
        public final String left, right;
        public SingleMenuRow(String left, String right)
        {
            this.left = I18n.get(left);
            this.right = I18n.get(right);
        }
        public SingleMenuRow(String center) {
            this(center, center);
        }
    }

    public static class MenuRows
    {
        protected final List<String> leftButtons, rightButtons;

        public MenuRows(List<SingleMenuRow> variants)
        {
            leftButtons = variants.stream().map(r -> r.left).collect(Collectors.toList());
            rightButtons = variants.stream().map(r -> r.right).collect(Collectors.toList());
        }

        public static MenuRows CreateMainMenuRows()
        {
            return new MenuRows(Arrays.asList(
            new SingleMenuRow("menu.singleplayer"),
            new SingleMenuRow("menu.multiplayer"),
            new SingleMenuRow("fml.menu.mods", "menu.online"),
            new SingleMenuRow("narrator.button.language", "narrator.button.accessibility")
            ));
        }

        public static MenuRows CreateIngameMenuRows()
        {
            return new MenuRows(Arrays.asList(
            new SingleMenuRow("menu.returnToGame"),
            new SingleMenuRow("gui.advancements", "gui.stats"),
            new SingleMenuRow("menu.sendFeedback", "menu.reportBugs"),
            new SingleMenuRow("menu.options", "menu.shareToLan"),
            new SingleMenuRow("menu.returnToMenu")
        ));
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT)
    public static class OpenConfigButtonHandler
    {

        @SubscribeEvent
        public static void onGuiInit(ScreenEvent.InitScreenEvent event) {
            Screen gui = event.getScreen();

            MenuRows menu = null;
            int rowIdx = 0, offsetX = 0;
            if (gui instanceof TitleScreen)
            {
                menu = MenuRows.CreateMainMenuRows();
                rowIdx = ModClientConfig.CONFIG_BUTTON_MAIN_MENU_ROW.get();
                offsetX = ModClientConfig.CONFIG_BUTTON_MAIN_MENU_OFFSET.get();
            }
            else if (gui instanceof PauseScreen)
            {
                menu = MenuRows.CreateIngameMenuRows();
                rowIdx = ModClientConfig.CONFIG_BUTTON_INGAME_MENU_ROW.get();
                offsetX = ModClientConfig.CONFIG_BUTTON_INGAME_MENU_OFFSET.get();
            }

            if (rowIdx != 0 && menu != null)
            {
                boolean onLeft = offsetX < 0;
                String target = (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);

                int offsetX_ = offsetX;
                MutableObject<GuiEventListener> toAdd = new MutableObject<>(null);
                event.getListenersList()
                    .stream()
                    .filter(w -> w instanceof AbstractWidget)
                    .map(w -> (AbstractWidget) w)
                    .filter(w -> w.getMessage()
                        .getString()
                        .equals(target))
                    .findFirst()
                    .ifPresent(w -> toAdd
                        .setValue(new ModMainConfigButton(w.x + offsetX_ + (onLeft ? -20 : w.getWidth()), w.y)));
                if (toAdd.getValue() != null)
                    event.addListener(toAdd.getValue());
            }
        }

    }

}
