package com.alternis.sculkwells.blocks.entity;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SculkWells.MOD_ID);

    public static final RegistryObject<BlockEntityType<SculkExtractorEntity>> SCULK_EXTRACTOR =
            BLOCK_ENTITIES.register("sculk_extractor", () -> BlockEntityType.Builder.of(SculkExtractorEntity::new, ModBlocks.SCULK_EXTRACTOR.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
