package com.alternis.sculkwells.networking.packet;

import com.alternis.sculkwells.blocks.entity.SculkExtractorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ItemStackSyncS2CPacket {
    private Container container;
    private final BlockPos pos;

    public ItemStackSyncS2CPacket(Container newContainer, BlockPos pos) {
        this.pos = pos;
        this.container = newContainer;
    }

    public ItemStackSyncS2CPacket(FriendlyByteBuf buf) {
        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        container = new SimpleContainer(collection.size());
        for (int i = 0; i < collection.size(); i++) {
            container.setItem(i, collection.get(i));
        }

        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        Collection<ItemStack> list = new ArrayList<>();
        for(int i = 0; i < container.getContainerSize(); i++) {
            list.add(container.getItem(i));
        }

        buf.writeCollection(list, FriendlyByteBuf::writeItem);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof SculkExtractorEntity blockEntity) {
                blockEntity.setHandler(this.container);
            }
        });
        return true;
    }
}
