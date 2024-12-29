package net.dndats.hackersandslashers.client.effects;

import net.dndats.hackersandslashers.assets.sounds.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SoundEffects {

    /**
     * These methods are responsible to call sounds easier.
     * @param entity Every method takes an Entity to play the sound.
     */

    public static void playBackstabSound(LivingEntity entity) {
        entity.level().playSound(null,
                entity.blockPosition(),
                ModSounds.BACKSTAB_CRITICAL.get(),
                SoundSource.PLAYERS, 1.0F, 1.0F
        );
    }

    public static void playRiposteSound(LivingEntity entity) {
        entity.level().playSound(null,
                entity.blockPosition(),
                ModSounds.RIPOSTE_CRITICAL.get(),
                SoundSource.PLAYERS, 1.0F, 1.0F
        );
    }

    public static void playBlockSound(LivingEntity entity) {
        entity.level().playSound(null,
                entity.blockPosition(),
                SoundEvents.SHIELD_BLOCK,
                SoundSource.PLAYERS, 1.0F, 1.0F
        );

    }

    public static void playBlockSwingSound(LivingEntity entity) {
        entity.level().playSound(null,
                entity.blockPosition(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS, 1.0F, 1.0F
        );
    }

}
