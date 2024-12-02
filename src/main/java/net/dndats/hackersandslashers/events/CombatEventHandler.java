package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.input.Keybinds;
import net.dndats.hackersandslashers.combat.critical.manager.CriticalRegistry;
import net.dndats.hackersandslashers.combat.mechanics.block.Block;
import net.dndats.hackersandslashers.playerdata.ModPlayerData;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// HANDLER: COMBAT RELATED THINGS
@EventBusSubscriber(modid = "hackersandslashers")
public class CombatEventHandler {

    // These methods communicate with the deeper layers, delegating the implementation of the necessary logic

    // Critical hit handler
    @SubscribeEvent
    public static void onEntityHurt(LivingIncomingDamageEvent event) {
        try {
            //Apply critical hit
            CriticalRegistry.processCriticalHit(event);
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error on entity hurt subscribed event: {}", e.getMessage());
        }
    }

}
