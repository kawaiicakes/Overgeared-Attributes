package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.kawaiicakes.overgeared_attributes.BonusHitCounter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stirdrem.overgeared.block.entity.AbstractSmithingAnvilBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractSmithingAnvilBlockEntity.class)
public abstract class AbstractSmithingAnvilBlockEntityMixin implements BonusHitCounter {
    @Unique
    private int overgeared_attributes_1_20_1_forge_template$bonusHits;

    @Override
    public void overgeared_attributes_1_20_1_forge_template$setRemainingBonusHits(int bonus) {
        this.overgeared_attributes_1_20_1_forge_template$bonusHits = bonus;
    }

    @WrapMethod(method = "increaseForgingProgress")
    private void increaseForgingProcess(Level pLevel, BlockPos pPos, BlockState pState, Operation<Void> original) {
        if (
                this.overgeared_attributes_1_20_1_forge_template$bonusHits >= 1
                // FIXME: add condition to check if the hit was missed, otherwise this will need to be depleted before you can finish forging
        ) {
            --this.overgeared_attributes_1_20_1_forge_template$bonusHits;
        } else {
            original.call(pLevel, pPos, pState);
        }
    }
}
