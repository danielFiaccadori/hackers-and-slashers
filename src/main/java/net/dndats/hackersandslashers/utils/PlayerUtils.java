package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.common.ModPlayerData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Objects;

// UTILITY METHODS RELATED TO PLAYERS
public class PlayerUtils {

    private static final ResourceLocation MOVESPEED_ATTRIBUTE_MODIFIER_LOCATION =
            ResourceLocation.fromNamespaceAndPath("hackersandslashers", "parry_movespeed_modifier");

    private static final AttributeModifier MOVESPEED_ATTRIBUTE_MODIFIER = new AttributeModifier(
            MOVESPEED_ATTRIBUTE_MODIFIER_LOCATION,
            -0.05,
            AttributeModifier.Operation.ADD_VALUE
    );

    // Checkers

    public static boolean isAtDarkPlace(Player player) {
        Level level = player.level();
        BlockPos position = player.blockPosition();
        boolean isObfuscated = (level.isRainingAt(position) || level.isThundering()) && level.isNight();
        int lightLevel = Math.max(
                level.getBrightness(LightLayer.SKY, position),
                level.getBrightness(LightLayer.BLOCK, position)
        );
        return lightLevel < 15 || isObfuscated;
    }

    public static boolean isOnBush(Player player) {
        Block block = player.level().getBlockState(player.blockPosition()).getBlock();
        return block instanceof BushBlock;
    }

    public static boolean isBlocking(Player player) {
        return player.getData(ModPlayerData.IS_BLOCKING).getIsBlocking();
    }

    public static double getVisibilityLevel(Player player) {
        if (player == null) return 0;
        return player.getData(ModPlayerData.VISIBILITY_LEVEL).getVisibilityLevel();
    }

    public static boolean isHoldingSword(Player player) {
        return player.getMainHandItem().getItem() instanceof SwordItem;
    }

    public static boolean isPointingAtBlockEntity(Player player) {
        HitResult block = player.pick(5, 0, false);
        if (block.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) block;
            BlockPos blockPos = blockHitResult.getBlockPos();
            Level world = player.level();
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            return blockEntity != null;
        }
        return false;
    }

    // Modifiers

    public static void addSpeedModifier(Player player) {
        if (!player.level().isClientSide) {
            if (!Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).hasModifier(MOVESPEED_ATTRIBUTE_MODIFIER_LOCATION)) {
                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(MOVESPEED_ATTRIBUTE_MODIFIER);
            } else {
                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(MOVESPEED_ATTRIBUTE_MODIFIER);
            }
        }
    }

    public static void removeSpeedModifier(Player player) {
        if (!player.level().isClientSide) {
            if (Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).hasModifier(MOVESPEED_ATTRIBUTE_MODIFIER_LOCATION)) {
                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(MOVESPEED_ATTRIBUTE_MODIFIER);
            }
        }
    }

}
