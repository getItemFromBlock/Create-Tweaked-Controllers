package com.getitemfromblock.create_tweaked_controllers.gui.InputConfig;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedControlsUtil;
import com.getitemfromblock.create_tweaked_controllers.input.GenericInput;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.EditBox;

public abstract class GenericInputScreen extends AbstractSimiScreen
{
    protected final Screen parent;
    protected final GenericInput input;
    protected final Component inputName;
    protected EditBox[] outputs;
    protected int textwidth = 0;

    public GenericInputScreen(Screen p, Component name, GenericInput i)
    {
        parent = p;
        input = i;
        inputName = name;
    }

    @Override
    protected void init()
    {
        super.init();
        addRenderableWidget(new Button(width / 2 - 75, height - 29, 150, 20, CommonComponents.GUI_DONE, (p_193996_) -> {
            ScreenOpener.open(parent);
        }));
        outputs = new EditBox[2];
        outputs[0] = new EditBox(font, width / 2, height - 85, 50, 20, CreateTweakedControllers.translateDirect("gui_output_button"));
        outputs[0].setEditable(false);
        outputs[1] = new EditBox(font, width / 2, height - 60, 50, 20, CreateTweakedControllers.translateDirect("gui_output_axis"));
        outputs[1].setEditable(false);
        addRenderableWidgets(outputs);
        Minecraft mc = Minecraft.getInstance();
        for (int i = 0; i < 2; i++)
        {
            int l = mc.font.width(outputs[i].getMessage()) + 10;
            if (l > textwidth) textwidth = l;
        }
        Populate();
    }

    @Override
    public void tick()
    {
        for (int i = 0; i < 2; i++)
        {
            outputs[i].tick();
        }
        super.tick();
    }

    @Override
    protected void renderWindow(PoseStack ms, int x, int y, float partialTicks)
    {
        TweakedControlsUtil.GuiUpdate();
        for (int i = 0; i < 2; i++)
        {
            outputs[i].setFocus(false);
        }
        if (input.GetButtonValue())
        {
            outputs[0].setValue("True");
            outputs[0].setTextColorUneditable(0x44ff44);
        }
        else
        {
            outputs[0].setValue("False");
            outputs[0].setTextColorUneditable(0xff4444);
        }
        float val = input.GetAxisValue();
        outputs[1].setValue(String.format("%.03f", val));
        int col = Math.round(val * 0xbb);
        col = ((0xff - col) << 16) | ((0x44 + col) << 8) | 0x44;
        outputs[1].setTextColorUneditable(col);
        font.draw(ms, CreateTweakedControllers.translateDirect("gui_output_button"), width / 2 - textwidth, height - 80, 0xaaaaaa);
        font.draw(ms, CreateTweakedControllers.translateDirect("gui_output_axis"), width / 2 - textwidth, height - 55, 0xaaaaaa);
        Component comp = input.GetDisplayName();
        String name = inputName.getString() + " : " + comp.getString();
        int w = font.width(name);
        font.draw(ms, name, (width - w) / 2, 10, 0xffffff);
    }

    protected abstract void Populate();

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }

    public static float ParseFloatAndCorrectValue(EditBox box)
    {
        float result;
        try {
           result = Float.valueOf(box.getValue());
        } catch (NumberFormatException numberformatexception)
        {
           result = 0.0f;
           //box.setValue("0.0");
        }
        return result;
    }

    public String GetSafeFloatString(float input)
    {
        String output = String.format("%f", input);
        return output.replaceAll(",", ".");
    }
}
