package net.dndats.hackersandslashers.common;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.common.data.IsBlockingData;
import net.dndats.hackersandslashers.common.data.VisibilityLevelData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModPlayerData {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, HackersAndSlashers.MODID);

    public static final Supplier<AttachmentType<IsBlockingData>> IS_BLOCKING = ATTACHMENT_TYPES.register("is_blocking", () ->
            AttachmentType.serializable(IsBlockingData::new).build());

    public static final Supplier<AttachmentType<VisibilityLevelData>> VISIBILITY_LEVEL = ATTACHMENT_TYPES.register("visibility_level", () ->
            AttachmentType.serializable(VisibilityLevelData::new).build());

}
