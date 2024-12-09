package net.dndats.hackersandslashers.network;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.ModPlayerData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                PlayerBlockPacket.TYPE,
                PlayerBlockPacket.STREAM_CODEC,
                NetworkHandler::handlePlayerBlockPacket
        );
    }

    private static void handlePlayerBlockPacket(PlayerBlockPacket packet, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        player.setData(ModPlayerData.IS_BLOCKING, packet.isBlocking());
        HackersAndSlashers.LOGGER.info("Player {} set blocking state to: {}", player.getDisplayName(), packet.isBlocking());
    }

}
