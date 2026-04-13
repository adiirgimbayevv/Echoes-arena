package com.patternforge.echoesarena.ability;

import com.patternforge.echoesarena.progression.UnlockService;

import java.util.HashMap;
import java.util.Map;

public class AbilityComboResolver {

    private static final class ComboKey {
        final ElementType a;
        final ElementType b;

        ComboKey(ElementType a, ElementType b) {
            if (a.ordinal() <= b.ordinal()) {
                this.a = a;
                this.b = b;
            } else {
                this.a = b;
                this.b = a;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ComboKey)) return false;
            ComboKey other = (ComboKey) o;
            return this.a == other.a && this.b == other.b;
        }

        @Override
        public int hashCode() {
            return 31 * a.hashCode() + b.hashCode();
        }
    }

    private final Map<ComboKey, ComboDefinition> combos;
    private final UnlockService unlockService;

    public AbilityComboResolver(UnlockService unlockService) {
        this.unlockService = unlockService;
        this.combos = new HashMap<>();
        registerDefaultCombos();
    }

    public void registerCombo(ElementType a, ElementType b,
                               ElementType result, String unlockId,
                               float bonusDamageMultiplier, float bonusAoeRadius) {
        combos.put(new ComboKey(a, b),
                new ComboDefinition(result, unlockId, bonusDamageMultiplier, bonusAoeRadius));
    }

    public ComboResult resolve(ElementType a, ElementType b, ElementProgress progress) {
        ComboKey key = new ComboKey(a, b);
        ComboDefinition def = combos.get(key);
        if (def == null) {
            return ComboResult.none();
        }
        if (!unlockService.isUnlocked(def.unlockId)) {
            return ComboResult.none();
        }
        if (!progress.isReady(a) || !progress.isReady(b)) {
            return ComboResult.none();
        }
        progress.consume(a);
        progress.consume(b);
        return new ComboResult(def.resultElement, def.bonusDamageMultiplier, def.bonusAoeRadius, true);
    }

    private void registerDefaultCombos() {
        registerCombo(ElementType.FIRE, ElementType.EARTH,
                ElementType.LAVA, "combo_fire_earth", 1.5f, 8f);
        registerCombo(ElementType.FIRE, ElementType.ICE,
                ElementType.STEAM, "combo_fire_ice", 1.3f, 12f);
    }

    public static class ComboResult {
        public final ElementType resultElement;
        public final float bonusDamageMultiplier;
        public final float bonusAoeRadius;
        public final boolean triggered;

        public ComboResult(ElementType resultElement, float bonusDamageMultiplier,
                           float bonusAoeRadius, boolean triggered) {
            this.resultElement = resultElement;
            this.bonusDamageMultiplier = bonusDamageMultiplier;
            this.bonusAoeRadius = bonusAoeRadius;
            this.triggered = triggered;
        }

        public static ComboResult none() {
            return new ComboResult(ElementType.NONE, 1f, 0f, false);
        }
    }

    private static class ComboDefinition {
        final ElementType resultElement;
        final String unlockId;
        final float bonusDamageMultiplier;
        final float bonusAoeRadius;

        ComboDefinition(ElementType resultElement, String unlockId,
                        float bonusDamageMultiplier, float bonusAoeRadius) {
            this.resultElement = resultElement;
            this.unlockId = unlockId;
            this.bonusDamageMultiplier = bonusDamageMultiplier;
            this.bonusAoeRadius = bonusAoeRadius;
        }
    }
}
