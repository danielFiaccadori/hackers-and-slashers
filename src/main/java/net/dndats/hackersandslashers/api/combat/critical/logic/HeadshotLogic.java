package net.dndats.hackersandslashers.api.combat.critical.logic;

import net.dndats.hackersandslashers.api.interfaces.ICriticalLogic;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class HeadshotLogic implements ICriticalLogic {

    private final float DAMAGE_MULTIPLIER;

    /**
     * When created in main class, specify the amount of damage that this critical hit does.
     * @param damageMultiplier The multiplier of this critical hit.
     */

    public HeadshotLogic(float damageMultiplier) {
        if (damageMultiplier <= 0) {
            throw new IllegalArgumentException("Damage multiplier must be greater than 0");
        }
        DAMAGE_MULTIPLIER = damageMultiplier;
    }

    /**
     * Verifies if the headshot can be applied based on the position of the projectile related to the eye height of the entity.
     * @param directSourceEntity (important) the PROJECTILE attempting the headshot.
     * @param target The target entity.
     * @return True if the headshot can be applied, false otherwise.
     */

    @Override
    public boolean canBeApplied(Entity directSourceEntity, LivingEntity target) {
        if (directSourceEntity instanceof Projectile projectile) {
            double targetEyeHeight = target.getEyeHeight();
            double targetEyeYCenter = target.getY() + targetEyeHeight;
            double tolerance = 0.5;
            return projectile.position().y >= (targetEyeYCenter - tolerance) &&
                    projectile.position().y <= (targetEyeYCenter + tolerance);
        }
        return false;
    }

    @Override
    public float getAdditionalModifiers(LivingIncomingDamageEvent event) {
        return 0;
    }

    @Override
    public void applyOnHitFunction(LivingIncomingDamageEvent event) {

    }

    @Override
    public float getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

}
