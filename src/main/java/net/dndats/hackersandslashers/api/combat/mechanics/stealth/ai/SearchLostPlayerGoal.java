package net.dndats.hackersandslashers.api.combat.mechanics.stealth.ai;

import net.dndats.hackersandslashers.utils.EntityHelper;
import net.dndats.hackersandslashers.utils.PlayerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class SearchLostPlayerGoal extends Goal {

    /**
     * This goal makes mobs search for possible targets around, when they detect a suspicious action
     */

    private static final Double SEARCH_SPEED = 1.0;
    private final Mob mob;
    private BlockPos targetPos;
    private Player target;

    public SearchLostPlayerGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Do only for suspect mobs, with 50% of chance
        return EntityHelper.getMobAlertLevel(mob) == 50 && mob.getRandom().nextBoolean();
    }

    @Override
    public void start() {
        Player mostProbableTarget = mob.level().getNearestPlayer(mob, 20);
        if (PlayerHelper.getVisibilityLevel(mostProbableTarget) == 50 && mostProbableTarget != null) {
            target = mostProbableTarget;
        }
        if (target != null) {
            RandomSource random = mob.getRandom();
            int offsetX = random.nextInt(7) - 3;
            int offsetZ = random.nextInt(7) - 3;
            targetPos = target.blockPosition().offset(mob.getRandom().nextInt(10) - 5, 0, mob.getRandom().nextInt(10) - 5);
        }
    }

    @Override
    public void tick() {
        if (targetPos != null) {
            mob.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, SEARCH_SPEED);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return targetPos != null && !mob.getNavigation().isDone();
    }

}