package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;

public class AnimationUtils {

    // Executors

    public static void playBlockAnimation(Player player) {
        if (player.getMainHandItem().getItem() instanceof SwordItem && player.getOffhandItem().getItem() instanceof SwordItem) {
            PlayerAnimator.playAnimation(null, player.level(), player, "parry_variation2");
        } else {
            PlayerAnimator.playAnimation(null, player.level(), player, "parry_variation1");
        }
    }

}
