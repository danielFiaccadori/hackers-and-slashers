package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.playerdata.ModPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// UTILITY METHODS RELATED TO COMBAT
public class CombatUtils {

    public static void dealCriticalDamage(float multiplier, LivingIncomingDamageEvent event) {
        try {
            event.setAmount(event.getOriginalAmount() * multiplier);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to apply a critical damage: {}", e.getMessage());
        }
    }

}
