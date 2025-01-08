package net.dndats.hackersandslashers.common.setup;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {

    // MOD SOUND EFFECT REGISTERER

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, HackersAndSlashers.MODID);

    public static final Supplier<SoundEvent> PLAYER_CRITICAL = registerSoundEvent();

    private static Supplier<SoundEvent> registerSoundEvent() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, "player_critical");
        return SOUND_EVENTS.register("player_critical", () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
