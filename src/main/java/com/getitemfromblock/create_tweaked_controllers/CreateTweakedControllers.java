package com.getitemfromblock.create_tweaked_controllers;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateTweakedControllers.ID)
@Mod.EventBusSubscriber
public class CreateTweakedControllers
{
    public static final String ID = "create_tweaked_controllers";

    private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(ID);

    public CreateTweakedControllers()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(this);
        eventBus.addListener(CreateTweakedControllers::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModClientStuff.onConstructor(eventBus, forgeEventBus));
        ModTab.register();
        ModBlocks.register();
        ModItems.register();
        ModBlockEntityTypes.register();
        ModMenuTypes.register();
    }

    public static void init(final FMLCommonSetupEvent event)
    {
		ModPackets.registerPackets();
	}

    public static CreateRegistrate registrate()
    {
        return REGISTRATE.get();
    }
    

    public static ResourceLocation asResource(String path)
    {
		return new ResourceLocation(ID, path);
	}

    public static MutableComponent translateDirect(String key, Object... args)
    {
		return Components.translatable(CreateTweakedControllers.ID + "." + key, Lang.resolveBuilders(args));
	}

    public static LangBuilder builder()
    {
		return new LangBuilder(CreateTweakedControllers.ID);
	}

    public static LangBuilder translate(String langKey, Object... args)
    {
		return builder().translate(langKey, args);
	}
}
