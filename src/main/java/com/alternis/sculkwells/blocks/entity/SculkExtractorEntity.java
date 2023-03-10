package com.alternis.sculkwells.blocks.entity;

import com.alternis.sculkwells.blocks.ModBlocks;
import com.alternis.sculkwells.blocks.api.ExposedSimpleInventoryBlockEntity;
import com.alternis.sculkwells.networking.ModMessages;
import com.alternis.sculkwells.networking.packet.ItemStackSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SculkExtractorEntity extends ExposedSimpleInventoryBlockEntity implements GeoBlockEntity {

    private int progress = 0;
    private int MAX_PROGRESS = 5;
    private int workProgress = 0;
    private static final int MAX_WORK = 200;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("animation.sculk_extractor.off");
    private List<RawAnimation> WORK_ANIMS;
    private Random random = new Random();

    public SculkExtractorEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SCULK_EXTRACTOR.get(), pPos, pBlockState);
        WORK_ANIMS = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            WORK_ANIMS.add(RawAnimation.begin().thenPlay("animation.sculk_extractor.working" + i));
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public ItemStack getRenderStack() {
        ItemStack stack = ItemStack.EMPTY;

        if(!getItemHandler().getItem(0).isEmpty()) {
            stack = getItemHandler().getItem(0);
        }

        return stack;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return stack.is(Blocks.GOLD_BLOCK.asItem());
    }


    public boolean addItem(@Nullable Player player, ItemStack stack, @Nullable InteractionHand hand) {
        for (int i = 0; i < getItemHandler().getContainerSize(); i++) {
            if (getItemHandler().getItem(i).isEmpty()) {
                ItemStack stackToAdd = stack.copy();
                stackToAdd.setCount(1);
                getItemHandler().setItem(i, stackToAdd);

                if (player == null || !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                break;
            }
        }

        return true;
    }

    public boolean isWorking()
    {
        return getItemHandler().getItem(0).is(Blocks.GOLD_BLOCK.asItem());
    }





    public void drops() {
        SimpleContainer inventory = new SimpleContainer(getItemHandler().getContainerSize());
        for (int i = 0; i < getItemHandler().getContainerSize(); i++) {
            inventory.setItem(i, getItemHandler().getItem(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SculkExtractorEntity pEntity) {
        if (pEntity.work() && pEntity.isWorking()) {
            for (int x = blockPos.getX()-3; x < blockPos.getX()+3; x++) {
                for (int y = blockPos.getY()-3; y < blockPos.getY()+3; y++) {
                    for (int z = blockPos.getZ()-3; z < blockPos.getZ()+3; z++) {
                        BlockPos checkedPos = new BlockPos(x,y,z);
                        if (level.getBlockState(checkedPos).getBlock() == Blocks.SCULK) {
                            if (level.getRandom().nextInt(0, 100) > 90) {
                                level.setBlockAndUpdate(checkedPos, Blocks.STONE.defaultBlockState());
                                level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, checkedPos, Block.getId(Blocks.SCULK.defaultBlockState()));
                                pEntity.increaseProgress(1);
                            }
                        }
                        else if (level.getBlockState(checkedPos).getBlock() == Blocks.SCULK_VEIN) {
                            level.removeBlock(checkedPos, false);
                            pEntity.increaseProgress(1);
                        }
                    }
                }
            }
        }
        pEntity.updateBlock();

    }

    public boolean increaseProgress(int amount) {
        progress+= amount;
        if (progress == MAX_PROGRESS) {
            resetProgress();
            if (getItemHandler().getItem(0).is(Blocks.GOLD_BLOCK.asItem())) {
                getLevel().playLocalSound(getBlockPos(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.AMBIENT, 1f, 1f, true);
                getItemHandler().setItem(0, ModBlocks.SCULK_IRON_BLOCK.get().asItem().getDefaultInstance());
            }

        }
        return false;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        getItemHandler().setItem(index, stack);
        ModMessages.sendToClients(new ItemStackSyncS2CPacket(getItemHandler(), worldPosition));
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = getItemHandler().removeItem(index, count);
        ModMessages.sendToClients(new ItemStackSyncS2CPacket(getItemHandler(), worldPosition));
        return stack;
    }


    public boolean work() {
        workProgress++;
        if (workProgress >= MAX_WORK) {
            workProgress = 0;
            return true;
        }
        return false;
    }



    private void resetProgress() {
        progress = 0;
    }

    public boolean isFull() {
        return !getItemHandler().getItem(0).isEmpty();
    }


    @Override
    protected SimpleContainer createItemHandler() {
        return new SimpleContainer(1) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        };
    }

    public void setHandler(Container container) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            getItemHandler().setItem(i, container.getItem(i));
        }
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event) {
        if (!this.isWorking()) {
            event.getController().setAnimation(IDLE_ANIM);

        }
        else {
            int anim = random.nextInt(WORK_ANIMS.size());
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED || event.getController().hasAnimationFinished() || event.getController().getCurrentRawAnimation() == IDLE_ANIM) {
                event.getController().setAnimation(WORK_ANIMS.get(anim));
            }

        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
