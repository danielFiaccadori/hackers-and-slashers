package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.combat.mechanics.stealth.Stealth;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.TickScheduler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class StealthHandler {

    /**
     * Handles all events related to the stealth mechanic
     */

    @SubscribeEvent
    public static void handleStealthBehavior(LivingChangeTargetEvent event) {
        try {
            Stealth.stealthBehavior(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to cancel entity target: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void resetMobAlert(EntityTickEvent.Pre event) {
        try {
            if (event.getEntity() instanceof Mob mob) {
                if (!EntityHelper.isAlert(mob)) {
                    if (EntityHelper.getAlertLevel(mob) > 0) {
                        EntityHelper.setAlertLevel(mob, EntityHelper.getAlertLevel(mob) - 1);
                    } else if (EntityHelper.getAlertLevel(mob) == 0) {
                        EntityHelper.removeAlertTags(mob);
                    }
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to reset entity data (IS_ALERT): {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void setPassiveMobAlert(EntityTickEvent.Pre event) {
        try {
            if (event.getEntity() instanceof Mob mob && !mob.isAggressive()) {
                if (mob.getLastAttacker() != null) {
                    EntityHelper.setAlertLevel(mob, 100);
                }
                if (mob instanceof Animal animal) {
                    if (!animal.isPanicking()) {
                        EntityHelper.setAlertLevel(mob, 0);
                    }
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to set passive mob entity data (IS_ALERT): {}", e.getMessage());
        }
    }

    private static int scheduledTracker = 0;
    @SubscribeEvent
    public static void mobTargetTracker(PlayerTickEvent.Pre event) {
        try {
            if (!event.getEntity().level().isClientSide) {
                scheduledTracker++;
                if (scheduledTracker >= 20) {
                    scheduledTracker = 0;
                    for (Player player : event.getEntity().level().players()) {
                        Stealth.updatePlayerVisibility(player);
                    }
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to change player data: {}", e.getMessage());
        }
    }

}
