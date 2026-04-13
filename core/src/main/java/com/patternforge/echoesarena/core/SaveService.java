package com.patternforge.echoesarena.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.patternforge.echoesarena.player.PlayerBuild;
import com.patternforge.echoesarena.progression.MetaProgression;
import com.patternforge.echoesarena.progression.UnlockService;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;
import com.patternforge.echoesarena.stats.ManaProfile;
import com.patternforge.echoesarena.stats.PhysicalStats;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SaveService {

    private static final String SAVE_FILE = "save.json";

    private final Json json;

    public SaveService() {
        this.json = new Json();
        this.json.setOutputType(com.badlogic.gdx.utils.JsonWriter.OutputType.json);
    }

    public void save(SaveData data) {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        file.writeString(json.toJson(data), false);
    }

    public SaveData load() {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        if (!file.exists()) {
            return null;
        }
        try {
            return json.fromJson(SaveData.class, file.readString());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasSave() {
        return Gdx.files.local(SAVE_FILE).exists();
    }

    public void deleteSave() {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    public SaveData buildSaveData(PlayerBuild playerBuild, MetaProgression metaProgression,
                                  UnlockService unlockService, int currentStageId, int currentLevel,
                                  int currentXp) {
        SaveData data = new SaveData();
        data.currentStageId = currentStageId;
        data.currentLevel = currentLevel;
        data.currentXp = currentXp;

        CombatStats cs = playerBuild.getCombatStats();
        data.maxHp = cs.getMaxHp();
        data.currentHp = cs.getCurrentHp();
        data.defense = cs.getDefense();
        data.damageMultiplier = cs.getDamageMultiplier();

        PhysicalStats ps = playerBuild.getPhysicalStats();
        data.speed = ps.getSpeed();
        data.dashDistance = ps.getDashDistance();
        data.dashCooldown = ps.getDashCooldown();
        data.attackRange = ps.getAttackRange();
        data.attackSpeed = ps.getAttackSpeed();

        MagicalStats ms = playerBuild.getMagicalStats();
        data.spellPower = ms.getSpellPower();
        data.elementalBonus = ms.getElementalBonus();
        data.abilityCdr = ms.getAbilityCooldownReduction();

        ManaProfile mp = playerBuild.getManaProfile();
        data.maxMana = mp.getMaxMana();
        data.manaRegen = mp.getManaRegenPerSecond();
        data.burstThreshold = mp.getBurstThreshold();

        data.physicalPointsBank = metaProgression.getPhysicalPointsBank();
        data.magicalPointsBank = metaProgression.getMagicalPointsBank();
        data.highestStageCleared = metaProgression.getHighestStageCleared();
        data.stagePointsEarned = new HashMap<>(metaProgression.getStagePointsEarned());

        data.unlockedIds = new HashSet<>(unlockService.getUnlockedIds());

        return data;
    }

    public void applyToPlayerBuild(SaveData data, PlayerBuild build) {
        build.getCombatStats().setMaxHp(data.maxHp);
        build.getCombatStats().setCurrentHp(data.currentHp);
        build.getCombatStats().setDefense(data.defense);
        build.getCombatStats().setDamageMultiplier(data.damageMultiplier);

        build.getPhysicalStats().setSpeed(data.speed);
        build.getPhysicalStats().setDashDistance(data.dashDistance);
        build.getPhysicalStats().setDashCooldown(data.dashCooldown);
        build.getPhysicalStats().setAttackRange(data.attackRange);
        build.getPhysicalStats().setAttackSpeed(data.attackSpeed);

        build.getMagicalStats().setSpellPower(data.spellPower);
        build.getMagicalStats().setElementalBonus(data.elementalBonus);
        build.getMagicalStats().setAbilityCooldownReduction(data.abilityCdr);

        build.getManaProfile().setMaxMana(data.maxMana);
        build.getManaProfile().setManaRegenPerSecond(data.manaRegen);
        build.getManaProfile().setBurstThreshold(data.burstThreshold);
    }

    public void applyToMetaProgression(SaveData data, MetaProgression metaProgression) {
        metaProgression.restoreState(
                data.physicalPointsBank,
                data.magicalPointsBank,
                data.highestStageCleared,
                data.stagePointsEarned != null ? data.stagePointsEarned : new HashMap<>()
        );
    }

    public void applyToUnlockService(SaveData data, UnlockService unlockService) {
        if (data.unlockedIds != null) {
            unlockService.restoreFrom(data.unlockedIds);
        }
    }

    public static class SaveData {
        public int currentStageId;
        public int currentLevel;
        public int currentXp;

        public float maxHp;
        public float currentHp;
        public float defense;
        public float damageMultiplier;

        public float speed;
        public float dashDistance;
        public float dashCooldown;
        public float attackRange;
        public float attackSpeed;

        public float spellPower;
        public float elementalBonus;
        public float abilityCdr;

        public float maxMana;
        public float manaRegen;
        public float burstThreshold;

        public int physicalPointsBank;
        public int magicalPointsBank;
        public int highestStageCleared;
        public Map<String, Integer> stagePointsEarned;

        public Set<String> unlockedIds;
    }
}
