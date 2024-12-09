package net.dndats.hackersandslashers;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.LinkedList;
import java.util.Queue;

@EventBusSubscriber(modid = "hackersandslashers")
public class TickScheduler {

    private static final Queue<ScheduledTask> TASK_QUEUE = new LinkedList<>();

    public static void schedule(Runnable action, int ticks) {
        TASK_QUEUE.add(new ScheduledTask(action, ticks));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (event.hasTime()) {
            if (!TASK_QUEUE.isEmpty()) {
                TASK_QUEUE.forEach(ScheduledTask::tick);
                TASK_QUEUE.removeIf(ScheduledTask::isComplete);
            }
        }
    }

    private static class ScheduledTask {

        private int remainingTicks;
        private final Runnable action;

        public ScheduledTask(Runnable action, int remainingTicks) {
            this.action = action;
            this.remainingTicks = remainingTicks;
        }

        public void tick() {
            remainingTicks--;
            if (remainingTicks <= 0) {
                action.run();
            }
        }

        public boolean isComplete() {
            return remainingTicks <= 0;
        }

    }



}
