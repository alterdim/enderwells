package com.alternis.sculkwells.blocks.entity.client;

import com.alternis.sculkwells.blocks.entity.SculkExtractorEntity;
import com.alternis.sculkwells.entity.custom.ProteanGuardEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SculkExtractorRenderer extends GeoBlockRenderer<SculkExtractorEntity> {
    public SculkExtractorRenderer(BlockEntityRendererProvider.Context context) {
        super(new SculkExtractorModel());
        this.addRenderLayer(new SculkExtractorItemLayer(this));

    }

    @Override
    public RenderType getRenderType(SculkExtractorEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }




    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private static class SculkExtractorItemLayer extends GeoRenderLayer<SculkExtractorEntity> {

        public SculkExtractorItemLayer(GeoRenderer<SculkExtractorEntity> entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(PoseStack pPoseStack, SculkExtractorEntity pBlockEntity,
                           BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack itemStack = pBlockEntity.getRenderStack();
            pPoseStack.pushPose();
            double offset = Math.sin(0.1*(pBlockEntity.getLevel().getDayTime()))*0.1;
            pPoseStack.translate(0.5f, 0.15f, 0.5f);
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.GROUND, getLightLevel(pBlockEntity.getLevel(),
                    pBlockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, pPoseStack, bufferSource, 1);
            pPoseStack.popPose();
        }
        private int getLightLevel(Level level, BlockPos pos) {
            int bLight = level.getBrightness(LightLayer.BLOCK, pos);
            int sLight = level.getBrightness(LightLayer.SKY, pos);
            return LightTexture.pack(bLight, sLight);
        }
    }

}
