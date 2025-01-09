package net.dndats.hackersandslashers.api.combat.critical.logic;

import net.dndats.hackersandslashers.common.setup.ModMobEffects;
import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;
import net.dndats.hackersandslashers.utils.ItemHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

// CRITICAL ATTACK OF TYPE RIPOSTE
public class RiposteLogic implements ICriticalLogic {

    private final float DAMAGE_MULTIPLIER;

    /**
     * When created in main class, specify the amount of damage that this critical hit does.
     * @param damageMultiplier The multiplier of this critical hit.
     */

    public RiposteLogic(float damageMultiplier) {
        if (damageMultiplier <= 0) {
            throw new IllegalArgumentException("Damage multiplier must be greater than 0");
        }
        DAMAGE_MULTIPLIER = damageMultiplier;
    }

    /**
     * Verifies if the riposte can be applied if an entity has the Stun effect.
     * @param source The player attempting the riposte.
     * @param target The target entity.
     * @return True if the riposte can be applied, false otherwise.
     */

    @Override
    public boolean canBeApplied(Entity source, LivingEntity target) {
        return target.hasEffect(ModMobEffects.STUN);
    }

    @Override
    public float getAdditionalModifiers(LivingIncomingDamageEvent event) {
        ItemStack usedItem = event.getSource().getWeaponItem();
        if (usedItem == null) return 0;
        if (event.getSource().getEntity() instanceof Player player) {
            float baseDamage = ItemHelper.getAttackDamage(usedItem, player);
            float attackSpeed = ItemHelper.getAttackSpeed(usedItem, player);
            float alpha = 0.125f;
            float beta = 0.75f;
            float adjustmentFactor = 0.25f;
            float additionalDamage = (float) (Math.pow(baseDamage, beta) * adjustmentFactor - (attackSpeed * alpha));
            return Math.max(additionalDamage, 0);
        }
        return 0;
    }

    @Override
    public void applyOnHitFunction(LivingIncomingDamageEvent event) {
        if (event.getEntity().hasEffect(ModMobEffects.STUN)) {
            event.getEntity().removeEffect(ModMobEffects.STUN);
            event.setAmount(event.getAmount() + event.getEntity().getMaxHealth() * 0.05F);
        }
    }

    @Override
    public float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

}
