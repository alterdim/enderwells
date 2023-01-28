package com.alternis.sculkwells.items.client;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.items.custom.SculkExtractorItem;
import com.alternis.sculkwells.items.custom.ShattererItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShattererItemModel extends GeoModel<ShattererItem> {

    @Override
    public ResourceLocation getModelResource(ShattererItem animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "geo/shatterer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShattererItem animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "textures/block/shatterer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShattererItem animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "animations/shatterer.json");
    }
}
