package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;

public abstract class GenericInputScreen extends AbstractSimiScreen
{
    protected final Screen parent;

    public GenericInputScreen(Screen parent)
    {
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        super.init();
        addRenderableWidget(new Button(this.width / 2 - 75, this.height - 29, 150, 20, CommonComponents.GUI_DONE, (p_193996_) -> {
            ScreenOpener.open(parent);
        }));
        Populate();
    }

    protected abstract void Populate();

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
}
