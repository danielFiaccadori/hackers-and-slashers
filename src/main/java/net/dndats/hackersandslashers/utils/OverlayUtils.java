package net.dndats.hackersandslashers.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class OverlayUtils {

    public static void renderOverlay(RenderGuiEvent.Pre event, String filepath, Player player) {

        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();

        double x, y, z;

        if (player != null) {

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);

            RenderSystem.setShaderColor(1, 1, 1, 1);

                int textureSize = 16;
                int halfTexture = textureSize / 2;
                int gap = 2;

                int posX = (width / 2) - halfTexture;
                int posY = ((height / 2) - textureSize - gap) - 30;

                event.getGuiGraphics().blit(
                        ResourceLocation.parse(filepath),
                        posX,
                        posY,
                        0, 0,
                        textureSize, textureSize,
                        textureSize, textureSize
                    );
                }

            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);

    }

}

