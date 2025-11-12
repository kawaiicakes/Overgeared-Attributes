package io.github.kawaiicakes.overgeared_attributes.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.kawaiicakes.overgeared_attributes.ArrowPosReturner;
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

@Mixin(AnvilMinigameEvents.class)
public abstract class AnvilMinigameEventsMixin implements ArrowPosReturner {
    @Shadow
    public static float arrowPosition;
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
    @Unique
    private static int overgeared_attributes_1_20_1_forge_template$bonusHits;
    @Unique
    private static float overgeared_attributes_1_20_1_forge_template$arrowPosProjected;
    @Unique
    private static boolean overgeared_attributes_1_20_1_forge_template$willMoveDown;

    @Override
    public float overgeared_attributes_1_20_1_forge_template$getProjectedPos() {
        return overgeared_attributes_1_20_1_forge_template$arrowPosProjected;
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
        if (
                overgeared_attributes_1_20_1_forge_template$bonusHits > 0
                && (arrowPosition < (float)perfectZoneStart || arrowPosition > (float)perfectZoneEnd)
        ) {
            LOGGER.info("Clientside free hits: {}", overgeared_attributes_1_20_1_forge_template$bonusHits);
            overgeared_attributes_1_20_1_forge_template$bonusHits--; // must be done clientside since there is no guarantee the server has updated the attribute yet? Is this true?
            return "skip";
        } else {
            return original.call();
        }
    }
}
