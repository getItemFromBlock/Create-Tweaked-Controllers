package com.getitemfromblock.create_tweaked_controllers.gui;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.config.ModClientConfig;
import com.simibubi.create.foundation.config.ui.ConfigScreen;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ModConfigScreen extends AbstractSimiScreen
{
    protected final Screen parent;
    protected boolean returnOnClose;
    protected Button advancedConfigButton;

    public ModConfigScreen(Screen parent)
    {
        this.parent = parent;
        this.returnOnClose = true;
    }

    @Override
    protected void init()
    {
        super.init();
        this.returnOnClose = true;
        this.Populate();
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        if (ModClientConfig.USE_CUSTOM_MAPPINGS.get())
        {
            advancedConfigButton.active = true;
            advancedConfigButton.setTooltip(null);
        }
        else
        {
            advancedConfigButton.active = false;
            advancedConfigButton.setTooltip(Tooltip.create(CreateTweakedControllers.translateDirect("menu.config_disabled").withStyle(s -> s.withColor(0xFC785C).withBold(true))));
        }
    }

    private void Populate()
    {
        int yStart = height / 4 + 40;
        int center = width / 2;
        int bHeight = 20;
        int bShortWidth = 98;
        int bLongWidth = 200;
        addRenderableWidget(Button.builder(CreateTweakedControllers.translateDirect("menu.return", new Object[0]), ($) -> {
            this.linkTo(parent);
        }).bounds(center - 100, yStart + 92, bLongWidth, bHeight).build());
        addRenderableWidget(Button.builder(CreateTweakedControllers.translateDirect("menu.config_general", new Object[0]), ($) -> {
            ConfigScreen.modID = CreateTweakedControllers.ID;
            this.linkTo((Screen)(new SubMenuConfigScreen(this, Type.CLIENT, ModClientConfig.SPEC)));
        }).bounds(center - 100, yStart + 8, bLongWidth, bHeight).build());
        advancedConfigButton = new Button.Builder(CreateTweakedControllers.translateDirect("menu.config_controller", new Object[0]), ($) -> {
            this.linkTo((Screen)(new ModControllerConfigScreen(this)));
        }).bounds(center - 100, yStart + 32, bLongWidth, bHeight).build();
        addRenderableWidget(advancedConfigButton);
        addRenderableWidget(Button.builder(CreateTweakedControllers.translateDirect("menu.issues", new Object[0]), ($) -> {
            this.linkTo("https://github.com/getItemFromBlock/Create-Tweaked-Controllers/issues");
        }).bounds(center + 2, yStart + 68, bShortWidth, bHeight).build());
        addRenderableWidget(Button.builder(CreateTweakedControllers.translateDirect("menu.wiki", new Object[0]), ($) -> {
            this.linkTo("https://github.com/getItemFromBlock/Create-Tweaked-Controllers/wiki");
        }).bounds(center - 100, yStart + 68, bShortWidth, bHeight).build());
    }

    private void linkTo(Screen screen)
    {
        returnOnClose = false;
        ScreenOpener.open(screen);
    }

   private void linkTo(String url)
   {
        returnOnClose = false;
        ScreenOpener.open(new ConfirmLinkScreen((p) -> {
            if (p)
            {
                Util.getPlatform().openUri(url);
            }
            this.minecraft.setScreen(this);
        }, url, true));
    }

    public boolean isPauseScreen()
    {
        return true;
    }
        
}
