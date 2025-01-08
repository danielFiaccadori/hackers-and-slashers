package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.dndats.hackersandslashers.common.network.packets.PacketServerPlayAnimation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnimationHelper {

    private static final Set<String> lightWeapons = new HashSet<>(
            Arrays.asList("dagger", "knife", "sai")
    );

    private static final Set<String> mediumWeapons = new HashSet<>(
            Arrays.asList("sword", "rapier", "cutlass")
    );

    private static final Set<String> heavyWeapons = new HashSet<>(
            Arrays.asList("claymore", "heavy", "longsword", "double", "hammer")
    );

    private static void playAnimation(Player player, String animationKey) {
        PlayerAnimator.playAnimation(player.level(), player, animationKey);
        PacketDistributor.sendToServer(new PacketServerPlayAnimation(animationKey));
    }

    private static String getWeaponCategory(String mainHandName) {
        if (lightWeapons.stream().anyMatch(mainHandName::contains)) return "parry_light";
        if (mediumWeapons.stream().anyMatch(mainHandName::contains)) return "parry_generic";
        if (heavyWeapons.stream().anyMatch(mainHandName::contains)) return "parry_heavy";
        return "parry_generic";
    }

    @OnlyIn(Dist.CLIENT)
    public static void playBlockAnimation(Player player) {
        String mainHandName = ItemHelper.getRegistryName(player.getMainHandItem());
        String weaponCategory = getWeaponCategory(mainHandName);

        if (player.getMainHandItem().getItem() instanceof SwordItem
                && player.getOffhandItem().getItem() instanceof SwordItem) {
            playAnimation(player, weaponCategory + "_dh");
        } else if (player.getMainHandItem().getItem() instanceof SwordItem) {
            playAnimation(player, weaponCategory + "_oh");
        } else {
            playAnimation(player, weaponCategory + "_oh");
        }
    }

}
