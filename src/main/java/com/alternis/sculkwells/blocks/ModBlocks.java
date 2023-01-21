package com.alternis.sculkwells.blocks;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.custom.SculkExtractor;
import com.alternis.sculkwells.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SculkWells.MOD_ID);

    public static final RegistryObject<Block> SCULK_EXTRACTOR = registerBlock("sculk_extractor", ()->
            new SculkExtractor(BlockBehaviour.Properties.of(Material.SCULK)));
    public static final RegistryObject<Block> SCULK_IRON_BLOCK = registerBlock("sculk_iron_block", () -> new Block(BlockBehaviour.Properties.of(Material.SCULK)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, ()->new BlockItem(block.get(), new Item.Properties()));
    }
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
