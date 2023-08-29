package com.getitemfromblock.create_tweaked_controllers.packet;

//import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.getitemfromblock.create_tweaked_controllers.CreateTweakedControllers;
import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import net.minecraftforge.network.simple.SimpleChannel;

public enum ModPackets
{
    TWEAKED_LINKED_CONTROLLER_INPUT(TweakedLinkedControllerButtonPacket.class, TweakedLinkedControllerButtonPacket::new, PLAY_TO_SERVER),
    TWEAKED_LINKED_CONTROLLER_INPUT_AXIS(TweakedLinkedControllerAxisPacket.class, TweakedLinkedControllerAxisPacket::new, PLAY_TO_SERVER),
    TWEAKED_LINKED_CONTROLLER_BIND(TweakedLinkedControllerBindPacket.class, TweakedLinkedControllerBindPacket::new, PLAY_TO_SERVER),
    TWEAKED_LINKED_CONTROLLER_USE_LECTERN(TweakedLinkedControllerStopLecternPacket.class, TweakedLinkedControllerStopLecternPacket::new,
        PLAY_TO_SERVER),
    ;

    public static final ResourceLocation CHANNEL_NAME = CreateTweakedControllers.asResource("main");
    public static final int NETWORK_VERSION = 2;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    public static SimpleChannel channel;

    private PacketType<?> packet;

    <T extends SimplePacketBase> ModPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
        NetworkDirection direction)
    {
        packet = new PacketType<>(type, factory, direction);
    }

    public static void registerPackets()
    {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
            .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
            .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
            .networkProtocolVersion(() -> NETWORK_VERSION_STR)
            .simpleChannel();
        for (ModPackets packet : values())
            packet.packet.register();
    }

    public static void sendToNear(Level world, BlockPos pos, int range, Object message)
    {
        channel.send(
            PacketDistributor.NEAR.with(TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())),
            message);
    }

    private static class PacketType<T extends SimplePacketBase>
    {
        private static int index = 0;

        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = (packet, contextSupplier) -> {
                Context context = contextSupplier.get();
                if (packet.handle(context))
                {
                    context.setPacketHandled(true);
                }
            };
            this.type = type;
            this.direction = direction;
        }

        private void register()
        {
            channel.messageBuilder(type, index++, direction)
                .encoder(encoder)
                .decoder(decoder)
                .consumer(handler)
                .add();
        }
    }
}
