package com.alternis.sculkwells.items;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.ModBlocks;
import com.alternis.sculkwells.entity.ModEntityTypes;
import com.alternis.sculkwells.items.custom.SculkExtractorItem;
import com.alternis.sculkwells.items.custom.ShattererItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SculkWells.MOD_ID);

    public static final RegistryObject<Item> DARK_DRILL = ITEMS.register("dark_drill", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCULK_IRON_INGOT = ITEMS.register("sculk_iron_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SCULK_EXTRATOR_ITEM = ITEMS.register("sculk_extractor",
            () -> new SculkExtractorItem(ModBlocks.SCULK_EXTRACTOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> SHATTERER_ITEM = ITEMS.register("shatterer",
            () -> new ShattererItem(ModBlocks.SHATTERER.get(), new Item.Properties()));

    public static final RegistryObject<Item> PROTEAN_GUARD_SPAWN_EGG =
            ITEMS.register("protean_guard_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PROTEAN_GUARD,0x620afa, 0x000000, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

    }



}
