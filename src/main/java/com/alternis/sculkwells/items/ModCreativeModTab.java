package com.alternis.sculkwells.items;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.blocks.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SculkWells.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModTab {
    public static CreativeModeTab SCULK_WELLS_CREATIVE_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        SCULK_WELLS_CREATIVE_TAB = event.registerCreativeModeTab(new ResourceLocation(SculkWells.MOD_ID, "sculk_wells_tab"),
                builder -> builder.icon(() -> new ItemStack(ModBlocks.SCULK_EXTRACTOR.get())).title(Component.literal("Sculk Wells")).build());
    }
}
