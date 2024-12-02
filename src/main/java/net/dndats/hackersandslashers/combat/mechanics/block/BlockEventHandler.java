package net.dndats.hackersandslashers.combat.mechanics.block;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.input.Keybinds;
import net.dndats.hackersandslashers.playerdata.ModPlayerData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// HANDLER: BLOCK
@EventBusSubscriber(modid = "hackersandslashers")
public class BlockEventHandler {

    // Block handler
    @SubscribeEvent
    public static void onEntityBlock(InputEvent.Key event) {
        if (Keybinds.PARRY.consumeClick()) {
            Block.triggerDefensive(1);
        }
    }

    @SubscribeEvent
    public static void onEntityHurtBlocked (LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getData(ModPlayerData.IS_DEFENSIVE)) {
                HackersAndSlashers.LOGGER.info("Blocked damage!");
                SoundEffects.playBlockSound(player);
                event.setAmount(event.getAmount() * 0.5F);
            }
        }
    }

}
