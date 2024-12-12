package net.dndats.hackersandslashers.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

public class ItemUtils {

    public static void damageItem(LevelAccessor world, ItemStack itemStack, int amount) {
        if (itemStack == null || world == null)
            return;
        if (world instanceof ServerLevel level) {
            itemStack.hurtAndBreak(amount, level, null, stackProvider -> {});
        }
    }


}
