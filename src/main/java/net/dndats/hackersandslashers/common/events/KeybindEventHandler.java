package net.dndats.hackersandslashers.common.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.input.ModKeybinds;
import net.dndats.hackersandslashers.common.combat.mechanics.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

// HANDLER: KEYBINDS
@EventBusSubscriber(modid = HackersAndSlashers.MODID, value = Dist.CLIENT)
public class KeybindEventHandler {

    /**
     * Handles all events related to keybind press
     */

    @SubscribeEvent
    public static void onEntityBlock(ClientTickEvent.Pre event) {
        if (ModKeybinds.PARRY.consumeClick()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            Block.triggerDefensive(15, player);
        }
    }

}
