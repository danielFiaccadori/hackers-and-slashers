package net.dndats.hackersandslashers.assets.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record HeartsEdgeEnchantmentEffect() implements EnchantmentEntityEffect {

    public static final MapCodec<HeartsEdgeEnchantmentEffect> CODEC = MapCodec.unit(HeartsEdgeEnchantmentEffect::new);

    @Override
    public void apply(@NotNull ServerLevel serverLevel, int enchantmentLevel, @NotNull EnchantedItemInUse enchantedItemInUse, @NotNull Entity entity, @NotNull Vec3 vec3) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getHealth() < livingEntity.getMaxHealth() * ((float) enchantmentLevel / 10)) {
                livingEntity.kill();
                VisualEffects.spawnHeartsEdgeExecuteEffect(serverLevel, entity.getX(), entity.getY() + entity.getBbHeight()/2, entity.getZ());
            }
        }
    }

    @Override
    public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }

}
