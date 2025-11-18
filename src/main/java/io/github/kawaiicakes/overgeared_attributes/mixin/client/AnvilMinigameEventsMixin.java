package io.github.kawaiicakes.overgeared_attributes.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.kawaiicakes.overgeared_attributes.ArrowPosLerper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.stirdrem.overgeared.event.AnvilMinigameEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.*;

@Mixin(value = AnvilMinigameEvents.class, remap = false)
public abstract class AnvilMinigameEventsMixin implements ArrowPosLerper {
    @Shadow
    public static int perfectZoneStart;
    @Shadow
    public static int perfectZoneEnd;
    @Shadow
    public static int goodZoneStart;
    @Shadow
    public static int goodZoneEnd;
    @Shadow
    public static float arrowSpeed;

    @Shadow
    public static float getArrowPosition() {
        throw new AssertionError();
    }

    @Unique
    private static int overgeared_attributes_1_20_1_forge_template$bonusHits;
    @Unique
    private static float overgeared_attributes_1_20_1_forge_template$arrowPosProjected;
    @Unique
    private static float overgeared_attributes_1_20_1_forge_template$frameDelta;
    @Unique
    private static boolean overgeared_attributes_1_20_1_forge_template$willMoveDown;

    @Override
    public void overgeared_attributes_1_20_1_forge_template$updateFrameDelta(float tick) {
        overgeared_attributes_1_20_1_forge_template$frameDelta = tick;
    }

    @WrapMethod(method = "getArrowPosition")
    private static float returnLerped(Operation<Float> original) {
        return Mth.lerp(
                overgeared_attributes_1_20_1_forge_template$frameDelta,
                original.call(),
                overgeared_attributes_1_20_1_forge_template$arrowPosProjected
        );
    }

    @WrapOperation(
            method = "onClientTick",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(FF)F"
            )
    )
    private static float setProjectedPos(float a, float b, Operation<Float> original) {
        final float toReturn = original.call(a, b);

        float delta = arrowSpeed * (float)(overgeared_attributes_1_20_1_forge_template$willMoveDown ? -1 : 1);

        final float projected = toReturn + delta;

        overgeared_attributes_1_20_1_forge_template$arrowPosProjected = Mth.clamp(
                projected,
                1.0F,
                100.0F
        );

        if (projected >= 100.0F) {
            overgeared_attributes_1_20_1_forge_template$willMoveDown = true;
        } else if (projected <= 1.0F) {
            overgeared_attributes_1_20_1_forge_template$willMoveDown = false;
        }

        return toReturn;
    }

    @Inject(
            method = "setupForQuality",
            at = @At("TAIL")
    )
    private static void injectAttributes(String quality, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null) return;
        overgeared_attributes_1_20_1_forge_template$bonusHits = (int) Minecraft.getInstance().player.getAttributeValue(SMITHING_BONUS.get());
        LOGGER.info("Setting up for quality with {} free hits", overgeared_attributes_1_20_1_forge_template$bonusHits);
        final float modifierValue = (float) Minecraft.getInstance().player.getAttributeValue(SMITHING_DIFFICULTY.get());
        perfectZoneStart -= (int) modifierValue;
        perfectZoneEnd += (int) modifierValue;
        goodZoneStart -= (int) modifierValue;
        goodZoneEnd += (int) modifierValue;
    }

    @WrapMethod(method = "handleHit")
    private static String applyFreeHits(Operation<String> original) {
        //noinspection ConstantValue
        if (
                overgeared_attributes_1_20_1_forge_template$bonusHits > 0
                && (getArrowPosition() < (float)perfectZoneStart || getArrowPosition() > (float)perfectZoneEnd)
        ) {
            LOGGER.info("Clientside free hits: {}", overgeared_attributes_1_20_1_forge_template$bonusHits);
            overgeared_attributes_1_20_1_forge_template$bonusHits--; // must be done clientside since there is no guarantee the server has updated the attribute yet? Is this true?
            return "skip";
        } else {
            return original.call();
        }
    }

    @ModifyExpressionValue(
            method = "handleHit",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/stirdrem/overgeared/event/AnvilMinigameEvents;arrowPosition:F"
            )
    )
    private static float changeReferencedArrowPos(float original) {
        return Mth.lerp(
                overgeared_attributes_1_20_1_forge_template$frameDelta,
                original,
                overgeared_attributes_1_20_1_forge_template$arrowPosProjected
        );
    }

    @WrapMethod(method = "reset()V")
    private static void resetLerped(Operation<Void> original) {
        overgeared_attributes_1_20_1_forge_template$bonusHits = 0;
        overgeared_attributes_1_20_1_forge_template$arrowPosProjected = 50.0F;
        overgeared_attributes_1_20_1_forge_template$willMoveDown = false;
        original.call();
    }

    @WrapMethod(method = "reset(Ljava/lang/String;)V")
    private static void resetLerped(String blueprintQuality, Operation<Void> original) {
        overgeared_attributes_1_20_1_forge_template$bonusHits = 0;
        overgeared_attributes_1_20_1_forge_template$arrowPosProjected = 50.0F;
        overgeared_attributes_1_20_1_forge_template$willMoveDown = false;
        original.call(blueprintQuality);
    }
}
