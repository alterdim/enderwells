package com.alternis.sculkwells.event;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.entity.ModBlockEntities;
import com.alternis.sculkwells.blocks.entity.client.SculkExtractorRenderer;
import com.alternis.sculkwells.blocks.entity.client.ShattererRenderer;
import com.alternis.sculkwells.client.SculkExtractorEntityRenderer;
import com.alternis.sculkwells.entity.ModEntityTypes;
import com.alternis.sculkwells.entity.custom.ProteanGuardEntity;
import com.alternis.sculkwells.particles.ModParticles;
import com.alternis.sculkwells.particles.custom.SculkCriesParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = SculkWells.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        private static ParticleEngine.SpriteParticleRegistration<SimpleParticleType> ParticleProvider;

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event)
        {
            event.put(ModEntityTypes.PROTEAN_GUARD.get(), ProteanGuardEntity.setAttributes());
        }
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
        {
            event.registerBlockEntityRenderer(ModBlockEntities.SCULK_EXTRACTOR.get(), SculkExtractorRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.SHATTERER.get(), ShattererRenderer::new);
        }
        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void registerParticleTypes(RegisterParticleProvidersEvent event)
        {
            event.register(ModParticles.SCULK_SOUL_PARTICLES.get(), SculkCriesParticle.Factory::new);
        }
        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void onParticlesRegistry(RegisterParticleProvidersEvent event)
        {
            Minecraft.getInstance().particleEngine.register(ModParticles.SCULK_SOUL_PARTICLES.get(), SculkCriesParticle.Factory::new);
        }
    }
}
