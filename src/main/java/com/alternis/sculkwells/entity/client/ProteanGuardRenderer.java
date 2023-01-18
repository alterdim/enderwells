package com.alternis.sculkwells.entity.client;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.entity.custom.ProteanGuardEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ProteanGuardRenderer extends GeoEntityRenderer<ProteanGuardEntity> {
    public ProteanGuardRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ProteanGuardModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(ProteanGuardEntity animatable) {
        return new ResourceLocation(SculkWells.MOD_ID, "textures/entity/protean_guard.png");
    }
    

}
