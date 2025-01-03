package net.dndats.hackersandslashers.common.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.dndats.hackersandslashers.common.combat.critical.manager.CriticalManager;
import net.dndats.hackersandslashers.common.combat.mechanics.block.Block;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

// HANDLER: COMBAT RELATED THINGS
@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class CombatEventHandler {

    // These methods communicate with the deeper layers, delegating the implementation of the necessary logic

    // Block logic implementation
    @SubscribeEvent
    public static void blockReduceDamage(LivingIncomingDamageEvent event) {
        try {
            Block.blockDamage(15, event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to block damage: {}", e.getMessage());
        }
    }

    // Critical hit handler
    @SubscribeEvent
    public static void dealCriticalHit(LivingIncomingDamageEvent event) {
        try {
            //Apply critical hit
            boolean isCritical = CriticalManager.processCriticalHit(event);
            CombatUtils.spawnCombatParticles(event, isCritical);
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

    @SubscribeEvent
    public static void stunEntityBlocked(LivingIncomingDamageEvent event) {
        try {
            CombatUtils.stunAttackingEntity(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to stun the target {}: {}", event.getSource().getEntity().getDisplayName(), e.getMessage());
        }
    }

}
