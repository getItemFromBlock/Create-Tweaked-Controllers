package com.getitemfromblock.create_tweaked_controllers.block;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Blocks;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.getitemfromblock.create_tweaked_controllers.ModTab;

public class ModBlocks
{

    static
    {
        CreateTweakedControllers.registrate().setCreativeTab(ModTab.MOD_TAB);
    }

    public static final BlockEntry<TweakedLecternControllerBlock> TWEAKED_LECTERN_CONTROLLER =
        CreateTweakedControllers.registrate().block("tweaked_lectern_controller", TweakedLecternControllerBlock::new)
            .initialProperties(() -> Blocks.LECTERN)
            .transform(axeOnly())
            .blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
                .getExistingFile(p.mcLoc("block/lectern"))))
            .loot((lt, block) -> lt.dropOther(block, Blocks.LECTERN))
            .register();

    public static void register() {}
}