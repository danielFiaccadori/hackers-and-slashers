package net.dndats.hackersandslashers.common.combat.critical.manager;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.dndats.hackersandslashers.client.effects.SoundEffects;
import net.dndats.hackersandslashers.client.effects.VisualEffects;
import net.dndats.hackersandslashers.common.combat.critical.interfaces.ICritical;
import net.dndats.hackersandslashers.common.combat.critical.logic.BackstabLogic;
import net.dndats.hackersandslashers.utils.CombatUtils;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.ArrayList;
import java.util.List;

// MANAGES CRITICAL TYPES REGISTERED
public class CriticalManager {

    /**
     * This class is a register and manager for the critical types, registering them at the list above:
     */

    private static final List<ICritical> criticalTypes = new ArrayList<>();

    /**
     * Registers a critical type at the list
     * @param critical Carries a Critical hit
     */

    public static void registerCritical(ICritical critical) {
        criticalTypes.add(critical);
    }

    /**
     * This method is important! It intermediates the critical to the list.
     * It searches for all critical types applicable on that condition and call the applier of the respective critical types.
     * @param event Holds an attack event
     */

    public static boolean processCriticalHit(LivingIncomingDamageEvent event) {
        try {
            if (event.getSource().getEntity() instanceof Player player) {
                float totalDamageMultiplier = calculateTotalDamageMultiplier(event, player);
                if (totalDamageMultiplier > 0) {
                    float finalAmount = CombatUtils.dealCriticalDamage(totalDamageMultiplier, event);
                    HackersAndSlashers.LOGGER.info("Dealt {} damage with a total multiplier of {}",
                            finalAmount,
                            totalDamageMultiplier);
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            HackersAndSlashers.LOGGER.error("Error while trying to process critical: {}", e.getMessage());
        }
        return false;
    }

    /**
     * This helper method is used to calculate the total damage multiplier of the critical hits.
     *
     * @param event: the damage event
     * @param player: the player attempting to backstab
     * @return the final value of the multiplier
     */

    private static float calculateTotalDamageMultiplier(LivingIncomingDamageEvent event, Player player) {
        float totalDamageMultiplier = 0;
        for (ICritical critical : criticalTypes) {
            if (critical instanceof RangedCritical rangedCritical) {
                if (rangedCritical.getLogic().canBeApplied(event.getSource().getDirectEntity(), event.getEntity())) {
                    totalDamageMultiplier += rangedCritical.getLogic().getDamageMultiplier();
                }
            }
            if (critical.getLogic().canBeApplied(player, event.getEntity())) {
                totalDamageMultiplier += critical.getLogic().getDamageMultiplier();
                if (critical.getLogic() instanceof BackstabLogic) {
                    totalDamageMultiplier += getBackstabAdditionalModifier(event);
                }
                if (event.getEntity().hasEffect(ModMobEffects.STUN)) {
                    event.getEntity().removeEffect(ModMobEffects.STUN);
                }
            }
        }
        return totalDamageMultiplier;
    }

    private static final float BACKSTAB_MODIFIER_MULTIPLIER = 0.25F;

    /**
     * The helper method below is used to calculate the additional damage to the backstab, that increases based on attack speed
     *
     * @param event: the damage event
     * @return the additional amount based on 25% of the attack speed modifier
     */

    private static float getBackstabAdditionalModifier(LivingIncomingDamageEvent event) {
        ItemStack usedItem = event.getSource().getWeaponItem();
        if (usedItem == null) return 0;
        if (event.getSource().getEntity() instanceof Player player) {
            for (var entry : usedItem.getAttributeModifiers().modifiers()) {
                if (entry.attribute() == Attributes.ATTACK_SPEED) {
                    double modifierValue = entry.modifier().amount();
                    double baseAttackSpeed = player.getAttribute(Attributes.ATTACK_SPEED).getBaseValue();
                    double finalModifier = baseAttackSpeed + modifierValue;
                    return  (float) (finalModifier * BACKSTAB_MODIFIER_MULTIPLIER);
                }
            }
        }
        return 0;
    }

}
