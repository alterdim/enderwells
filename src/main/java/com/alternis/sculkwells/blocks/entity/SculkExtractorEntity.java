package com.alternis.sculkwells.blocks.entity;

import com.alternis.sculkwells.networking.packet.VanillaPacketDispatcher;
import com.alternis.sculkwells.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SculkExtractorEntity extends SimpleInventoryBlockEntity {

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    private int progress = 0;
    private int MAX_PROGRESS = 80;
    private int workProgress = 0;
    private int MAX_WORK = 200;
    private static Vec3i box;
    private static Vec3i box2;
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot)
        {
            setChanged();
        }

    };

    public ItemStack getRenderStack() {
        ItemStack stack = ItemStack.EMPTY;

        if(!itemHandler.getStackInSlot(0).isEmpty()) {
            stack = itemHandler.getStackInSlot(2);
        }

        return stack;
    }

    public boolean addItem(@Nullable Player player, ItemStack stack, @Nullable InteractionHand hand) {
        boolean did = false;

        for (int i = 0; i < inventorySize(); i++) {
            if (getItemHandler().getItem(i).isEmpty()) {
                did = true;
                ItemStack stackToAdd = stack.copy();
                stackToAdd.setCount(1);
                getItemHandler().setItem(i, stackToAdd);

                if (player == null || !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                break;
            }
        }

        if (did) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }
        System.out.println(getItemHandler().getItem(0));
        return true;
    }



    public SculkExtractorEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SCULK_EXTRACTOR.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SculkExtractorEntity.this.progress;
                    case 1 -> SculkExtractorEntity.this.MAX_PROGRESS;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SculkExtractorEntity.this.progress = pValue;
                    case 1 -> SculkExtractorEntity.this.MAX_PROGRESS = pValue;
                };

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
        box = new Vec3i(pPos.getX()-5, pPos.getY()-5, pPos.getZ()-5);
        box2 = new Vec3i(pPos.getX()+5, pPos.getY()+5, pPos.getZ()+5);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad(){
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(serializeNBT().getCompound("inventory"));
    }

    public void drops() {

        Containers.dropContents(this.level, this.worldPosition, getItemHandler());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SculkExtractorEntity pEntity) {
        if (pEntity.work()) {
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

        }

    public boolean increaseProgress(int amount) {
        progress+= amount;
        if (progress == MAX_PROGRESS) {
            resetProgress();

            if (level.getBlockState(getBlockPos().below()).getBlock() == Blocks.GOLD_BLOCK) {
                level.setBlockAndUpdate(getBlockPos().below(), ModBlocks.SCULK_IRON_BLOCK.get().defaultBlockState());
                getLevel().playLocalSound(getBlockPos(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.AMBIENT, 1f, 1f, true);
            }

        }
        return false;
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

    private static void craftItem(SculkExtractorEntity pEntity) {
    }

    @Override
    protected SimpleContainer createItemHandler() {
        return new SimpleContainer(16) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        };
    }

    public boolean isFull() {
        return !getItemHandler().getItem(0).isEmpty();
    }


}
