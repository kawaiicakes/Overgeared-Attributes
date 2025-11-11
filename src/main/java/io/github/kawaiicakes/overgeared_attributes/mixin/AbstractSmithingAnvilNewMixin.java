package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.stirdrem.overgeared.block.custom.AbstractSmithingAnvilNew;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSmithingAnvilNew.class)
public abstract class AbstractSmithingAnvilNewMixin extends BaseEntityBlock {
    private AbstractSmithingAnvilNewMixin(Properties pProperties) {
        super(pProperties);
    }

    @WrapOperation(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/stirdrem/overgeared/event/AnvilMinigameEvents;speedUp()V"
            )
    )
    private void noSpeedForFreeHits(
            Operation<Void> original,
            @Local(ordinal = 0) LocalRef<String> quality
    ) {
        if (("skip").equals(quality.get())) return;

        original.call();
    }
}
