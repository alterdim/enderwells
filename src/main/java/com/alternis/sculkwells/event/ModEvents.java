package com.alternis.sculkwells.event;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.entity.ModBlockEntities;
import com.alternis.sculkwells.client.SculkExtractorEntityRenderer;
import com.alternis.sculkwells.entity.ModEntityTypes;
import com.alternis.sculkwells.entity.custom.ProteanGuardEntity;
import com.alternis.sculkwells.particles.ModParticles;
import com.alternis.sculkwells.particles.custom.SculkSoulParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

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
            event.registerBlockEntityRenderer(ModBlockEntities.SCULK_EXTRACTOR.get(), SculkExtractorEntityRenderer::new);
        }
        @SubscribeEvent
        public static void registerParticleTypes(RegisterParticleProvidersEvent event)
        {
            event.register(ModParticles.SCULK_SOUL_PARTICLES.get(), SculkSoulParticle.Factory::new);
        }
    }
}
