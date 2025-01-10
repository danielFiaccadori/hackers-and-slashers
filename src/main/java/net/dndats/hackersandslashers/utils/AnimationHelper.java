package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.dndats.hackersandslashers.common.network.packets.PacketServerPlayAnimation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnimationHelper {

    private static final Set<String> lightSwordWeapons = new HashSet<>(
            Arrays.asList("dagger", "knife", "saï", "sai")
    );

    private static final Set<String> mediumSwordWeapons = new HashSet<>(
            Arrays.asList("sword", "cutlass", "blade")
    );

    private static final Set<String> heavySwordWeapons = new HashSet<>(
            Arrays.asList("claymore", "longsword")
    );

    private static final Set<String> slimSwordWeapons = new HashSet<>(
            Arrays.asList("rapier", "saber")
    );

    private static final Set<String> cuttingSwordWeapons = new HashSet<>(
            Arrays.asList("katana", "uchigatana", "scythe")
    );

    private static final Set<String> smasherSwordWeapons = new HashSet<>(
            Arrays.asList("hammer", "axe", "mace")
    );

    private static void playAnimation(Player player, String animationKey) {
        PlayerAnimator.playAnimation(player.level(), player, animationKey);
        PacketDistributor.sendToServer(new PacketServerPlayAnimation(animationKey));
    }

    private static String getWeaponCategory(String mainHandName) {
        if (lightSwordWeapons.stream().anyMatch(mainHandName::contains)) return "parry_light";
        if (mediumSwordWeapons.stream().anyMatch(mainHandName::contains)) return "parry_generic";
        if (heavySwordWeapons.stream().anyMatch(mainHandName::contains)) return "parry_heavy";
        if (slimSwordWeapons.stream().anyMatch(mainHandName::contains)) return "parry_slim";
        if (cuttingSwordWeapons.stream().anyMatch(mainHandName::contains)) return "parry_cutting";
        if (smasherSwordWeapons.stream().anyMatch(mainHandName::contains)) return "parry_smasher";
        return "parry_generic";
    }

    @OnlyIn(Dist.CLIENT)
    public static void playBlockAnimation(Player player) {
        if (ModList.get().isLoaded("bettercombat")) {
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

        } else {
            playAnimation(player, "parry_generic_oh");
        }
    }

}
