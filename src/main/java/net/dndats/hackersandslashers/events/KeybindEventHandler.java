package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.dndats.hackersandslashers.client.input.Keybinds;
import net.dndats.hackersandslashers.combat.mechanics.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

// HANDLER: KEYBINDS
@EventBusSubscriber(modid = HackersAndSlashers.MODID, value = Dist.CLIENT)
public class KeybindEventHandler {

    // Block handler
    @SubscribeEvent
    public static void onEntityBlock(PlayerTickEvent.Pre event) {
        if (Keybinds.PARRY.consumeClick()) {
            Player player = event.getEntity();
            Block.triggerDefensive(5, player);
        }
    }

}
