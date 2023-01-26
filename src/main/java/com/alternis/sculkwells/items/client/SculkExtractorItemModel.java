package com.alternis.sculkwells.items.client;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.entity.SculkExtractorEntity;
import com.alternis.sculkwells.items.SculkExtractorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SculkExtractorItemModel extends GeoModel<SculkExtractorItem> {

    @Override
    public ResourceLocation getModelResource(SculkExtractorItem animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "geo/sculk_extractor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SculkExtractorItem animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "textures/block/sculk_extractor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SculkExtractorItem animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "animations/sculk_extractor.animation.json");
    }
}
