package net.dndats.hackersandslashers.assets.effects;

import net.dndats.hackersandslashers.HackersAndSlashers;
import net.dndats.hackersandslashers.assets.effects.instance.Stun;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HackersAndSlashers.MODID);
    public static final DeferredHolder<MobEffect, MobEffect> STUN = MOB_EFFECTS.register("stun", Stun::new);

}
