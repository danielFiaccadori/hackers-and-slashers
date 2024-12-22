package net.dndats.hackersandslashers.combat.mechanics.block;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.TickScheduler;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.network.packets.PlayerBlockPacket;
import net.dndats.hackersandslashers.utils.AnimationUtils;
import net.dndats.hackersandslashers.utils.ItemUtils;
import net.dndats.hackersandslashers.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class Block {

    /**
     * This class represents the block mechanic behavior
     * @param percentage: the damage percentage reduced when blocked an attack
     * @param event: the event that triggers 
     */

    public static void blockDamage(float percentage, LivingIncomingDamageEvent event) {
        try {
            if (event.getEntity() instanceof Player player) {
                if (PlayerUtils.isBlocking(player)) {
                    SoundEffects.playBlockSound(player);
                    if (event.getEntity().getOffhandItem().getItem() instanceof SwordItem &&
                            event.getEntity().getMainHandItem().getItem() instanceof SwordItem) {
                        ItemUtils.damageAndDistribute(event.getEntity().level(),
                                event.getEntity().getMainHandItem(),
                                event.getEntity().getOffhandItem(),
                                (int) event.getOriginalAmount());
                    } else {
                        ItemUtils.damage(event.getEntity().level(),
                                event.getEntity().getMainHandItem(),
                                (int) event.getOriginalAmount());
                    }
                    float totalReducedDamage = event.getAmount() * (percentage / 100);
                    event.setAmount(totalReducedDamage);
                }
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to reduce damage: {}", e.getMessage());
        }
    }

    public static void triggerDefensive(int duration) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (canBlock(player)) {
            PacketDistributor.sendToServer(new PlayerBlockPacket(true));
            AnimationUtils.playBlockAnimation(player);
            TickScheduler.schedule(() -> PacketDistributor.sendToServer(new PlayerBlockPacket(false)), duration);
        }
    }

    public static boolean canBlock(Player player) {
        return !PlayerUtils.isBlocking(player) && PlayerUtils.isHoldingSword(player);
    }

}
