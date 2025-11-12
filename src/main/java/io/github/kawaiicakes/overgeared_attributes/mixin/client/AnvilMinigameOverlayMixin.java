package io.github.kawaiicakes.overgeared_attributes.mixin.client;

import io.github.kawaiicakes.overgeared_attributes.ArrowPosLerper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.stirdrem.overgeared.client.AnvilMinigameOverlay;
import net.stirdrem.overgeared.event.AnvilMinigameEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMinigameOverlay.class)
public abstract class AnvilMinigameOverlayMixin {
    // Really not sure that this is a good idea, but I don't know how else I can access its projected arrow pos @Unique otherwise
    @SuppressWarnings("InstantiationOfUtilityClass")
    @Unique
    private static final AnvilMinigameEvents EVENTS_SINGLETON = new AnvilMinigameEvents();

    @Inject(
            method = "lambda$static$0",
            at = @At(value = "HEAD")
    )
    private static void lerpArrow(
            ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight, CallbackInfo ci
    ) {
        //noinspection RedundantCast
        ((ArrowPosLerper) ((Object) EVENTS_SINGLETON)).overgeared_attributes_1_20_1_forge_template$updateFrameDelta(partialTick);
    }
}
