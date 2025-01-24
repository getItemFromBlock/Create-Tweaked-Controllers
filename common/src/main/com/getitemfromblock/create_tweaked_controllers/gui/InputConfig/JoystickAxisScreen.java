package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.input.JoystickAxisInput;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class JoystickAxisScreen extends GenericInputScreen
{
    public JoystickAxisInput source;
    private EditBox[] bounds;
    private EditBox valueRender;
    private int boundsTextWidth = 0;

    public JoystickAxisScreen(Screen parent, Component name, JoystickAxisInput s)
    {
        super(parent, name, s);
        source = s;
    }

    @Override
    protected void renderWindow(PoseStack ms, int x, int y, float partialTicks)
    {
        super.renderWindow(ms, x, y, partialTicks);
        source.minBound = ParseFloatAndCorrectValue(bounds[0]);
        source.maxBound = ParseFloatAndCorrectValue(bounds[1]);
        if (bounds[0].isFocused()) bounds[1].setFocus(false);
        valueRender.setValue(String.format("%.03f", source.GetRawInput()));
        valueRender.setFocus(false);
        font.draw(ms, CreateTweakedControllers.translateDirect("gui_config_lower"), width / 2 - boundsTextWidth, height / 2 - 50, 0xaaaaaa);
        font.draw(ms, CreateTweakedControllers.translateDirect("gui_config_upper"),width / 2 - boundsTextWidth, height / 2 - 25, 0xaaaaaa);
        font.draw(ms, CreateTweakedControllers.translateDirect("gui_input_axis"), width / 2 - textwidth, height - 105, 0xaaaaaa);
    }

    @Override
    public void tick()
    {
        for (int i = 0; i < 2; i++)
        {
            bounds[i].tick();
        }
        super.tick();
    }

    @Override
    protected void Populate()
    {
        bounds = new EditBox[2];
        bounds[0] = new EditBox(font, width / 2, height/2 - 55, 90, 20, CreateTweakedControllers.translateDirect("gui_config_lower"));
        bounds[0].setValue(GetSafeFloatString(source.minBound));
        bounds[1] = new EditBox(font, width / 2, height/2 - 30, 90, 20, CreateTweakedControllers.translateDirect("gui_config_upper"));
        bounds[1].setValue(GetSafeFloatString(source.maxBound));
        addRenderableWidgets(bounds);
        valueRender = new EditBox(font, width / 2, height - 110, 50, 20, CreateTweakedControllers.translateDirect("gui_input_axis"));
        valueRender.setEditable(false);
        valueRender.setTextColorUneditable(0xffffff);
        addRenderableWidget(valueRender);
        int l = Minecraft.getInstance().font.width(valueRender.getMessage()) + 10;
        if (l > textwidth) textwidth = l;
        boundsTextWidth = Math.max(font.width(CreateTweakedControllers.translateDirect("gui_config_lower")), font.width(CreateTweakedControllers.translateDirect("gui_config_upper")));
        boundsTextWidth += 10;
    }
    
}
