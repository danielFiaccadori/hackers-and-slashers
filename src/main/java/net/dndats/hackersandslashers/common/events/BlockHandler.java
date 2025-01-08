package net.dndats.hackersandslashers.common.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.api.combat.mechanics.block.Block;
import net.dndats.hackersandslashers.common.setup.ModPlayerData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class BlockHandler {

    @SubscribeEvent
    public static void handleBlockBehavior(LivingIncomingDamageEvent event) {
        try {
            Block.blockBehavior(25, event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to block damage: {}", e.getMessage());
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
