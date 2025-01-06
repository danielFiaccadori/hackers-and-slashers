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
        double attackSpeed = ItemUtils.getAttackSpeed(player.getMainHandItem(), player);
        if (player.getMainHandItem().getItem() instanceof SwordItem
                && player.getOffhandItem().getItem() instanceof SwordItem) {
            if (attackSpeed >= 2) {
                PlayerAnimator.playAnimation(player.level(), player, "parry_light_dh");
                PacketDistributor.sendToServer(new PacketServerPlayAnimation("parry_light_dh"));
            } else {
                PlayerAnimator.playAnimation(player.level(), player, "parry_variation2");
                PacketDistributor.sendToServer(new PacketServerPlayAnimation("parry_variation2"));
            }
        } else {
            PlayerAnimator.playAnimation(player.level(), player, "parry_variation1");
            PacketDistributor.sendToServer(new PacketServerPlayAnimation("parry_variation1"));
        }
    }

}
