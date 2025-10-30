package io.github.kawaiicakes.overgeared_attributes.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.stirdrem.overgeared.event.AnvilMinigameEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.SMITHING_DIFFICULTY;

@Mixin(AnvilMinigameEvents.class)
public class AnvilMinigameEventsMixin {
    @WrapOperation(
            method = "shrinkAndShiftZones",
            at = @At(value = "FIELD", target = "Lnet/stirdrem/overgeared/event/AnvilMinigameEvents;zoneShrinkFactor:F")
    )
    private static float wrapShrinkFactorWhenShrinking(Operation<Float> original) {
        assert Minecraft.getInstance().player != null; // should be fine to do considering where this is called
        final float originalCall = original.call();
        // FIXME - does not update. At this time I think it's because this inject is incorrect, and because attributes might not be updated client-side
        final float modifierValue = ((float) Minecraft.getInstance().player.getAttributeValue(SMITHING_DIFFICULTY.get()));
        LogUtils.getLogger().info("Original: " + originalCall);
        LogUtils.getLogger().info("Modifier: " + modifierValue);
        return originalCall * modifierValue;
    }
}
