package net.dndats.hackersandslashers;

import net.dndats.hackersandslashers.assets.effects.ModMobEffects;
import net.dndats.hackersandslashers.combat.critical.logic.BackstabLogic;
import net.dndats.hackersandslashers.combat.critical.logic.RiposteLogic;
import net.dndats.hackersandslashers.combat.critical.manager.CriticalAttack;
import net.dndats.hackersandslashers.combat.critical.manager.CriticalRegistry;
import net.dndats.hackersandslashers.common.ModData;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
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
        CriticalRegistry.registerCritical(new CriticalAttack("Backstab", new BackstabLogic(3.0F)));
        CriticalRegistry.registerCritical(new CriticalAttack("Riposte", new RiposteLogic(1.5F)));

        // Register data
        ModData.registerData(modEventBus);

        // Register assets
        ModMobEffects.register(modEventBus);

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

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Client setup executed.");
            LOGGER.info("Logged in as: {}", Minecraft.getInstance().getUser().getName());
        }
    }

}
