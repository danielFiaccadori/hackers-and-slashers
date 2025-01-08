package net.dndats.hackersandslashers.api.combat.critical.logic;

import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;
import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.ItemHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// CRITICAL ATTACK OF TYPE BACKSTAB
public class BackstabLogic implements ICriticalLogic {

    private final float DAMAGE_MULTIPLIER;

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
            return EntityHelper.isBehind(player, target)
                    && !EntityHelper.isAwareOf(player, target);
        } else {
            return !EntityHelper.isBeingTargeted(player, target);
        }
    }

    @Override
    public float getAdditionalModifiers(LivingIncomingDamageEvent event) {
        ItemStack usedItem = event.getSource().getWeaponItem();
        if (usedItem == null) return 0;
        if (event.getSource().getEntity() instanceof Player player) {
            float baseDamage = ItemHelper.getAttackDamage(usedItem, player);
            float attackSpeed = ItemHelper.getAttackSpeed(usedItem, player);
            float alpha = 2.5f;
            float beta = 1f;
            float adjustmentFactor = 1.5f;
            float additionalDamage = (float) (Math.pow(attackSpeed, alpha) * adjustmentFactor - (baseDamage * beta));
            return Math.max(additionalDamage, 0);
        }
        return 0;
    }

    @Override
    public void applyOnHitFunction(LivingIncomingDamageEvent event) {
        Player player = (Player) event.getSource().getEntity();
        if (player == null) return;
        LivingEntity target = event.getEntity();
        if (EntityHelper.isBehind(player, target)) {
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
