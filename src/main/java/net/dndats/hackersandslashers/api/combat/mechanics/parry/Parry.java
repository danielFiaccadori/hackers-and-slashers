package net.dndats.hackersandslashers.api.combat.mechanics.parry;

import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.common.setup.ModPlayerData;
import net.dndats.hackersandslashers.common.network.packets.PacketTriggerPlayerParry;
import net.dndats.hackersandslashers.utils.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Parry {

    private static final Random lostHealthComparator = new Random();
    private static final int MAX_COOLDOWN = 20;

    public static int getMaxCooldown() {
        return MAX_COOLDOWN;
    }

    /**
     * This set stores all damage types that can be blocked
     */

    private static final HashSet<ResourceKey<DamageType>> damageSourcesAccepted = new HashSet<>(
            Arrays.asList(
                    DamageTypes.PLAYER_ATTACK,
                    DamageTypes.MOB_ATTACK,
                    DamageTypes.ARROW,
                    DamageTypes.MOB_ATTACK_NO_AGGRO,
                    DamageTypes.EXPLOSION,
                    DamageTypes.SONIC_BOOM
            )
    );

    /**
     * This method represents the block mechanic behavior
     *
     * @param damageReduction: the damage damageReduction reduced when blocked an attack
     * @param event: the event that is responsible by applying the block effect
     */

    public static void parryBehavior(float damageReduction, LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (damageSourcesAccepted.stream().noneMatch(event.getSource()::is)) return;
        if (!PlayerHelper.isBlocking(player)) return;
        handleParryEffects(player, event, damageReduction);
    }

    private static void handleParryEffects(Player player, LivingIncomingDamageEvent event, float damageReduction) {
        SoundEffects.playParrySound(player);
        ItemHelper.damageBlockWeapon(player, (int) event.getAmount());
        event.setAmount(calculateReducedDamage(event.getAmount(), damageReduction));
        if (shouldStunAttacker(event)) {
            CombatHelper.stunAttackingEntity(event);
        }
    }

    private static float calculateReducedDamage(float originalDamage, float reductionPercentage) {
        return originalDamage * (reductionPercentage / 100);
    }

    private static boolean shouldStunAttacker(LivingIncomingDamageEvent event) {
        if (!event.getSource().isDirect() || !(event.getSource().getEntity() instanceof LivingEntity source)) {
            return false;
        }
        int chance = lostHealthComparator.nextInt(100);
        return EntityHelper.getMobLostHealth(source) >= chance;
    }

    /**
     * This method triggers the defensive mode based on a parameter
     * @param duration: the parameter that controls the amount of time that the player will stay in defensive mode
     */

    public static void triggerDefensive(int duration, Player player) {
        if (player == null) return;
        if (canParry(player)) {
            AnimationHelper.playBlockAnimation(player);
            var playerData = player.getData(ModPlayerData.IS_PARRYING);
            playerData.setIsParrying(true);
            PacketDistributor.sendToServer(new PacketTriggerPlayerParry(playerData, duration));
        }
    }

    private static boolean canParry(Player player) {
        return  !player.isCrouching()
                && !PlayerHelper.isBlocking(player)
                && !PlayerHelper.isPointingAtBlockEntity(player)
                && !player.swinging;
    }

}
