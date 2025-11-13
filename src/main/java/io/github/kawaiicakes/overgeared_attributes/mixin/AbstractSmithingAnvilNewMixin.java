package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.kawaiicakes.overgeared_attributes.BonusHitCounter;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.stirdrem.overgeared.block.custom.AbstractSmithingAnvilNew;
import net.stirdrem.overgeared.block.entity.AbstractSmithingAnvilBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static net.minecraft.sounds.SoundEvents.BELL_BLOCK;

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

    // TODO: add vfx
    @Unique
    private static SoundEvent overgeared_attributes_1_20_1_forge_template$bonusHitEffects() {
        return BELL_BLOCK;
    }

    // FIXME: using all bonus hits causes all sounds to be the bell lol
    @WrapOperation(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
            )
    )
    public void modifySounds(
            Level instance, Player pPlayer, BlockPos pPos, SoundEvent pSound, SoundSource pCategory,
            float pVolume, float pPitch, Operation<Void> original,
            @Local LocalRef<AbstractSmithingAnvilBlockEntity> anvil
    ) {
        @SuppressWarnings("RedundantCast")
        final BonusHitCounter counter = (BonusHitCounter) ((Object) anvil.get());
        SoundEvent toPlay = counter.overgeared_attributes_1_20_1_forge_template$appliedBonusHit()
                ? overgeared_attributes_1_20_1_forge_template$bonusHitEffects()
                : pSound;
        float newPitch = counter.overgeared_attributes_1_20_1_forge_template$appliedBonusHit()
                ? 1.23F
                : pPitch;
        original.call(instance, pPlayer, pPos, toPlay, pCategory, pVolume, newPitch);
    }
}
