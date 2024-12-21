package net.dndats.hackersandslashers.events;


import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.TickScheduler;
import net.dndats.hackersandslashers.client.overlay.DetectionOverlay;
import net.dndats.hackersandslashers.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, value = Dist.CLIENT)
public class OverlayEventHandler {

    private static final Map<UUID, Player> hiddenPlayers = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onPlayerDetectHiddenStatus(PlayerTickEvent.Pre event) {
        try {
            Player player = event.getEntity();
            UUID playerUUID = event.getEntity().getUUID();
            if (PlayerUtils.isHidden(player)) {
                hiddenPlayers.put(playerUUID, player);
                TickScheduler.schedule(() -> {
                    if (!PlayerUtils.isHidden(player)) {
                        hiddenPlayers.remove(playerUUID);
                    }
                }, 5);
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error at setting player detection status at OverlayEventHandler: {}", e.getMessage());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void renderOverlay(RenderGuiEvent.Pre event) {
        UUID playerUUID = Minecraft.getInstance().player.getUUID();
        boolean isHidden = hiddenPlayers.containsKey(playerUUID);
        DetectionOverlay.renderDetectionOverlay(event, isHidden);
    }

}
