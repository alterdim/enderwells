package com.alternis.sculkwells.blocks.entity;

import com.alternis.sculkwells.blocks.ModBlocks;
import com.alternis.sculkwells.networking.ModMessages;
import com.alternis.sculkwells.networking.packet.ItemStackSyncS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Mod;


public class SculkExtractorEntity extends SimpleInventoryBlockEntity {

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected final ContainerData data;
    private int progress = 0;
    private int MAX_PROGRESS = 25;
    private int workProgress = 0;
    private int MAX_WORK = 200;

    public ItemStack getRenderStack() {
        ItemStack stack = ItemStack.EMPTY;

        if(!getItemHandler().getItem(0).isEmpty()) {
            stack = getItemHandler().getItem(0);
        }

        return stack;
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

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            getItemHandler().setItem(i, itemStackHandler.getStackInSlot(i));
        }
    }

}
