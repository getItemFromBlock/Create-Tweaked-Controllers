package com.getitemfromblock.create_tweaked_controllers.gui;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.config.ui.BaseConfigScreen;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;

public class ModConfigScreen extends AbstractSimiScreen
{
    protected final Screen parent;
    protected boolean returnOnClose;

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
    protected void renderWindow(PoseStack ms, int mouseX, int mouseY, float partialTicks)
    {
        
    }

    private void Populate()
    {
        int yStart = this.height / 4 + 40;
        int center = this.width / 2;
        int bHeight = 20;
        int bShortWidth = 98;
        int bLongWidth = 200;
        this.addRenderableWidget(new Button(center - 100, yStart + 92, bLongWidth, bHeight, CreateTweakedControllers.translateDirect("menu.return", new Object[0]), ($) -> {
            this.linkTo(this.parent);
        }));
        this.addRenderableWidget(new Button(center - 100, yStart + 8, bLongWidth, bHeight, CreateTweakedControllers.translateDirect("menu.config_general", new Object[0]), ($) -> {
            this.linkTo((Screen)(new BaseConfigScreen(this, CreateTweakedControllers.ID)));
        }));
        this.addRenderableWidget(new Button(center - 100, yStart + 32, bLongWidth, bHeight, CreateTweakedControllers.translateDirect("menu.config_controller", new Object[0]), ($) -> {
            this.linkTo((Screen)(new ModControllerConfigScreen(this)));
        }));
        this.addRenderableWidget(new Button(center + 2, yStart + 68, bShortWidth, bHeight, CreateTweakedControllers.translateDirect("menu.issues", new Object[0]), ($) -> {
            this.linkTo("https://github.com/getItemFromBlock/Create-Tweaked-Controllers/issues");
        }));
        this.addRenderableWidget(new Button(center - 100, yStart + 68, bShortWidth, bHeight, CreateTweakedControllers.translateDirect("menu.wiki", new Object[0]), ($) -> {
            this.linkTo("https://github.com/getItemFromBlock/Create-Tweaked-Controllers/wiki");
        }));
   }

   private void linkTo(Screen screen)
   {
      this.returnOnClose = false;
      ScreenOpener.open(screen);
   }

   private void linkTo(String url)
   {
      this.returnOnClose = false;
      ScreenOpener.open(new ConfirmLinkScreen((p_213069_2_) -> {
         if (p_213069_2_) {
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
