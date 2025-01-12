package net.dndats.hackersandslashers.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class MobDetectabilityData implements INBTSerializable<CompoundTag> {

    private int alertLevel = 0;
    private boolean isAlert = false;
    private UUID currentTarget = null;

    public int getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(int alertLevel) {
        this.alertLevel = alertLevel;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setAlert(boolean alert) {
        isAlert = alert;
    }

    public UUID getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(UUID currentTarget) {
        this.currentTarget = currentTarget;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("alertLevel", alertLevel);
        tag.putBoolean("isAlert", isAlert);
        tag.putUUID("currentTargetUUID", currentTarget);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag tag) {
        alertLevel = tag.getInt("alertLevel");
        isAlert = tag.getBoolean("isAlert");
        currentTarget = tag.getUUID("currentTargetUUID");
    }

}
