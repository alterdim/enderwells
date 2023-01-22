package com.alternis.sculkwells.blocks.custom;

import com.alternis.sculkwells.networking.packet.VanillaPacketDispatcher;
import com.alternis.sculkwells.blocks.entity.ModBlockEntities;
import com.alternis.sculkwells.blocks.entity.SculkExtractorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SculkExtractor extends BaseEntityBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
    public SculkExtractor(Properties pProperties) {
        super(pProperties.noOcclusion().sound(SoundType.AMETHYST));
    }


    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (pEntity instanceof Warden) {
            pEntity.playSound(SoundEvents.AMETHYST_BLOCK_CHIME);
            pEntity.hurt(DamageSource.MAGIC, 40);
            pEntity.move(MoverType.SELF, new Vec3(10, 0, 0));
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity((pPos));
            if (blockEntity instanceof SculkExtractorEntity) {
                ((SculkExtractorEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (!(world.getBlockEntity(pos) instanceof SculkExtractorEntity extractor)) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty() && extractor.isFull()) {
            boolean result = player.addItem(extractor.getItemHandler().removeItem(0, 1));
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(extractor);
            if (result) {
                return InteractionResult.sidedSuccess(world.isClientSide());
            }

        }
        if (!stack.isEmpty() && !extractor.isFull() && stack.is(Blocks.GOLD_BLOCK.asItem())) {
            boolean result = extractor.addItem(player, stack, hand);
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(extractor);
            if (result) {
                return InteractionResult.sidedSuccess(world.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SculkExtractorEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.SCULK_EXTRACTOR.get(), SculkExtractorEntity::tick);
    }
}
