package com.alternis.sculkwells.blocks.entity;

import ca.weblite.objc.Client;
import com.alternis.sculkwells.entity.ModEntityTypes;
import com.alternis.sculkwells.items.ModItems;
import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.ArrayList;
import java.util.List;


public class SculkExtractorEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot)
        {
            setChanged();
        }

    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    private int progress = 0;
    private int MAX_PROGRESS = 80;
    private int workProgress = 0;
    private int MAX_WORK = 200;
    private static Vec3i box;
    private static Vec3i box2;

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
    public Component getDisplayName() {
        return Component.literal("Sculk Extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
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
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(serializeNBT().getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i <itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SculkExtractorEntity pEntity) {

        List<Entity> closeEntities = level.getEntities(null, AABB.of(BoundingBox.fromCorners(box, box2)));
        if (pEntity.work()) {
            for (Entity e : closeEntities)
            {
                e.hurt(DamageSource.MAGIC, 10);
                if (pEntity.increaseProgress(1)) {
                    if (!level.isClientSide()) {
                        EntityType.BEE.spawn(level.getServer().getLevel(ServerLevel.OVERWORLD), blockPos, MobSpawnType.SPAWN_EGG);
                    }

                }
            }
            for (int x = blockPos.getX()-3; x < blockPos.getX()+3; x++) {
                for (int y = blockPos.getY()-3; y < blockPos.getY()+3; y++) {
                    for (int z = blockPos.getZ()-3; z < blockPos.getZ()+3; z++) {
                        BlockPos checkedPos = new BlockPos(x,y,z);
                        if (level.getBlockState(checkedPos).getBlock() == Blocks.SCULK) {
                            if (level.getRandom().nextInt(0, 100) > 90) {
                                level.setBlock(checkedPos, Blocks.STONE.defaultBlockState(), 0);
                                if (pEntity.increaseProgress(1)) {
                                    if (!level.isClientSide()) {
                                        EntityType.BEE.spawn(level.getServer().getLevel(ServerLevel.OVERWORLD), blockPos, MobSpawnType.SPAWN_EGG);
                                        level.playLocalSound(blockPos, SoundEvents.SCULK_BLOCK_BREAK, SoundSource.BLOCKS, 0.5f, 1.0f, false);
                                    }

                                }
                            }


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
            return true;
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
}
