package com.alternis.sculkwells.items;

import com.alternis.sculkwells.items.client.SculkExtractorItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
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
import java.util.function.Consumer;

public class SculkExtractorItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private List<RawAnimation> WORK_ANIMS;
    private Random random = new Random();

    public SculkExtractorItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
        WORK_ANIMS = new ArrayList<>();
        for (int i = 2; i <= 8; i++) {
            WORK_ANIMS.add(RawAnimation.begin().thenPlay("animation.sculk_extractor.working" + i));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller", 0, this::predicate));
    }
    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event) {
        int anim = random.nextInt(WORK_ANIMS.size());
        if (event.getController().getAnimationState() == AnimationController.State.STOPPED || event.getController().hasAnimationFinished()) {
            event.getController().setAnimation(WORK_ANIMS.get(anim));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private SculkExtractorItemRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer ==  null){
                    this.renderer = new SculkExtractorItemRenderer();
                }
                return this.renderer;
            }
        });
    }
}
