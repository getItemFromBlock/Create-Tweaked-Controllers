package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.input.KeyboardInput;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class KeyboardInputScreen extends GenericInputScreen
{
    public KeyboardInput source;

    private Checkbox invertBox;
    private Checkbox springbackEnabledBox;

    private Checkbox isExponentialBox;
    private Checkbox useGlobalRateBox;

    private EditBox globalRateBox;
    private EditBox riseRateBox;
    private EditBox fallRateBox;
    private EditBox holdTimeBox;

    private int labelWidth = 0;

    public KeyboardInputScreen(Screen parent, Component name, KeyboardInput s)
    {
        super(parent, name, s);
        source = s;
    }

    @Override
    public void tick()
    {
        globalRateBox.tick();
        riseRateBox.tick();
        fallRateBox.tick();
        holdTimeBox.tick();
        super.tick();
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int x, int y, float partialTicks)
    {
        super.renderWindow(graphics, x, y, partialTicks);

        source.invertValue = invertBox.selected();
        source.springbackEnabled = springbackEnabledBox.selected();

        boolean sbEnabled = source.springbackEnabled;

        isExponentialBox.visible = sbEnabled;
        useGlobalRateBox.visible = sbEnabled;

        boolean exponential = isExponentialBox.selected();
        boolean useGlobal   = useGlobalRateBox.selected();

        source.springback.isExponential = exponential;
        source.springback.useGlobalRate = useGlobal;

        setBoxState(globalRateBox, sbEnabled && useGlobal);
        setBoxState(riseRateBox,   sbEnabled && !useGlobal);
        setBoxState(fallRateBox,   sbEnabled && !useGlobal);
        setBoxState(holdTimeBox,   sbEnabled);

        if (sbEnabled) {
            if (useGlobal) {
                source.springback.globalRate = ParseFloatAndCorrectValue(globalRateBox);
            } else {
                source.springback.riseRate = ParseFloatAndCorrectValue(riseRateBox);
                source.springback.fallRate = ParseFloatAndCorrectValue(fallRateBox);
            }
            source.springback.holdTime = ParseFloatAndCorrectValue(holdTimeBox);
        }

        int cx = width / 2;
        int startY = height / 2 - 115;

        if (sbEnabled && useGlobal) {
            graphics.drawString(font,
                CreateTweakedControllers.translateDirect("gui_config_springback_globalrate"),
                cx - labelWidth, startY + 105, 0xaaaaaa);
        }
        if (sbEnabled && !useGlobal) {
            graphics.drawString(font,
                CreateTweakedControllers.translateDirect("gui_config_springback_riserate"),
                cx - labelWidth, startY + 105, 0xaaaaaa);
            graphics.drawString(font,
                CreateTweakedControllers.translateDirect("gui_config_springback_fallrate"),
                cx - labelWidth, startY + 130, 0xaaaaaa);
        }
        if (sbEnabled) {
            graphics.drawString(font,
                CreateTweakedControllers.translateDirect("gui_config_springback_holdtime"),
                cx - labelWidth, startY + 155, 0xaaaaaa);
        }
    }

    private static void setBoxState(EditBox box, boolean active)
    {
        box.visible = active;
        box.setEditable(active);
        if (!active) box.setFocused(false);
    }

    @Override
    protected void Populate()
    {
        int cx = width / 2;
        int startY = height / 2 - 115;

        invertBox = new Checkbox(cx - 100, startY, 200, 20,
            CreateTweakedControllers.translateDirect("gui_config_invert"), source.invertValue);
        addRenderableWidget(invertBox);

        springbackEnabledBox = new Checkbox(cx - 100, startY + 25, 200, 20,
            CreateTweakedControllers.translateDirect("gui_config_springback_enabled"), source.springbackEnabled);
        addRenderableWidget(springbackEnabledBox);

        isExponentialBox = new Checkbox(cx - 100, startY + 50, 200, 20,
            CreateTweakedControllers.translateDirect("gui_config_springback_exponential"), source.springback.isExponential);
        addRenderableWidget(isExponentialBox);

        useGlobalRateBox = new Checkbox(cx - 100, startY + 75, 200, 20,
            CreateTweakedControllers.translateDirect("gui_config_springback_useglobalrate"), source.springback.useGlobalRate);
        addRenderableWidget(useGlobalRateBox);

        globalRateBox = new EditBox(font, cx, startY + 100, 90, 20,
            CreateTweakedControllers.translateDirect("gui_config_springback_globalrate"));
        globalRateBox.setValue(GetSafeFloatString(source.springback.globalRate));
        addRenderableWidget(globalRateBox);

        riseRateBox = new EditBox(font, cx, startY + 100, 90, 20,
            CreateTweakedControllers.translateDirect("gui_config_springback_riserate"));
        riseRateBox.setValue(GetSafeFloatString(source.springback.riseRate));
        addRenderableWidget(riseRateBox);

        fallRateBox = new EditBox(font, cx, startY + 125, 90, 20,
            CreateTweakedControllers.translateDirect("gui_config_springback_fallrate"));
        fallRateBox.setValue(GetSafeFloatString(source.springback.fallRate));
        addRenderableWidget(fallRateBox);

        holdTimeBox = new EditBox(font, cx, startY + 150, 90, 20,
            CreateTweakedControllers.translateDirect("gui_config_springback_holdtime"));
        holdTimeBox.setValue(GetSafeFloatString(source.springback.holdTime));
        addRenderableWidget(holdTimeBox);

        labelWidth = 0;
        String[] labelKeys = {
            "gui_config_springback_globalrate",
            "gui_config_springback_riserate",
            "gui_config_springback_fallrate",
            "gui_config_springback_holdtime"
        };
        for (String key : labelKeys) {
            int w = font.width(CreateTweakedControllers.translateDirect(key)) + 10;
            if (w > labelWidth) labelWidth = w;
        }
    }
}
