package com.alternis.sculkwells.event;

import com.alternis.sculkwells.SculkWells;
import com.alternis.sculkwells.entity.ModEntityTypes;
import com.alternis.sculkwells.entity.custom.ProteanGuardEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = SculkWells.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event)
        {
            event.put(ModEntityTypes.PROTEAN_GUARD.get(), ProteanGuardEntity.setAttributes());
        }
    }
}
