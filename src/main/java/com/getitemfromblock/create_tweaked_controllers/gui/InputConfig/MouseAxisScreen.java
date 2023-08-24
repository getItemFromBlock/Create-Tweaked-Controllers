package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.input.MouseAxisInput;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MouseAxisScreen extends GenericInputScreen
{
    public MouseAxisInput source;
    private EditBox[] bounds;
    private EditBox valueRender;
    private Checkbox isYBox;
    private Checkbox useVelBox;
    private int boundsTextWidth = 0;

    public MouseAxisScreen(Screen parent, Component name, MouseAxisInput s)
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
        valueRender.setValue(String.format("%.01f", source.GetRawInput()));
        valueRender.setFocus(false);
        font.draw(ms, CreateTweakedControllers.translateDirect("gui_config_lower"), width / 2 - boundsTextWidth, height / 2 - 40, 0xaaaaaa);
        font.draw(ms, CreateTweakedControllers.translateDirect("gui_config_upper"),width / 2 - boundsTextWidth, height / 2 - 15, 0xaaaaaa);
        font.draw(ms, CreateTweakedControllers.translateDirect("gui_input_axis"), width / 2 - textwidth, height - 105, 0xaaaaaa);
        source.isYAxis = isYBox.selected();
        source.useVelocity = useVelBox.selected();
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
        bounds[0] = new EditBox(font, width / 2, height/2 - 45, 90, 20, CreateTweakedControllers.translateDirect("gui_config_lower"));
        bounds[0].setValue(GetSafeFloatString(source.minBound));
        bounds[1] = new EditBox(font, width / 2, height/2 - 20, 90, 20, CreateTweakedControllers.translateDirect("gui_config_upper"));
        bounds[1].setValue(GetSafeFloatString(source.maxBound));
        addRenderableWidgets(bounds);
        valueRender = new EditBox(font, width / 2, height - 110, 50, 20, CreateTweakedControllers.translateDirect("gui_input_axis"));
        valueRender.setEditable(false);
        valueRender.setTextColorUneditable(0xffffff);
        addRenderableWidget(valueRender);
        int l = Minecraft.getInstance().font.width(valueRender.getMessage()) + 10;
        if (l > textwidth) textwidth = l;
        isYBox = new Checkbox(width / 2 - 60, height/2 - 95, 100, 20,
            CreateTweakedControllers.translateDirect("gui_config_isyaxis"), source.isYAxis);
        addRenderableWidget(isYBox);
        useVelBox = new Checkbox(width / 2 - 60, height/2 - 70, 100, 20,
            CreateTweakedControllers.translateDirect("gui_config_usevelocity"), source.useVelocity);
        addRenderableWidget(useVelBox);
        boundsTextWidth = Math.max(font.width(CreateTweakedControllers.translateDirect("gui_config_lower")), font.width(CreateTweakedControllers.translateDirect("gui_config_upper")));
        boundsTextWidth += 10;
    }
    
}
