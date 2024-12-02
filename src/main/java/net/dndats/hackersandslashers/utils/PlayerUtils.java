package net.dndats.hackersandslashers.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;

// UTILITY METHODS RELATED TO PLAYERS
public class PlayerUtils {

    public static boolean isOnDarkPlace(Player player) {
        Level level = player.level();
        BlockPos position = player.blockPosition();
        int lightLevel = Math.max(
                level.getBrightness(LightLayer.SKY, position),
                level.getBrightness(LightLayer.BLOCK, position)
        );
        return lightLevel <= 10;
    }

    public static boolean isOnBush(Player player) {
        Block block = player.level().getBlockState(player.blockPosition()).getBlock();
        return block instanceof BushBlock;
    }

}
