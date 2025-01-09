package net.dndats.hackersandslashers.events;


import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.overlay.DetectionOverlay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, value = Dist.CLIENT)
public class OverlayHandler {

    /**
     * Handles all events related to overlays
     */

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void renderOverlay(RenderGuiEvent.Pre event) {
        DetectionOverlay.renderDetectionOverlay(event);
    }

}
