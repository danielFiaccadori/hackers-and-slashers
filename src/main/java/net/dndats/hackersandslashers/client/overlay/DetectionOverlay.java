package net.dndats.hackersandslashers.client.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dndats.hackersandslashers.utils.OverlayUtils;
import net.dndats.hackersandslashers.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
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
            if (PlayerUtils.getVisibilityLevel(player) == 100) {
                OverlayUtils.renderOverlay(event, "hackersandslashers:textures/screens/alert.png", player);
            } else if (PlayerUtils.getVisibilityLevel(player) == 50) {
                OverlayUtils.renderOverlay(event, "hackersandslashers:textures/screens/suspect.png", player);
            } else {
                OverlayUtils.renderOverlay(event, "hackersandslashers:textures/screens/hidden.png", player);
            }
        }
    }

    private static boolean canRender(Player player) {
        return player.isCrouching();
    }

}
