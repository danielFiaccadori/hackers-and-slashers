package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class TickScheduler {

    /*
    This class is a scheduler, to provide a function to wait, in ticks.
    */

    private static final Queue<ScheduledTask> workQueue = new ConcurrentLinkedQueue<>();

    public static void schedule(Runnable action, int ticks) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            workQueue.add(new ScheduledTask(action, ticks));
        }
    }

    @SubscribeEvent
    public static void tick(ServerTickEvent.Post event) {
        Iterator<ScheduledTask> iterator = workQueue.iterator();
        while (iterator.hasNext()) {
            ScheduledTask task = iterator.next();
            task.decrementTicks();
            if (task.isReady()) {
                HackersAndSlashers.LOGGER.debug("Executing scheduled task.");
                task.run();
                iterator.remove();
            }
        }
    }

    private static class ScheduledTask {
        private final Runnable action;
        private int ticksRemaining;

        public ScheduledTask(Runnable action, int ticks) {
            this.action = action;
            this.ticksRemaining = ticks;
        }

        public void decrementTicks() {
            this.ticksRemaining--;
        }

        public boolean isReady() {
            return ticksRemaining <= 0;
        }

        public void run() {
            action.run();
        }
    }
}