package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import java.util.Collections;
import java.util.List;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedControlsUtil;
import com.getitemfromblock.create_tweaked_controllers.gui.ModControllerConfigScreen;
import com.getitemfromblock.create_tweaked_controllers.input.GamepadInputs;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InputList extends ContainerObjectSelectionList<InputList.Entry>
{
    final ModControllerConfigScreen modControllerConfigScreen;
    int maxNameWidth;

    public InputList(ModControllerConfigScreen screen, Minecraft mc)
    {
        super(mc, screen.width + 45, screen.height, 120, screen.height - 32, 20);
        modControllerConfigScreen = screen;
        addEntry(new InputList.CategoryEntry(CreateTweakedControllers.translateDirect("gui_gamepad_buttons")));
        for (int i = 0; i < 15; i++)
        {
            Component c = GamepadInputs.GetButtonName(i);
            int l = mc.font.width(c);
            if (l > maxNameWidth)
            {
                maxNameWidth = l;
            }
            addEntry(new InputList.InputEntry(i, c, screen));
        }
        addEntry(new InputList.CategoryEntry(CreateTweakedControllers.translateDirect("gui_gamepad_axis")));
        for (int i = 0; i < 10; i++)
        {
            Component c = GamepadInputs.GetAxisName(i);
            int l = mc.font.width(c);
            if (l > maxNameWidth)
            {
                maxNameWidth = l;
            }
            addEntry(new InputList.InputEntry(i+15, c, screen));
        }
    }

    protected int getScrollbarPosition()
    {
        return super.getScrollbarPosition() + 15 + 120;
    }

    public int getRowWidth()
    {
        return super.getRowWidth() + 32;
    }

    @OnlyIn(Dist.CLIENT)
    public class CategoryEntry extends InputList.Entry
    {
        final Component name;
        private final int width;

        public CategoryEntry(Component comp)
        {
            name = comp;
            width = InputList.this.minecraft.font.width(name);
        }

        public void render(PoseStack ms, int p_193889_, int p_193890_, int p_193891_, int p_193892_,
            int p_193893_, int p_193894_, int p_193895_, boolean p_193896_, float p_193897_)
        {
            InputList.this.minecraft.font.draw(ms, name,
                    (float) (InputList.this.minecraft.screen.width / 2 - width / 2),
                    (float) (p_193890_ + p_193893_ - 9 - 1), 16777215);
        }

        public boolean changeFocus(boolean value)
        {
            return false;
        }

        public List<? extends GuiEventListener> children()
        {
            return Collections.emptyList();
        }

        public List<? extends NarratableEntry> narratables()
        {
            return ImmutableList.of(new NarratableEntry()
            {
                public NarratableEntry.NarrationPriority narrationPriority()
                {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                public void updateNarration(NarrationElementOutput output)
                {
                    output.add(NarratedElementType.TITLE, CategoryEntry.this.name);
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<InputList.Entry>
    {
    }

    @OnlyIn(Dist.CLIENT)
    public class InputEntry extends InputList.Entry
    {
        private final int key;
        private final Component name;
        private final Button changeButton;
        private final Button resetButton;
        private final Button configButton;

        InputEntry(final int input, Component text, Screen parent)
        {
            key = input;
            name = text;
            changeButton = new Button(0, 0, 95, 20, name, (b) -> {
                InputList.this.modControllerConfigScreen.SetActiveInput(input);
            }) {
                protected MutableComponent createNarrationMessage()
                {
                    return (TweakedControlsUtil.profile.layout[key] != null) ? new TranslatableComponent("narrator.controls.unbound", name)
                            : new TranslatableComponent("narrator.controls.bound", name,
                                    super.createNarrationMessage());
                }
            };
            resetButton = new Button(0, 0, 50, 20, CreateTweakedControllers.translateDirect("gui_config_reset"), (b) -> {
                TweakedControlsUtil.profile.layout[key] = null;
                TweakedControlsUtil.profile.UpdateProfileData();
            }) {
                protected MutableComponent createNarrationMessage()
                {
                    return new TranslatableComponent("narrator.controls.reset", name);
                }
            };
            configButton = new Button(0, 0, 50, 20, CreateTweakedControllers.translateDirect("gui_config_config"), (b) -> {
                ScreenOpener.open(TweakedControlsUtil.profile.layout[key].OpenConfigScreen(parent, name));
            }) {
                protected MutableComponent createNarrationMessage()
                {
                    return new TranslatableComponent("narrator.controls.reset", name);
                }
            };
        }

        public void render(PoseStack p_193923_, int p_193924_, int p_193925_, int p_193926_, int p_193927_,
                int p_193928_, int p_193929_, int p_193930_, boolean p_193931_, float p_193932_)
        {
            float f = (float) (p_193926_ + 40 - InputList.this.maxNameWidth);
            InputList.this.minecraft.font.draw(p_193923_, name, f, (float) (p_193925_ + p_193928_ / 2 - 9 / 2),
                    16777215);
                    boolean active = TweakedControlsUtil.profile.layout[key] != null;
            resetButton.x = p_193926_ + 155;
            resetButton.y = p_193925_;
            resetButton.active = active;
            resetButton.render(p_193923_, p_193929_, p_193930_, p_193932_);
            configButton.x = p_193926_ + 210;
            configButton.y = p_193925_;
            configButton.active = active;
            configButton.render(p_193923_, p_193929_, p_193930_, p_193932_);
            changeButton.x = p_193926_ + 55;
            changeButton.y = p_193925_;
            changeButton.setMessage((
                active ?
                TweakedControlsUtil.profile.layout[key].GetDisplayName() :
                CreateTweakedControllers.translateDirect("gui_config_none")
                ));
            if (InputList.this.modControllerConfigScreen.GetActiveInput() == key)
            {
                changeButton.setMessage((new TextComponent("> "))
                        .append(changeButton.getMessage().copy().withStyle(ChatFormatting.YELLOW)).append(" <")
                        .withStyle(ChatFormatting.YELLOW));
            }
            else if (!active || !TweakedControlsUtil.profile.layout[key].IsInputValid())
            {
                changeButton.setMessage(changeButton.getMessage().copy()
                        .withStyle(active ? ChatFormatting.RED : ChatFormatting.DARK_AQUA));
            }

            changeButton.render(p_193923_, p_193929_, p_193930_, p_193932_);
        }

        public List<? extends GuiEventListener> children()
        {
            return ImmutableList.of(changeButton, resetButton, configButton);
        }

        public List<? extends NarratableEntry> narratables()
        {
            return ImmutableList.of(changeButton, resetButton, configButton);
        }

        public boolean mouseClicked(double p_193919_, double p_193920_, int p_193921_)
        {
            if (changeButton.mouseClicked(p_193919_, p_193920_, p_193921_))
            {
                return true;
            }
            else
            {
                return resetButton.mouseClicked(p_193919_, p_193920_, p_193921_)
                    || configButton.mouseClicked(p_193919_, p_193920_, p_193921_);
            }
        }

        public boolean mouseReleased(double p_193941_, double p_193942_, int p_193943_)
        {
            return changeButton.mouseReleased(p_193941_, p_193942_, p_193943_)
                    || resetButton.mouseReleased(p_193941_, p_193942_, p_193943_)
                    || configButton.mouseReleased(p_193941_, p_193942_, p_193943_);
        }
    }
}
