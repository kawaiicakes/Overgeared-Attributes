package io.github.kawaiicakes.overgeared_attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(OvergearedAttributes.MOD_ID)
public final class OvergearedAttributes {
    public static final String MOD_ID = "overgeared_attributes";

    // TODO - add lerp to arrow in minigame
    public OvergearedAttributes() {
        MinecraftForge.EVENT_BUS.register(this);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ATTRIBUTES.register(modEventBus);
    }

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(
            ForgeRegistries.ATTRIBUTES,
            "overgeared" // Would look prettier
    );

    public static final RegistryObject<Attribute> SMITHING_DIFFICULTY = ATTRIBUTES.register(
        "smithing.difficulty",
            () -> new RangedAttribute(
                    "attribute.name.smithing.difficulty",
                    0.0D,
                    0.0D,
                    50.0D
            )
    );

    public static final RegistryObject<Attribute> SMITHING_BONUS = ATTRIBUTES.register(
            "smithing.bonus",
            () -> new RangedAttribute(
                    "attribute.name.smithing.bonus",
                    0.0D,
                    0.0D,
                    100.0D
            )
    );
}
