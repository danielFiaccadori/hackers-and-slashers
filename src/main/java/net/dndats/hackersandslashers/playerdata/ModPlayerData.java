package net.dndats.hackersandslashers.playerdata;

import com.mojang.serialization.Codec;
import net.dndats.hackersandslashers.HackersAndSlashers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModPlayerData {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, HackersAndSlashers.MODID);

    public static final Supplier<AttachmentType<Boolean>> IS_DEFENSIVE = ATTACHMENT_TYPES.register(
            "is_defensive", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
