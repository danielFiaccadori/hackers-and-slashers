package net.dndats.hackersandslashers.client.effects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class SoundEffects {

    public static void playCriticalSound(LivingEntity entity) {
        entity.level().playSound(null,
                entity.blockPosition(),
                SoundEvents.PLAYER_ATTACK_CRIT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public static void playBlockSound(LivingEntity entity) {
        entity.level().playSound(null,
                entity.blockPosition(),
                SoundEvents.SHIELD_BLOCK,
                SoundSource.PLAYERS, 1.0F, 1.0F);
    }

}
