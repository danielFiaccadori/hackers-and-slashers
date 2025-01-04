package net.dndats.hackersandslashers.common.combat.critical.logic;

import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;
import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.dndats.hackersandslashers.utils.EntityUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Objects;

// CRITICAL ATTACK OF TYPE BACKSTAB
public class BackstabLogic implements ICriticalLogic {

    private final float DAMAGE_MULTIPLIER;
    private static final float BACKSTAB_MODIFIER_MULTIPLIER = 0.25F;

    /**
     * When created in main class, specify the amount of damage that this critical hit does.
     * @param damageMultiplier The multiplier of this critical hit.
     */

    public BackstabLogic(float damageMultiplier) {
        if (damageMultiplier <= 0) {
            throw new IllegalArgumentException("Damage multiplier must be greater than 0");
        }
        DAMAGE_MULTIPLIER = damageMultiplier;
    }

    /**
     * Verifies if the backstab can be applied based on the position of the player and the target.
     * Also, applies the backstab if it's not being actively targeted (for mobs).
     * @param source The player attempting the backstab.
     * @param target The target entity.
     * @return True if the backstab can be applied, false otherwise.
     */

    @Override
    public boolean canBeApplied(Entity source, LivingEntity target) {
        if (!(source instanceof Player player)) return false;
        if (target instanceof Player) {
            return EntityUtils.isBehind(player, target)
                    && !EntityUtils.isAwareOf(player, target);
        } else {
            return !EntityUtils.isBeingTargeted(player, target);
        }
    }

    @Override
    public boolean hasAdditionalModifiers() {
        return true;
    }

    @Override
    public float getAdditionalModifiers(LivingIncomingDamageEvent event) {
        ItemStack usedItem = event.getSource().getWeaponItem();
        if (usedItem == null) return 0;
        if (event.getSource().getEntity() instanceof Player player) {
            for (var entry : usedItem.getAttributeModifiers().modifiers()) {
                if (entry.attribute() == Attributes.ATTACK_SPEED) {
                    double modifierValue = entry.modifier().amount();
                    double baseAttackSpeed = Objects.requireNonNull(
                            player.getAttribute(Attributes.ATTACK_SPEED)).getBaseValue();
                    double finalModifier = baseAttackSpeed + modifierValue;
                    return  (float) (finalModifier * BACKSTAB_MODIFIER_MULTIPLIER);
                }
            }
        }
        return 0;
    }

    @Override
    public void applyOnHitFunction(LivingIncomingDamageEvent event) {
        Player player = (Player) event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (EntityUtils.isBehind(player, target)) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                    20,
                    1,
                    false,
                    false));

        }
    }

    @Override
    public float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

}
