package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.input.Keybinds;
import net.dndats.hackersandslashers.combat.mechanics.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

// HANDLER: KEYBINDS
@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class KeybindEventHandler {

    // Block handler
    @SubscribeEvent
    public static void onEntityBlock(ClientTickEvent.Pre event) {
        if (Keybinds.PARRY.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            Block.triggerDefensive(5, player);
        }
    }

}
