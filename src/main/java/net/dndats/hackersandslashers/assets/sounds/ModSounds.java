package net.dndats.hackersandslashers.assets.sounds;

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

    public static final Supplier<SoundEvent> BACKSTAB_CRITICAL = registerSoundEvent("backstab_critical");
    public static final Supplier<SoundEvent> RIPOSTE_CRITICAL = registerSoundEvent("riposte_critical");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(HackersAndSlashers.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
