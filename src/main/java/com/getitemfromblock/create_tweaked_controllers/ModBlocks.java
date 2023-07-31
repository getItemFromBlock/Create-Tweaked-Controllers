package com.getitemfromblock.create_extended_controllers;

import com.getitemfromblock.create_extended_controllers.controller.extended.ExtendedLecternControllerBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Blocks;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;

public class ModBlocks
{
    private static final
	CreateRegistrate REGISTRATE = CreateExtendedControllers.registrate().creativeModeTab(() -> ModTab.MOD_TAB);

    public static final BlockEntry<ExtendedLecternControllerBlock> EXTENDED_LECTERN_CONTROLLER =
		REGISTRATE.block("extended_lectern_controller", ExtendedLecternControllerBlock::new)
			.initialProperties(() -> Blocks.LECTERN)
			.transform(axeOnly())
			.blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
				.getExistingFile(p.mcLoc("block/lectern"))))
			.loot((lt, block) -> lt.dropOther(block, Blocks.LECTERN))
			.register();

            public static void register() {}
}