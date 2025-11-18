package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.stirdrem.overgeared.networking.packet.PacketSendCounterC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.LOGGER;
import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.SMITHING_BONUS;

@Mixin(value = PacketSendCounterC2SPacket.class, remap = false)
public abstract class PacketSendCounterC2SPacketMixin {
    @WrapOperation(
            method = "lambda$handle$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/stirdrem/overgeared/block/custom/AbstractSmithingAnvilNew;setQuality(Ljava/lang/String;)V"
            )
    )
    private static void updateFreeHits(String quality, Operation<Void> original, @Local LocalRef<ServerPlayer> sender) {
        if (("skip").equals(quality)) {
            original.call("poor");
            Objects.requireNonNull(sender.get().getAttribute(SMITHING_BONUS.get())).addTransientModifier(
                    new AttributeModifier("used_free_hit", -1, AttributeModifier.Operation.ADDITION)
            );
            // DEBUG ONLY
            LOGGER.info("Quality on serverside read as skip, remaining bonus hits: {}", Objects.requireNonNull(sender.get().getAttribute(SMITHING_BONUS.get())).getValue());
            return;
        }
        LOGGER.info("handling C2S packet without skip: {}", Objects.requireNonNull(sender.get().getAttribute(SMITHING_BONUS.get())).getValue());
        original.call(quality);
    }
}
