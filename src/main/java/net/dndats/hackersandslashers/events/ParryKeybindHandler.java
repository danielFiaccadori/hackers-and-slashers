package net.dndats.hackersandslashers.events;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.setup.ModKeybinds;
import net.dndats.hackersandslashers.api.combat.mechanics.parry.Parry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = HackersAndSlashers.MODID, value = Dist.CLIENT)
public class ParryKeybindHandler {

    /**
     * Handles the parry action keybind and it's cooldown
     */

    private static int currentCooldown = Parry.getMaxCooldown();

    @SubscribeEvent
    public static void onEntityBlock(ClientTickEvent.Pre event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (currentCooldown < Parry.getMaxCooldown()) {
            currentCooldown++;
        }
        if (ModKeybinds.PARRY.consumeClick()) {
            if (currentCooldown >= Parry.getMaxCooldown()) {
                currentCooldown = 0;
                Parry.triggerDefensive(10, player);
            } else {
                player.displayClientMessage(Component.literal("Parry is on cooldown!"), true);
            }
        }
    }

}
