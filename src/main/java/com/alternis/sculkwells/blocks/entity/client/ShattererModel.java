package com.alternis.sculkwells.blocks.entity.client;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.entity.SculkExtractorEntity;
import com.alternis.sculkwells.blocks.entity.ShattererEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShattererModel extends GeoModel<ShattererEntity> {
    @Override
    public ResourceLocation getModelResource(ShattererEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "geo/shatterer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShattererEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "textures/block/shatterer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShattererEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "animations/shatterer.animation.json");
    }
}
