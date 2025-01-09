package net.dndats.hackersandslashers.common.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.setup.ModKeybinds;
import net.dndats.hackersandslashers.api.combat.mechanics.parry.Parry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, value = Dist.CLIENT)
public class KeybindHandler {

    /**
     * Handles all events related to keybind press
     */

    @SubscribeEvent
    public static void onEntityBlock(ClientTickEvent.Pre event) {
        if (ModKeybinds.PARRY.consumeClick()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;
            Parry.triggerDefensive(10, player);
        }
    }

}
