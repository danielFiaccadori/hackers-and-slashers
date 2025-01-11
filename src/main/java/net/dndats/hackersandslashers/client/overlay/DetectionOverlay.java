package net.dndats.hackersandslashers.client.overlay;

import net.dndats.hackersandslashers.utils.OverlayHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class DetectionOverlay {

    /**
     * This class manages the detection overlay that appears when the player sneaks
     * @param event: the event triggered to display the overlay
     */

    public static void renderDetectionOverlay(RenderGuiEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (canRender(player)) {
            int visibilityLevel = PlayerHelper.getVisibilityLevel(player);
            if (visibilityLevel > 90) {
                OverlayHelper.renderOverlay(event, "hackersandslashers:textures/screens/alert_80.png", player);
            } else if (visibilityLevel > 60) {
                OverlayHelper.renderOverlay(event, "hackersandslashers:textures/screens/alert_60.png", player);
            } else if (visibilityLevel > 40) {
                OverlayHelper.renderOverlay(event, "hackersandslashers:textures/screens/alert_40.png", player);
            } else if (visibilityLevel > 0) {
                OverlayHelper.renderOverlay(event, "hackersandslashers:textures/screens/alert_20.png", player);
            } else {
                OverlayHelper.renderOverlay(event, "hackersandslashers:textures/screens/alert_0.png", player);
            }
        }
    }

    private static boolean canRender(Player player) {
        return player.isCrouching();
    }

}
