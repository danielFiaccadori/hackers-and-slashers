package net.dndats.hackersandslashers.combat.mechanics.block;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.TickScheduler;
import net.dndats.hackersandslashers.client.animations.PlayerAnimator;
import net.dndats.hackersandslashers.network.packets.PlayerBlockPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.network.PacketDistributor;

import static net.dndats.hackersandslashers.common.ModPlayerData.IS_BLOCKING;

public class Block {

    public static void triggerDefensive(int duration) {
        HackersAndSlashers.LOGGER.info("Triggered defensive at Block::triggerDefensive");
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (canBlock(player)) {
            PacketDistributor.sendToServer(new PlayerBlockPacket(true));
            PlayerAnimator.execute(null, player.level(), player, "parry_variation1");
            TickScheduler.schedule(() -> {
                PacketDistributor.sendToServer(new PlayerBlockPacket(false));
            }, duration);
        }
    }

    private static boolean canBlock(Player player) {
        return !player.getData(IS_BLOCKING) && player.getMainHandItem().getItem() instanceof SwordItem;
    }

}
