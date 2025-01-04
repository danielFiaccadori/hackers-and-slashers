package net.dndats.hackersandslashers.common.data;

import net.dndats.hackersandslashers.common.network.packets.PacketTriggerPlayerBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.UnknownNullability;

public class IsBlockingData implements INBTSerializable<CompoundTag> {

    /**
     * This class stores the data related to the blocking mechanic
     */

    private boolean isBlocking = false;

    public boolean getIsBlocking() {
        return isBlocking;
    }

    public void setIsBlocking(boolean isBlocking) {
        this.isBlocking = isBlocking;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isBlocking", isBlocking);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        isBlocking = tag.getBoolean("isBlocking");
    }

    public void syncData(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new PacketTriggerPlayerBlock(this));
        }
    }

}
