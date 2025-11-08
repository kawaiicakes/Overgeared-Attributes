package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.stirdrem.overgeared.block.custom.AbstractSmithingAnvilNew;
import net.stirdrem.overgeared.networking.packet.PacketSendCounterC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSmithingAnvilNew.class)
public abstract class AbstractSmithingAnvilNewMixin extends BaseEntityBlock {
    private AbstractSmithingAnvilNewMixin(Properties pProperties) {
        super(pProperties);
    }

    // FIXME: something is causing behaviour of anvil to be weird. Doesn't seem to reset progress between attempts and stuff
    @WrapOperation(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/stirdrem/overgeared/networking/ModMessages;sendToServer(Ljava/lang/Object;)V"
            )
    )
    private <MSG> void interruptPacketsForFreeHits(
            MSG message,
            Operation<Void> original,
            @Local(argsOnly = true) LocalRef<BlockPos> pos,
            @Local(ordinal = 0) LocalRef<String> quality
    ) {
        final PacketSendCounterC2SPacket dispatchedMsg = ("skip").equals(quality.get())
                ? new PacketSendCounterC2SPacket(pos.get(), "poor")
                : (PacketSendCounterC2SPacket) message;

        original.call(dispatchedMsg);
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
