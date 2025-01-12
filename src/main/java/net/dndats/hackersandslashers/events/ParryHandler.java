package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.combat.mechanics.parry.Parry;
import net.dndats.hackersandslashers.common.setup.ModData;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class ParryHandler {

    @SubscribeEvent
    public static void updateLostHealth(LivingIncomingDamageEvent event) {
        try {
            if (event.getEntity() instanceof Mob mob && event.getSource().getEntity() instanceof Player) {
                EntityHelper.updateLostHealth(mob);
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to update entity lost health data: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void handleBlockBehavior(LivingIncomingDamageEvent event) {
        try {
            Parry.parryBehavior(25, event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to block damage: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void attackCancelDefensiveState(AttackEntityEvent event) {
        Player player = event.getEntity();
        var playerData = player.getData(ModData.IS_PARRYING);
        if (playerData.getIsParrying()) {
            event.setCanceled(true);
        }
    }

}
