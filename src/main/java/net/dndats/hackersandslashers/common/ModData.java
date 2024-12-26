package net.dndats.hackersandslashers.common;

import com.mojang.serialization.Codec;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModData {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, HackersAndSlashers.MODID
    );

    public static final Supplier<AttachmentType<Boolean>> IS_BLOCKING = ATTACHMENT_TYPES.register(
            "is_blocking", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build()
    );

    public static final Supplier<AttachmentType<Integer>> VISIBILITY_LEVEL = ATTACHMENT_TYPES.register(
            "visibility_level", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static void registerData(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
