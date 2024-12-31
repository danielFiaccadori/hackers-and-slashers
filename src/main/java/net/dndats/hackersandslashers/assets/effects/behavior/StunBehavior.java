package net.dndats.hackersandslashers.assets.effects.behavior;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class StunBehavior {

    /**
     * These methods are related to stun uncommon behavior
     * @param event: every method takes an event to trigger an action
     */

    @SubscribeEvent
    public static void disablePlayerActions(AttackEntityEvent event) {
        if (event.getEntity().hasEffect(ModMobEffects.STUN)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEffectEnd(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() == null) return;
        if (event.getEffectInstance().getEffect().equals(ModMobEffects.STUN)) {
            if (event.getEntity() instanceof Mob mob) {
                mob.setNoAi(false);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffectInstance() == null) return;
        if (event.getEffectInstance().getEffect().equals(ModMobEffects.STUN)) {
            if (event.getEntity() instanceof Mob mob) {
                mob.setNoAi(false);
            }
        }
    }

}
