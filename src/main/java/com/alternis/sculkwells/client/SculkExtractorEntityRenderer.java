package com.alternis.sculkwells.client;

import com.alternis.sculkwells.blocks.ModBlocks;
import com.alternis.sculkwells.blocks.custom.SculkExtractor;
import com.alternis.sculkwells.blocks.entity.SculkExtractorEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.renderable.BakedModelRenderable;
import org.joml.Vector3f;

import java.util.Random;

public class SculkExtractorEntityRenderer implements BlockEntityRenderer<SculkExtractorEntity> {
    float randomOffset = new Random().nextFloat(1, 100);
    public SculkExtractorEntityRenderer(BlockEntityRendererProvider.Context context ) {

    }
    @Override
    public void render(SculkExtractorEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getRenderStack();
        pPoseStack.pushPose();
        double offset = Math.sin(0.1*(pBlockEntity.getLevel().getDayTime()+randomOffset))*0.1;
        pPoseStack.translate(0.5f, 0.35f+offset, 0.5f);
        itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.GROUND, getLightLevel(pBlockEntity.getLevel(),
                pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, 1);
        pPoseStack.popPose();
    }
    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

}
