package com.getitemfromblock.create_tweaked_controllers;

import com.getitemfromblock.create_tweaked_controllers.block.ModBlocks;
import com.getitemfromblock.create_tweaked_controllers.compat.ComputerCraft.ModComputerCraftProxy;
import com.getitemfromblock.create_tweaked_controllers.config.ModConfigs;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import com.getitemfromblock.create_tweaked_controllers.gui.ModMenuTypes;
import com.getitemfromblock.create_tweaked_controllers.item.ModDataComponents;
import com.getitemfromblock.create_tweaked_controllers.item.ModItems;
import com.getitemfromblock.create_tweaked_controllers.packet.ModPackets;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.createmod.catnip.lang.LangBuilder;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(CreateTweakedControllers.ID)
public class CreateTweakedControllers
{
    public static final String ID = "create_tweaked_controllers";
    public static final String NAME = "Create: Tweaked Controllers";

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID)
            .defaultCreativeTab((net.minecraft.resources.ResourceKey<CreativeModeTab>) null);

    public CreateTweakedControllers(IEventBus eventBus, ModContainer container)
    {
        IEventBus forgeEventBus = NeoForge.EVENT_BUS;
        REGISTRATE.registerEventListeners(eventBus);
        ModTab.register(eventBus);
        ModItems.register();
        ModBlocks.register();
        ModBlockEntityTypes.register();
        ModMenuTypes.register();
        ModConfigs.register(container);
        ModDataComponents.register(eventBus);
        ModPackets.registerBusListener(eventBus);
        if (FMLEnvironment.dist.isClient())
            ModClientStuff.onConstructor(eventBus);
        ModComputerCraftProxy.register();
        eventBus.addListener(this::onRegisterCapabilities);
    }

    private void onRegisterCapabilities(final RegisterCapabilitiesEvent event)
    {
        ModComputerCraftProxy.registerCapabilities(event);
    }

    public static CreateRegistrate registrate()
    {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static MutableComponent translateDirect(String key, Object... args)
    {
        return Component.translatable(CreateTweakedControllers.ID + "." + key, LangBuilder.resolveBuilders(args));
    }

    public static MutableComponent translateDirectRaw(String key, Object... args)
    {
        return Component.translatable(key, LangBuilder.resolveBuilders(args));
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

    public static void error(String message)
    {
        Create.LOGGER.error(message);
    }
}
