package net.dndats.hackersandslashers.combat.mechanics.block;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.ModPlayerData;
import net.dndats.hackersandslashers.network.packets.PacketTriggerPlayerBlock;
import net.dndats.hackersandslashers.utils.AnimationUtils;
import net.dndats.hackersandslashers.utils.ItemUtils;
import net.dndats.hackersandslashers.utils.PlayerUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class Block {

    /**
     * This class represents the block mechanic behavior
     *
     * @param percentage: the damage percentage reduced when blocked an attack
     * @param event: the event that is responsible by applying the block effect
     */

    public static void blockDamage(float percentage, LivingIncomingDamageEvent event) {
        try {
            if (event.getEntity() instanceof Player player) {
                if (PlayerUtils.isBlocking(player)) {
                    SoundEffects.playBlockSound(player);
                    if (event.getEntity().getOffhandItem().getItem() instanceof SwordItem &&
                            event.getEntity().getMainHandItem().getItem() instanceof SwordItem) {
                        ItemUtils.damageAndDistribute(event.getEntity().level(),
                                event.getEntity().getMainHandItem(),
                                event.getEntity().getOffhandItem(),
                                (int) event.getOriginalAmount());
                    } else {
                        ItemUtils.damage(event.getEntity().level(),
                                event.getEntity().getMainHandItem(),
                                (int) event.getOriginalAmount());
                    }
                    float totalReducedDamage = event.getAmount() * (percentage / 100);
                    event.setAmount(totalReducedDamage);
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to reduce damage: {}", e.getMessage());
        }
    }

    /**
     * This method triggers the defensive mode based on a parameter
     * @param duration: the parameter that controls the amount of time that the player will stay in defensive mode
     */

    public static void triggerDefensive(int duration, Player player) {
        if (player == null) return;
        if (canBlock(player)) {
            AnimationUtils.playBlockAnimation(player);
            var playerData = player.getData(ModPlayerData.IS_BLOCKING);
            playerData.setIsBlocking(true);
            PacketDistributor.sendToServer(new PacketTriggerPlayerBlock(playerData));
        }
    }

    private static boolean canBlock(Player player) {
        return PlayerUtils.isHoldingSword(player)
                && !player.isCrouching()
                && !PlayerUtils.isBlocking(player);
    }

}
