package net.dndats.hackersandslashers.utils;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@EventBusSubscriber(modid = HackersAndSlashers.MODID)
public class TickScheduler {

    /*
    This class is a scheduler, to provide a function to wait, in ticks.
     */

    private static final Collection<Tuple<Runnable, Integer>> workQueue =
            new ConcurrentLinkedQueue<>();


    public static void schedule(Runnable action, int ticks) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new Tuple<>(action, ticks));
    }

    @SubscribeEvent
    public static void tick(ServerTickEvent.Post event) {
        List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
        workQueue.forEach(work -> {
            work.setB(work.getB() - 1);
            if (work.getB() == 0)
                actions.add(work);
        });
        actions.forEach(e -> e.getA().run());
        workQueue.removeAll(actions);
    }

}