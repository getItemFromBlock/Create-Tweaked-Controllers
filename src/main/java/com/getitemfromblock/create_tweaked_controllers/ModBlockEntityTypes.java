package com.getitemfromblock.create_extended_controllers;

import com.getitemfromblock.create_extended_controllers.controller.extended.ExtendedLecternControllerBlockEntity;
import com.getitemfromblock.create_extended_controllers.controller.extended.ExtendedLecternControllerRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntityTypes
{
    private static final CreateRegistrate REGISTRATE = CreateExtendedControllers.registrate();

    public static final BlockEntityEntry<ExtendedLecternControllerBlockEntity> EXTENDED_LECTERN_CONTROLLER = REGISTRATE
		.tileEntity("extended_lectern_controller", ExtendedLecternControllerBlockEntity::new)
		.validBlocks(ModBlocks.EXTENDED_LECTERN_CONTROLLER)
		.renderer(() -> ExtendedLecternControllerRenderer::new)
		.register();

	public static void register() {}
}
