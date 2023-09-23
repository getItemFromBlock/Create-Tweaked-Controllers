package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.block.ModBlocks;
import com.getitemfromblock.create_tweaked_controllers.compat.ComputerCraft.ModComputerCraftProxy;
import com.getitemfromblock.create_tweaked_controllers.config.ModConfigs;
import com.getitemfromblock.create_tweaked_controllers.gui.ModMenuTypes;
import com.getitemfromblock.create_tweaked_controllers.item.ModItems;
import com.getitemfromblock.create_tweaked_controllers.packet.ModPackets;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateTweakedControllers.ID)
@Mod.EventBusSubscriber
public class CreateTweakedControllers
{
    public static final String ID = "create_tweaked_controllers";
    public static final String NAME = "Create: Tweaked Controllers";

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);

    public CreateTweakedControllers()
    {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(this);
        eventBus.addListener(CreateTweakedControllers::init);
        REGISTRATE.registerEventListeners(eventBus);
        ModTab.register();
        ModItems.register();
        ModBlocks.register();
        ModBlockEntityTypes.register();
        ModMenuTypes.register();
        ModConfigs.register(modLoadingContext);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModClientStuff.onConstructor(eventBus, forgeEventBus));
        ModComputerCraftProxy.register();
    }

    public static void init(final FMLCommonSetupEvent event)
    {
        ModPackets.registerPackets();
    }

    public static CreateRegistrate registrate()
    {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path)
    {
        return new ResourceLocation(ID, path);
    }

    public static MutableComponent translateDirect(String key, Object... args)
    {
        return Components.translatable(CreateTweakedControllers.ID + "." + key, Lang.resolveBuilders(args));
    }

    public static MutableComponent translateDirectRaw(String key, Object... args)
    {
        return Components.translatable(key, Lang.resolveBuilders(args));
    }

    public static LangBuilder builder()
    {
        return new LangBuilder(CreateTweakedControllers.ID);
    }

    public static LangBuilder translate(String langKey, Object... args)
    {
        return builder().translate(langKey, args);
    }

    public static void log(String message)
    {
        Create.LOGGER.info(message);
    }
}
