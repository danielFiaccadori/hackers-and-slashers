package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.input.Keybinds;
import net.dndats.hackersandslashers.combat.mechanics.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

// HANDLER: KEYBINDS
@EventBusSubscriber(modid = "hackersandslashers")
public class KeybindEventHandler {

    // Block handler
    @SubscribeEvent
    public static void onEntityBlock(InputEvent.Key event) {
        if (Keybinds.PARRY.consumeClick()) {
            HackersAndSlashers.LOGGER.info("Key {} pressed, trying to trigger defensive mode", event.getKey());
            Block.triggerDefensive(20);
        }
    }

}
