package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.stirdrem.overgeared.block.custom.AbstractSmithingAnvilNew;
import net.stirdrem.overgeared.block.entity.AbstractSmithingAnvilBlockEntity;
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

    // TODO: add sounds and effects on bonus hit use
    // FIXME: fail or complete sounds playing on last hit even if bonus hit is used.
    @Definition(id = "anvil", local = @Local(type = AbstractSmithingAnvilBlockEntity.class))
    @Definition(id = "getHitsRemaining", method = "Lnet/stirdrem/overgeared/block/entity/AbstractSmithingAnvilBlockEntity;getHitsRemaining()I")
    @Expression("anvil.getHitsRemaining() == 1")
    @ModifyExpressionValue(
            method = "use",
            at = @At(
                    value = "MIXINEXTRAS:EXPRESSION"
            )
    )
    public boolean interruptSounds(boolean original) {
        return original;
    }
}
