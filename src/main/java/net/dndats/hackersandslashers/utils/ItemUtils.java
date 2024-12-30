package net.dndats.hackersandslashers.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.LevelAccessor;

import java.util.Collections;
import java.util.List;

public class ItemUtils {

    public static void damage(LevelAccessor world, ItemStack itemStack, int amount) {
        if (itemStack == null || world == null)
            return;
        if (world instanceof ServerLevel level) {
            itemStack.hurtAndBreak(amount, level, null, stackProvider -> {});
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

}
