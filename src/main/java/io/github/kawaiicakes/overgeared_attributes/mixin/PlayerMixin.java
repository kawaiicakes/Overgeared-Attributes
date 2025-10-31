package io.github.kawaiicakes.overgeared_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.SMITHING_DIFFICULTY;
import static io.github.kawaiicakes.overgeared_attributes.OvergearedAttributes.SMITHING_BONUS;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @WrapMethod(method = "createAttributes")
    private static AttributeSupplier.Builder wrapAttributeSupplier(Operation<AttributeSupplier.Builder> original) {
        // these should be ready to get by here. if not, welp...
        return original.call().add(SMITHING_DIFFICULTY.get()).add(SMITHING_BONUS.get());
    }
}
