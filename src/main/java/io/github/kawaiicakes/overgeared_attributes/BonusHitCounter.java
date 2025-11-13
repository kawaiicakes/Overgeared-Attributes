package io.github.kawaiicakes.overgeared_attributes;

public interface BonusHitCounter {
    default void overgeared_attributes_1_20_1_forge_template$setRemainingBonusHits(int bonus) {
        throw new AssertionError("Mixin interface body for free hits was not injected!");
    }

    default boolean overgeared_attributes_1_20_1_forge_template$appliedBonusHit() {
        throw new AssertionError();
    }
}
