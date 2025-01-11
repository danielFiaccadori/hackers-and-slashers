package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.common.setup.ModPlayerData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

// UTILITY METHODS RELATED TO PLAYERS
public class PlayerHelper {

    private static final ResourceLocation PARRY_HEAVY_MODIFIER_LOCATION =
            ResourceLocation.fromNamespaceAndPath("hackersandslashers", "parry_heavy_modifier");
    private static final AttributeModifier PARRY_HEAVY_MODIFIER = new AttributeModifier(
            PARRY_HEAVY_MODIFIER_LOCATION,
            -0.05,
            AttributeModifier.Operation.ADD_VALUE
    );

    private static final ResourceLocation PARRY_GENERIC_MODIFIER_LOCATION =
            ResourceLocation.fromNamespaceAndPath("hackersandslashers", "parry_generic_modifier");
    private static final AttributeModifier PARRY_GENERIC_MODIFIER = new AttributeModifier(
            PARRY_GENERIC_MODIFIER_LOCATION,
            -0.05,
            AttributeModifier.Operation.ADD_VALUE
    );

    private static final ResourceLocation PARRY_LIGHT_MODIFIER_LOCATION =
            ResourceLocation.fromNamespaceAndPath("hackersandslashers", "parry_light_modifier");
    private static final AttributeModifier PARRY_LIGHT_MODIFIER = new AttributeModifier(
            PARRY_LIGHT_MODIFIER_LOCATION,
            -0.05,
            AttributeModifier.Operation.ADD_VALUE
    );

    // Checkers

    public static int lightLevel(Player player) {
        Level level = player.level();
        BlockPos position = player.blockPosition();
        return Math.max(
                level.getBrightness(LightLayer.SKY, position),
                level.getBrightness(LightLayer.BLOCK, position)
        );
    }

    public static int getArmorLevel(Player player) {
        return player.getArmorValue();
    }

    public static boolean isPlayerBehind(LivingEntity target, Player player) {
        Vec3 mobForward = Vec3.directionFromRotation(0, target.getYRot());
        Vec3 mobToPlayer = new Vec3(
                player.getX() - target.getX(),
                player.getY() - target.getY(),
                player.getZ() - target.getZ()
        ).normalize();
        double dotProduct = mobForward.dot(mobToPlayer);
        double angle = Math.acos(dotProduct) * (180 / Math.PI);
        return angle > 90;
    }

    public static boolean isOnBush(Player player) {
        Block block = player.level().getBlockState(player.blockPosition()).getBlock();
        return block instanceof BushBlock;
    }

    public static boolean isMoving(Player player) {
        return player.getSpeed() > 0;
    }

    public static boolean isBlocking(Player player) {
        return player.getData(ModPlayerData.IS_PARRYING).getIsParrying();
    }

    public static int getVisibilityLevel(Player player) {
        if (player == null) return 0;
        return player.getData(ModPlayerData.VISIBILITY_LEVEL).getVisibilityLevel();
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

    public static void addSpeedModifier(Player player, ItemStack item) {
        if (!player.level().isClientSide) {
            double attackSpeed = ItemHelper.getAttackSpeed(item, player);
            if (attackSpeed <= 1.4) {
                applyModifier(player, PARRY_HEAVY_MODIFIER, PARRY_HEAVY_MODIFIER_LOCATION);
            } else if (attackSpeed >= 1.9) {
                applyModifier(player, PARRY_LIGHT_MODIFIER, PARRY_LIGHT_MODIFIER_LOCATION);
            } else {
                applyModifier(player, PARRY_GENERIC_MODIFIER, PARRY_GENERIC_MODIFIER_LOCATION);
            }
        }
    }

    public static void removeSpeedModifier(Player player) {
        if (!player.level().isClientSide) {
            removeModifier(player, PARRY_HEAVY_MODIFIER_LOCATION);
            removeModifier(player, PARRY_GENERIC_MODIFIER_LOCATION);
            removeModifier(player, PARRY_LIGHT_MODIFIER_LOCATION);
        }
    }

    private static void applyModifier(Player player, AttributeModifier modifier, ResourceLocation location) {
        var attribute = Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED));
        if (!attribute.hasModifier(location)) {
            attribute.addTransientModifier(modifier);
        }
    }

    private static void removeModifier(Player player, ResourceLocation location) {
        var attribute = Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED));
        if (attribute.hasModifier(location)) {
            attribute.removeModifier(location);
        }
    }

}
