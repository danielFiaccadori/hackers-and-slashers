package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import java.util.Objects;

public class ItemUtils {

    public static void damage(LevelAccessor world, ItemStack item, int amount) {
        if (item == null || world == null)
            return;
        if (world instanceof ServerLevel level) {
            item.hurtAndBreak(amount, level, null, stackProvider -> {});
        }
    }

    public static void damageAndDistribute(LevelAccessor world, ItemStack item1, ItemStack item2, int amount) {
        if ((item1 == null || item2 == null) || world == null)
            return;
        if (world instanceof ServerLevel level) {
            item1.hurtAndBreak(amount/2, level, null, stackProvider -> {});
            item2.hurtAndBreak(amount/2, level, null, stackProvider -> {});
        }
    }

    public static float getAttackSpeed(ItemStack item, Player player) {
        if (item == null) return 0;
        for (var entry : item.getAttributeModifiers().modifiers()) {
            if (entry.attribute() == Attributes.ATTACK_SPEED) {
                double baseAttackSpeed = Objects.requireNonNull(
                        player.getAttribute(Attributes.ATTACK_SPEED)).getBaseValue();
                double additionalAttackSpeed = entry.modifier().amount();
                double finalValue = baseAttackSpeed + additionalAttackSpeed;
                return (float) finalValue;
            }
        }
        return 0;
    }

    public static float getAttackDamage(ItemStack item, Player player) {
        if (item == null) return 0;
        for (var entry : item.getAttributeModifiers().modifiers()) {
            if (entry.attribute() == Attributes.ATTACK_DAMAGE) {
                return (float) entry.modifier().amount();
            }
        }
        return 0;
    }

}
