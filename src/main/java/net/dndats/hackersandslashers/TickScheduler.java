package net.dndats.hackersandslashers;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

@EventBusSubscriber(modid = "hackersandslashers")
public class TickScheduler {

    /*
    This class is a scheduler, to provide a function to wait, in ticks.
     */

    private static final Queue<ScheduledTask> TASK_QUEUE = new PriorityQueue<>(
            Comparator.comparingInt(ScheduledTask::getRemainingTicks)
    );

    public static void schedule(Runnable action, int ticks) {
        TASK_QUEUE.add(new ScheduledTask(action, ticks));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        if (event.hasTime()) {
            processTasks();
        }
    }

    private static void processTasks() {
        if (!TASK_QUEUE.isEmpty()) {
            Iterator<ScheduledTask> iterator = TASK_QUEUE.iterator();
            while (iterator.hasNext()) {
                ScheduledTask task = iterator.next();
                task.tick();
                if (task.isComplete()) {
                    iterator.remove();
                }
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

        public int getRemainingTicks() {
            return remainingTicks;
        }
    }

}