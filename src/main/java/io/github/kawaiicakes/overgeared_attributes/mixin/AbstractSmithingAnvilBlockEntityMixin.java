package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.kawaiicakes.overgeared_attributes.BonusHitCounter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stirdrem.overgeared.block.entity.AbstractSmithingAnvilBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.LOGGER;
import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.SMITHING_BONUS;

@Mixin(AbstractSmithingAnvilBlockEntity.class)
public abstract class AbstractSmithingAnvilBlockEntityMixin implements BonusHitCounter {
    @Unique
    private int overgeared_attributes_1_20_1_forge_template$bonusHits;

    @Unique
    private boolean overgeared_attributes_1_20_1_forge_template$updateFreeHits(double hits) {
        final boolean toReturn = this.overgeared_attributes_1_20_1_forge_template$bonusHits != hits;
        LOGGER.info("Old: {}  New: {}", this.overgeared_attributes_1_20_1_forge_template$bonusHits, hits);
        if (toReturn) {
            this.overgeared_attributes_1_20_1_forge_template$setRemainingBonusHits((int) hits);
        }
        return toReturn;
    }

    @Override
    public void overgeared_attributes_1_20_1_forge_template$setRemainingBonusHits(int bonus) {
        this.overgeared_attributes_1_20_1_forge_template$bonusHits = bonus;
    }

    @Shadow
    public Player getPlayer() {
        throw new AssertionError();
    }

    @WrapMethod(method = "setPlayer")
    private void setInitialFreeHits(Player player, Operation<Void> original) {
        original.call(player);
        this.overgeared_attributes_1_20_1_forge_template$bonusHits = (int) player.getAttributeValue(
                SMITHING_BONUS.get()
        );
    }

    @WrapMethod(method = "increaseForgingProgress")
    private void increaseForgingProcess(Level pLevel, BlockPos pPos, BlockState pState, Operation<Void> original) {
            // FIXME: is there a way to do this without relying on (a potentially null) player?
            //  Adding a null check just kicks the problem further down the line... it might be possible that
            //  a player could be using the anvil but for some reason getPlayer() returns null. Not sure,
            //  need to read where and when that field is updated
        if (
                overgeared_attributes_1_20_1_forge_template$bonusHits > 0
                && overgeared_attributes_1_20_1_forge_template$updateFreeHits(
                        this.getPlayer().getAttributeValue(SMITHING_BONUS.get())
                )
        ) {
            LOGGER.info("Free hit used in increase forging process");
            return;
        }
        original.call(pLevel, pPos, pState);
    }

    @WrapOperation(
            method = "resetProgress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/stirdrem/overgeared/event/ModEvents;resetMinigameForPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"
            )
    )
    private void resetFreeHits(ServerPlayer player, Operation<Void> original) {
        LOGGER.info("Resetting progress");
        // unlike with the fix note mentioned above, this might be fine...
        if (player != null) {
            Objects.requireNonNull(player.getAttribute(SMITHING_BONUS.get())).removeModifiers();
        }
        this.overgeared_attributes_1_20_1_forge_template$setRemainingBonusHits(0);
        original.call(player);
    }
}
