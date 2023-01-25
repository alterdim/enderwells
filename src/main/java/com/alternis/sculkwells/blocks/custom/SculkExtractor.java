package com.alternis.sculkwells.blocks.custom;

import com.alternis.sculkwells.blocks.entity.ModBlockEntities;
import com.alternis.sculkwells.blocks.entity.SculkExtractorEntity;
import com.alternis.sculkwells.networking.ModMessages;
import com.alternis.sculkwells.networking.packet.ItemStackSyncS2CPacket;
import com.alternis.sculkwells.particles.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
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
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SculkExtractor extends BaseEntityBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    public SculkExtractor(Properties pProperties) {
        super(pProperties.noOcclusion().sound(SoundType.AMETHYST));
    }
    public static final List<BlockPos> SCULK_BLOCKS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2).filter((block) -> {
        return Math.abs(block.getX()) == 2 || Math.abs(block.getZ()) == 2;
    }).map(BlockPos::immutable).toList();

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static boolean isValidSculk(Level level, BlockPos pPos, BlockPos sculkPos) {
        return level.getBlockState(pPos.offset(sculkPos)).getBlock() == Blocks.SCULK && level.isEmptyBlock(pPos.offset(sculkPos.getX() / 2, sculkPos.getY(), sculkPos.getZ() / 2));
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
        return RenderShape.ENTITYBLOCK_ANIMATED;
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
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        if ((pLevel.getBlockEntity(pPos) instanceof SculkExtractorEntity extractor) && extractor.isWorking()) {
            for(BlockPos blockpos : SCULK_BLOCKS) {
                if (pRandom.nextInt(2) == 0 && isValidSculk(pLevel, pPos, blockpos)) {
                    pLevel.addParticle(ModParticles.SCULK_SOUL_PARTICLES.get(), (double)pPos.getX() + 0.5D, (double)pPos.getY() + 2.0D, (double)pPos.getZ() + 0.5D, (double)((float)blockpos.getX() + pRandom.nextFloat()) - 0.5D, (double)((float)blockpos.getY() - pRandom.nextFloat() - 1.0F), (double)((float)blockpos.getZ() + pRandom.nextFloat()) - 0.5D);
                }
            }
        }



    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (!(world.getBlockEntity(pos) instanceof SculkExtractorEntity extractor)) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty() && extractor.isFull()) {
            boolean result = player.addItem(extractor.getItemHandler().removeItem(0, 1));
            if (result) {
                return InteractionResult.sidedSuccess(world.isClientSide());
            }

        }
        if (!stack.isEmpty() && !extractor.isFull() && stack.is(Blocks.GOLD_BLOCK.asItem())) {
            boolean result = extractor.addItem(player, stack, hand);
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
