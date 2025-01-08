package net.dndats.hackersandslashers;

import net.dndats.hackersandslashers.common.setup.ModMobEffects;
import net.dndats.hackersandslashers.common.setup.ModParticles;
import net.dndats.hackersandslashers.common.setup.ModSounds;
import net.dndats.hackersandslashers.api.combat.critical.logic.BackstabLogic;
import net.dndats.hackersandslashers.api.combat.critical.logic.HeadshotLogic;
import net.dndats.hackersandslashers.api.combat.critical.logic.RiposteLogic;
import net.dndats.hackersandslashers.api.manager.MeleeCritical;
import net.dndats.hackersandslashers.api.manager.CriticalManager;
import net.dndats.hackersandslashers.common.ModPlayerData;
import net.dndats.hackersandslashers.api.manager.RangedCritical;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(HackersAndSlashers.MODID)
public class HackersAndSlashers {
    public static final String MODID = "hackersandslashers";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HackersAndSlashers(IEventBus modEventBus, ModContainer modContainer) {
        // Mod event register
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        // Register Critical types
        CriticalManager.registerCritical(new MeleeCritical("Backstab", new BackstabLogic(2.0F)));
        CriticalManager.registerCritical(new MeleeCritical("Riposte", new RiposteLogic(1.5F)));
        CriticalManager.registerCritical(new RangedCritical("Headshot", new HeadshotLogic(2.0F)));

        // Register data
        ModPlayerData.ATTACHMENT_TYPES.register(modEventBus);

        // Register assets
        ModMobEffects.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticles.register(modEventBus);

        LOGGER.info("HackersAndSlashers mod initialized without configuration.");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup executed.");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server is starting...");
    }

    @SubscribeEvent
    public void removeVanillaCritical(CriticalHitEvent event) {
        event.setCriticalHit(false);
    }

    @SubscribeEvent
    public void removeInvulnerabilityTicks(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            event.setInvulnerabilityTicks(0);
        }
    }

    @SubscribeEvent
    public void removeInvisibilityPotionOnHit(LivingIncomingDamageEvent event) {
        if (event.getEntity().hasEffect(MobEffects.INVISIBILITY)) {
            event.getEntity().removeEffect(MobEffects.INVISIBILITY);
        }
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Client setup executed.");
            LOGGER.info("Logged in as: {}", Minecraft.getInstance().getUser().getName());
        }
    }

}
