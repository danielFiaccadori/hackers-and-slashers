package net.dndats.hackersandslashers.common.data;

import net.dndats.hackersandslashers.common.network.packets.PacketSetPlayerVisibility;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

public class VisibilityLevelData implements INBTSerializable<CompoundTag> {

    /**
     * This class stores the data related to the stealth mechanic, specifically the visibility
     */

    private int visibilityLevel = 0;

    public int getVisibilityLevel() {
        return visibilityLevel;
    }

    public void setVisibilityLevel(int visibilityLevel) {
        this.visibilityLevel = visibilityLevel;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("visibilityLevel", visibilityLevel);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        visibilityLevel = tag.getInt("visibilityLevel");
    }

    public void syncData(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new PacketSetPlayerVisibility(this));
        }
    }

}
