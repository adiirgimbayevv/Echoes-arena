package com.patternforge.echoesarena.player;

import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;
import com.patternforge.echoesarena.stats.ManaProfile;
import com.patternforge.echoesarena.stats.PhysicalStats;

public final class PlayerSelection {

    private static PlayerClass selectedClass = PlayerClass.BRUISER;

    private PlayerSelection() {
    }

    public static PlayerClass getSelectedClass() {
        return selectedClass;
    }

    public static void setSelectedClass(PlayerClass playerClass) {
        selectedClass = playerClass;
    }

    public static PlayerBuild createSelectedBuild() {
        if (selectedClass == PlayerClass.RUNNER) {
            return createRunnerBuild();
        }

        if (selectedClass == PlayerClass.MAGE) {
            return createMageBuild();
        }

        return createBruiserBuild();
    }

    private static PlayerBuild createBruiserBuild() {
        return new PlayerBuild(
            new CombatStats(155f, 6f, 1.12f),
            new PhysicalStats(235f, 170f, 0.90f, 44f, 1.05f),
            new MagicalStats(12f, 0.05f, 0.04f),
            new ManaProfile(100f, 10f, 62f)
        );
    }

    private static PlayerBuild createRunnerBuild() {
        return new PlayerBuild(
            new CombatStats(118f, 2f, 1.02f),
            new PhysicalStats(290f, 210f, 0.70f, 40f, 1.22f),
            new MagicalStats(10f, 0.05f, 0.08f),
            new ManaProfile(95f, 11f, 58f)
        );
    }

    private static PlayerBuild createMageBuild() {
        return new PlayerBuild(
            new CombatStats(108f, 1f, 0.96f),
            new PhysicalStats(232f, 155f, 1.00f, 34f, 1.00f),
            new MagicalStats(34f, 0.16f, 0.12f),
            new ManaProfile(145f, 16f, 75f)
        );
    }
}
