package net.dndats.hackersandslashers.events;


import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.TickScheduler;
import net.dndats.hackersandslashers.client.overlay.DetectionOverlay;
import net.dndats.hackersandslashers.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, value = Dist.CLIENT)
public class OverlayEventHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void renderOverlay(RenderGuiEvent.Pre event) {
        DetectionOverlay.renderDetectionOverlay(event);
    }

}
