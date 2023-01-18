package com.alternis.sculkwells.entity;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.entity.custom.ProteanGuardEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SculkWells.MOD_ID);

    public static final RegistryObject<EntityType<ProteanGuardEntity>> PROTEAN_GUARD =
            ENTITY_TYPES.register("protean_guard", () ->
                    EntityType.Builder.of(ProteanGuardEntity::new, MobCategory.MONSTER).sized(0.4f, 1.5f)
                            .build(new ResourceLocation(SculkWells.MOD_ID, "protean_guard").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
