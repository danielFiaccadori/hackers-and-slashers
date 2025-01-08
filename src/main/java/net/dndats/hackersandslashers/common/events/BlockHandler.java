package net.dndats.hackersandslashers.common.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.combat.mechanics.block.Block;
import net.dndats.hackersandslashers.common.ModPlayerData;
import net.dndats.hackersandslashers.utils.CombatHelper;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class BlockHandler {

    @SubscribeEvent
    public static void blockReduceDamage(LivingIncomingDamageEvent event) {
        try {
            Block.reduceIncomingDamage(25, event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to block damage: {}", e.getMessage());
        }
    }

    @SubscribeEvent
    public static void stunEntityBlocked(LivingIncomingDamageEvent event) {
        try {
            CombatHelper.stunAttackingEntity(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to stun the target {}: {}", event.getSource().getEntity().getDisplayName(), e.getMessage());
        }
    }

    @SubscribeEvent
    public static void attackCancelDefensiveState(AttackEntityEvent event) {
        Player player = event.getEntity();
        var playerData = player.getData(ModPlayerData.IS_BLOCKING);
        if (playerData.getIsBlocking()) {
            event.setCanceled(true);
        }
    }

}
