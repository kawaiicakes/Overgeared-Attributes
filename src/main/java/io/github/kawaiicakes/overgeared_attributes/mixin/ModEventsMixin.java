package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.kawaiicakes.overgeared_attributes.BonusHitCounter;
import net.minecraft.server.level.ServerPlayer;
import net.stirdrem.overgeared.block.entity.AbstractSmithingAnvilBlockEntity;
import net.stirdrem.overgeared.event.ModEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModEvents.class)
public class ModEventsMixin {
    @WrapOperation(
            method = "resetMinigameForPlayer(Lnet/minecraft/server/level/ServerPlayer;)V",
            at = @At(value = "INVOKE", target = "Lnet/stirdrem/overgeared/block/entity/AbstractSmithingAnvilBlockEntity;setProgress(I)V")
    )
    private static void resetFreeHitsForPlayer(
            AbstractSmithingAnvilBlockEntity instance, int progress, Operation<Void> original,
            @Local(argsOnly = true) LocalRef<ServerPlayer> player
    ) {
        //noinspection RedundantCast
        ((BonusHitCounter) ((Object) instance)).overgeared_attributes_1_20_1_forge_template$setRemainingBonusHits(
                // (int) player.get().getAttributeValue(SMITHING_BONUS.get())
                3
        );
        original.call(instance, progress);
    }

    @WrapOperation(
            method = "resetMinigameForPlayer(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/stirdrem/overgeared/block/entity/AbstractSmithingAnvilBlockEntity;setProgress(I)V")
    )
    private static void resetFreeHitsForPlayerPos(
            AbstractSmithingAnvilBlockEntity instance, int progress, Operation<Void> original,
            @Local(argsOnly = true) LocalRef<ServerPlayer> player
    ) {
        //noinspection RedundantCast
        ((BonusHitCounter) ((Object) instance)).overgeared_attributes_1_20_1_forge_template$setRemainingBonusHits(
                // (int) player.get().getAttributeValue(SMITHING_BONUS.get())
                3
        );
        original.call(instance, progress);
    }

    @WrapOperation(
            method = "resetMinigameForAnvil",
            at = @At(value = "INVOKE", target = "Lnet/stirdrem/overgeared/block/entity/AbstractSmithingAnvilBlockEntity;setProgress(I)V")
    )
    private static void resetFreeHitsForAnvil(
            AbstractSmithingAnvilBlockEntity instance, int progress, Operation<Void> original
    ) {
        //noinspection RedundantCast
        ((BonusHitCounter) ((Object) instance)).overgeared_attributes_1_20_1_forge_template$setRemainingBonusHits(0);
        original.call(instance, progress);
    }
}
