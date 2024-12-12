package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import static net.dndats.hackersandslashers.common.ModPlayerData.IS_BLOCKING;

// UTILITY METHODS RELATED TO COMBAT
public class CombatUtils {

    public static void dealCriticalDamage(float multiplier, LivingIncomingDamageEvent event) {
        try {
            event.setAmount(event.getOriginalAmount() * multiplier);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to apply a critical damage: {}", e.getMessage());
        }
    }

    public static void blockDamage(float percentage, LivingIncomingDamageEvent event) {
        try {
            if (event.getEntity() instanceof Player player) {
                if (player.getData(IS_BLOCKING)) {
                    SoundEffects.playBlockSound(player);
                    ItemUtils.damageItem(event.getEntity().level(),
                            event.getEntity().getMainHandItem(),
                            (int)event.getOriginalAmount());
                    float totalReducedDamage = event.getAmount() * (percentage / 100);
                    event.setAmount(totalReducedDamage);
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to reduce damage: {}", e.getMessage());
        }
    }

    public static void stunAttackingEntity(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity entity) {
            if (entity.getHealth() < event.getEntity().getHealth()) {
                if (event.getEntity().getData(IS_BLOCKING)) {
                    entity.addEffect(new MobEffectInstance(ModMobEffects.STUN, 60, 1, false, false));
                }
            }
        }
    }

}
