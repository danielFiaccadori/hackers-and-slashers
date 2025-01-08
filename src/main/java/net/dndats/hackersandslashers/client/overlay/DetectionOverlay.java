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
            if (PlayerHelper.getVisibilityLevel(player) == 100) {
                OverlayHelper.renderOverlay(event, "hackersandslashers:textures/screens/alert.png", player);
            } else if (PlayerHelper.getVisibilityLevel(player) == 50) {
                OverlayHelper.renderOverlay(event, "hackersandslashers:textures/screens/suspect.png", player);
            } else {
                OverlayHelper.renderOverlay(event, "hackersandslashers:textures/screens/hidden.png", player);
            }
        }
    }

    private static boolean canRender(Player player) {
        return player.isCrouching();
    }

}
