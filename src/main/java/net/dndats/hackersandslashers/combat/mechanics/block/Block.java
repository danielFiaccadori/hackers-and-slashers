package net.dndats.hackersandslashers.combat.mechanics.block;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.TickScheduler;
import net.dndats.hackersandslashers.common.ModPlayerData;
import net.dndats.hackersandslashers.network.PlayerBlockPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import static net.dndats.hackersandslashers.common.ModPlayerData.IS_BLOCKING;

public class Block {

    public static void triggerDefensive(int duration) {
        HackersAndSlashers.LOGGER.info("Triggered defensive at Block::triggerDefensive");
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (!player.getData(IS_BLOCKING)) {
            player.setData(IS_BLOCKING, true);
            PacketDistributor.sendToServer(new PlayerBlockPacket(true));
            TickScheduler.schedule(() -> {
                player.setData(IS_BLOCKING, false);
                PacketDistributor.sendToServer(new PlayerBlockPacket(false));
            }, duration);
        }
    }

}
