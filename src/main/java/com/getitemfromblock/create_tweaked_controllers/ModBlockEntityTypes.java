package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.block.ModBlocks;
import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.controller.TweakedLecternControllerRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntityTypes
{
    private static final CreateRegistrate REGISTRATE = CreateTweakedControllers.registrate();

    public static final BlockEntityEntry<TweakedLecternControllerBlockEntity> TWEAKED_LECTERN_CONTROLLER = REGISTRATE
        .blockEntity("tweaked_lectern_controller", TweakedLecternControllerBlockEntity::new)
        .validBlocks(ModBlocks.TWEAKED_LECTERN_CONTROLLER)
        .renderer(() -> TweakedLecternControllerRenderer::new)
        .register();

    public static void register() {}
}
