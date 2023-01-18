package com.alternis.sculkwells;

import com.alternis.sculkwells.blocks.ModBlocks;
import com.alternis.sculkwells.blocks.entity.ModBlockEntities;
import com.alternis.sculkwells.entity.ModEntityTypes;
import com.alternis.sculkwells.entity.client.ProteanGuardRenderer;
import com.alternis.sculkwells.items.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SculkWells.MOD_ID)
public class SculkWells
{
    public static final String MOD_ID = "sculkwells";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SculkWells()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.register(this);
        ModBlockEntities.register(modEventBus);
        GeckoLib.initialize();



    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntityTypes.PROTEAN_GUARD.get(), ProteanGuardRenderer::new);
        }
    }

    @SubscribeEvent
    public void buildContents(CreativeModeTabEvent.BuildContents event) {
        // Add to ingredients tab
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.DARK_DRILL);
        }
        if (event.getTab() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.PROTEAN_GUARD_SPAWN_EGG);
        }



    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

}
