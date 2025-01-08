package net.dndats.hackersandslashers.api.combat.mechanics.block;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.ModPlayerData;
import net.dndats.hackersandslashers.common.network.packets.PacketTriggerPlayerBlock;
import net.dndats.hackersandslashers.utils.AnimationHelper;
import net.dndats.hackersandslashers.utils.ItemHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.HashSet;

public class Block {

    private static final HashSet<ResourceKey<DamageType>> damageSourcesAccepted = new HashSet<>(
            Arrays.asList(
                    DamageTypes.PLAYER_ATTACK,
                    DamageTypes.MOB_ATTACK,
                    DamageTypes.ARROW,
                    DamageTypes.MOB_ATTACK_NO_AGGRO,
                    DamageTypes.EXPLOSION
            )
    );

    /**
     * This method represents the block mechanic behavior
     *
     * @param percentage: the damage percentage reduced when blocked an attack
     * @param event: the event that is responsible by applying the block effect
     */

    public static void reduceIncomingDamage(float percentage, LivingIncomingDamageEvent event) {
        try {
            if (event.getEntity() instanceof Player player) {
                if (PlayerHelper.isBlocking(player)) {
                    SoundEffects.playBlockSound(player);
                    ItemHelper.damageBlockWeapon(player, (int) event.getAmount());
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
            AnimationHelper.playBlockAnimation(player);
            var playerData = player.getData(ModPlayerData.IS_BLOCKING);
            playerData.setIsBlocking(true);
            PacketDistributor.sendToServer(new PacketTriggerPlayerBlock(playerData, duration));
        }
    }

    private static boolean canBlock(Player player) {
        return PlayerHelper.isHoldingSword(player)
                && !player.isCrouching()
                && !PlayerHelper.isBlocking(player)
                && !PlayerHelper.isPointingAtBlockEntity(player)
                && !player.swinging;
    }

}
