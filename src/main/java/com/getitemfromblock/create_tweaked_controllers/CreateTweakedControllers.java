package com.getitemfromblock.create_extended_controllers;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateExtendedControllers.ID)
@Mod.EventBusSubscriber
public class CreateExtendedControllers
{
    public static final String ID = "create_extended_controllers";

    private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(ID);

    public CreateExtendedControllers()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(this);
        eventBus.addListener(CreateExtendedControllers::init);
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
}
