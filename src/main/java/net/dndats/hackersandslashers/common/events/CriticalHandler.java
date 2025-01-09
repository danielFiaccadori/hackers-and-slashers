package net.dndats.hackersandslashers.common.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.dndats.hackersandslashers.api.manager.CriticalManager;
import net.dndats.hackersandslashers.utils.CombatHelper;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class CriticalHandler {

    @SubscribeEvent
    public static void dealCriticalHit(LivingIncomingDamageEvent event) {
        try {
            boolean isCritical = false;
            if (event.getSource().getEntity() instanceof Player player) {
                isCritical = CriticalManager.applyCriticalHit(event);
                if (isCritical) {
                    SoundEffects.playCriticalSound(event.getEntity());
                    VisualEffects.spawnCriticalParticle(event.getEntity().level(),
                            event.getEntity().getX(),
                            event.getEntity().getEyeY() + 1,
                            event.getEntity().getZ(),
                            event.getSource());
                }
            }
            CombatHelper.spawnCombatParticles(event, isCritical);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to apply a critical hit: {}", e.getMessage());
        }
    }

}
