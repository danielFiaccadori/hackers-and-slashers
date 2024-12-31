package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.dndats.hackersandslashers.common.network.packets.PacketServerPlayAnimation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

public class AnimationUtils {

    @OnlyIn(Dist.CLIENT)
    public static void playBlockAnimation(Player player) {
        if (player.getMainHandItem().getItem() instanceof SwordItem && player.getOffhandItem().getItem() instanceof SwordItem) {
            PlayerAnimator.playAnimation(player.level(), player, "block_two_handed");
            PacketDistributor.sendToServer(new PacketServerPlayAnimation("block_two_handed"));
        } else {
            PlayerAnimator.playAnimation(player.level(), player, "block_one_handed");
            PacketDistributor.sendToServer(new PacketServerPlayAnimation("block_one_handed"));
        }
    }

}
