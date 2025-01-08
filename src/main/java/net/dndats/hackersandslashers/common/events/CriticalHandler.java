package net.dndats.hackersandslashers.common.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.dndats.hackersandslashers.api.manager.CriticalManager;
import net.dndats.hackersandslashers.utils.CombatHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class CriticalHandler {

    @SubscribeEvent
    public static void dealCriticalHit(LivingIncomingDamageEvent event) {
        try {
            //Apply critical hit
            boolean isCritical = CriticalManager.applyCriticalHit(event);
            CombatHelper.spawnCombatParticles(event, isCritical);
            if (isCritical) {
                SoundEffects.playCriticalSound(event.getEntity());
                VisualEffects.spawnCriticalParticle(event.getEntity().level(),
                        event.getEntity().getX(),
                        event.getEntity().getEyeY() + 1,
                        event.getEntity().getZ(),
                        event.getSource());
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to apply a critical hit: {}", e.getMessage());
        }
    }

}
