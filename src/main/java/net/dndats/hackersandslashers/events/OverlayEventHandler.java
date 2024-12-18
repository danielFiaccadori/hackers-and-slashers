package net.dndats.hackersandslashers.events;


import net.dndats.hackersandslashers.client.overlay.DetectionOverlay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber({Dist.CLIENT})
public class OverlayEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void renderOverlay(RenderGuiEvent.Pre event) {
        DetectionOverlay.renderDetectionOverlay(event);
    }

}
