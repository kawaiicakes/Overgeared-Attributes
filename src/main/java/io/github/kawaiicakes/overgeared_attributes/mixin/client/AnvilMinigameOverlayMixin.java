package io.github.kawaiicakes.overgeared_attributes.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.kawaiicakes.overgeared_attributes.ArrowPosReturner;
import net.minecraft.util.Mth;
import net.stirdrem.overgeared.client.AnvilMinigameOverlay;
import net.stirdrem.overgeared.event.AnvilMinigameEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.LOGGER;

@Mixin(AnvilMinigameOverlay.class)
public abstract class AnvilMinigameOverlayMixin {
    // Really not sure that this is a good idea, but I don't know how else I can access its projected arrow pos @Unique otherwise
    @SuppressWarnings("InstantiationOfUtilityClass")
    @Unique
    private static final AnvilMinigameEvents EVENTS_SINGLETON = new AnvilMinigameEvents();

    // Oh, this looks so brittle LMAO
    @Definition(id = "arrowX", local = @Local(type = int.class, name = "arrowX"))
    @Expression("arrowX")
    @ModifyExpressionValue(
            method = "lambda$static$0",
            at = @At(value = "MIXINEXTRAS:EXPRESSION")
    )
    private static int lerpArrow(
            int original,
            @Local(argsOnly = true) float delta, @Local(name = "barX") int barX, @Local(name = "barWidth") int barWidth
    ) {
        // Lerp to the future value rather than from an old one so minigame has precision still

        @SuppressWarnings("RedundantCast")
        final float projected = ((ArrowPosReturner) ((Object) EVENTS_SINGLETON)).overgeared_attributes_1_20_1_forge_template$getProjectedPos();
        final float projectedArrowX = barX + (int)((float)barWidth * projected / 100.0F) - 5;

        final int toReturn = Mth.lerpInt(
                delta,
                original,
                (int) projectedArrowX
        );

        LOGGER.info("Current {}, Projected {}, Lerp {}", original, projectedArrowX, toReturn);

        return toReturn;
    }
}
