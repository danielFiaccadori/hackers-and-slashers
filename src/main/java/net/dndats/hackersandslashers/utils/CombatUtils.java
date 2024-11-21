package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// UTILITY METHODS RELATED TO COMBAT
public class CombatUtils {

    public static void dealCriticalDamage(float multiplier, LivingIncomingDamageEvent event) {
        try {
            event.setAmount(event.getOriginalAmount() * multiplier);
            event.getEntity().level().playSound(null,
                    event.getEntity().blockPosition(),
                    SoundEvents.PLAYER_ATTACK_CRIT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to apply a critical damage: {}", e.getMessage());
        }
    }

}
