package com.alternis.sculkwells.blocks.entity.client;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.custom.SculkExtractor;
import com.alternis.sculkwells.blocks.entity.SculkExtractorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SculkExtractorModel extends GeoModel<SculkExtractorEntity> {
    @Override
    public ResourceLocation getModelResource(SculkExtractorEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "geo/sculk_extractor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SculkExtractorEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "textures/block/sculk_extractor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SculkExtractorEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "animations/sculk_extractor.animation.json");
    }
}
