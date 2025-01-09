package net.dndats.hackersandslashers.common.data;

import net.dndats.hackersandslashers.common.network.packets.PacketTriggerPlayerParry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class IsParryingData implements INBTSerializable<CompoundTag> {

    /**
     * This class stores the data related to the blocking mechanic, including the duration.
     */

    private boolean isParrying = false;
    private int duration = 0;

    public boolean getIsParrying() {
        return isParrying;
    }

    public void setIsParrying(boolean isParrying) {
        this.isParrying = isParrying;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isParrying", isParrying);
        tag.putInt("duration", duration);
        return tag;
    }


    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag tag) {
        isParrying = tag.getBoolean("isParrying");
        duration = tag.getInt("duration");
    }

    public void syncData(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new PacketTriggerPlayerParry(this, duration));
        }
    }
}
