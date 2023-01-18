package com.alternis.sculkwells.entity.client;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.entity.custom.ProteanGuardEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ProteanGuardModel extends GeoModel<ProteanGuardEntity> {
    @Override
    public ResourceLocation getModelResource(ProteanGuardEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "geo/protean_guard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProteanGuardEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "textures/entity/protean_guard.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ProteanGuardEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "animations/protean_guard.animation.json");
    }
}
