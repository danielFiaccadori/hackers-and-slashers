package net.dndats.hackersandslashers.combat.mechanics.block;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.playerdata.ModPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class Block {

    public static void triggerDefensive(int duration) {
        Player player = Minecraft.getInstance().player;
        if (player == null){
            return;
        }
        player.setData(ModPlayerData.IS_DEFENSIVE, true);
        HackersAndSlashers.LOGGER.info("Player {} is now in defensive mode! {}", player.getDisplayName(), player.getData(ModPlayerData.IS_DEFENSIVE));

        Minecraft.getInstance().execute(() -> {
            new Thread(() -> {
                try {
                    Thread.sleep((long) (duration * 1000L));
                    player.setData(ModPlayerData.IS_DEFENSIVE, false);
                    HackersAndSlashers.LOGGER.info("Player {} is no longer in defensive mode! {}", player.getDisplayName(), player.getData(ModPlayerData.IS_DEFENSIVE));
                } catch (InterruptedException e) {
                    HackersAndSlashers.LOGGER.error("Error in defensive timer: {}", e.getMessage());
                }
            }).start();
        });

    }


}
