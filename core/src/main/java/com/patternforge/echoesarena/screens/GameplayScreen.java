package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.patternforge.echoesarena.ability.AbilityService;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.combat.HitData;
import com.patternforge.echoesarena.config.GameConfig;
import com.patternforge.echoesarena.core.AudioService;
import com.patternforge.echoesarena.core.GameContext;
import com.patternforge.echoesarena.enemy.EnemyFactory;
import com.patternforge.echoesarena.enemy.EnemyType;
import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;
import com.patternforge.echoesarena.entity.Projectile;
import com.patternforge.echoesarena.player.PlayerBuild;
import com.patternforge.echoesarena.player.PlayerClass;
import com.patternforge.echoesarena.player.PlayerSelection;
import com.patternforge.echoesarena.progression.LevelUpService;
import com.patternforge.echoesarena.progression.UpgradeBranch;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.ui.UpgradeTreeView;
import com.patternforge.echoesarena.world.MapLoader;
import com.patternforge.echoesarena.world.SpawnService;
import com.patternforge.echoesarena.world.StageDirector;
import com.patternforge.echoesarena.world.StageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class GameplayScreen extends ScreenAdapter {

    private final GameContext context;

    private Player player;

    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    private CombatSystem combatSystem;
    private AbilityService abilityService;
    private EnemyFactory enemyFactory;
    private List<Enemy> enemies;
    private List<HealthPickup> healthPickups;
    private List<ArenaObstacle> arenaObstacles;
    private StageDirector stageDirector;
    private LevelUpService levelUpService;
    private UpgradeTreeView upgradeTreeView;

    private Texture playerTexture;
    private Texture gruntTexture;
    private Texture swarmTexture;
    private Texture tankTexture;
    private Texture sniperTexture;
    private Texture supportTexture;
    private Texture frostNovaTexture;
    private Texture attackAirSheetTexture;
    private Texture dashSheetTexture;
    private Texture fireballSheetTexture;
    private Texture magicSheetTexture;
    private Texture sparksSheetTexture;
    private Texture fogMaskTexture;
    private Texture worldBackdropTexture;
    private Texture rocksTexture;
    private Texture vegetationTexture;
    private Texture floorTilesTexture;
    private Texture dungeonTilesTexture;
    private Texture wallTilesTexture;
    private Texture wallVariationsTexture;
    private Texture dungeonPropsTexture;
    private Texture furnitureTexture;
    private Texture esotericTexture;
    private TextureRegion[][] rockTiles;
    private TextureRegion[][] vegetationTiles;
    private TextureRegion[][] floorTiles;
    private TextureRegion[][] dungeonTiles;
    private TextureRegion[][] wallTiles;
    private TextureRegion[][] wallVariationTiles;
    private TextureRegion[][] dungeonPropTiles;
    private TextureRegion[][] furnitureTiles;
    private TextureRegion[][] esotericTiles;

    private Color arenaColor;
    private Color arenaGridColor;
    private Color arenaBorderColor;
    private Color playerCircuitColor;

    private float meleeAttackCooldown;
    private float guardianShieldVisualTimer;
    private float bossAnnouncementTimer;
    private float bossSpawnFlashTimer;
    private float dashVisualTimer;
    private float rangedAttackCooldown;
    private float fireballCastVisualTimer;
    private float glacialRiftVisualTimer;
    private float finalBossContactAttackTimer;
    private float stateTime;
    private float bossHealthDisplayRatio;
    private boolean wasPlayerDashing;

    private int killedEnemies;
    private int collectedHealthPickups;
    private int currentStageId;
    private int totalStages;

    private boolean isLevelUpUiOpen;
    private boolean isUpgradeTreeOpen;
    private boolean isStageTransitionOpen;
    private boolean archersSpawnedForStage;
    private boolean finalBossSpawnedForStage;
    private boolean finalBossDefeatedForStage;
    private boolean initialized;
    private Enemy activeBoss;
    private final Set<Enemy> stageTunedEnemies = new HashSet<Enemy>();

    private static final float MELEE_ATTACK_RANGE = 72f;
    private static final float MELEE_ATTACK_DAMAGE = 150f;
    private static final float MELEE_ATTACK_MANA_COST = 12f;
    private static final float MELEE_ATTACK_COOLDOWN = 0.40f;
    private static final float MELEE_ATTACK_ANIMATION_TIME = 0.16f;
    private static final float DASH_VISUAL_TIME = 0.20f;
    private static final float ULTIMATE_CAST_VISUAL_TIME = 0.36f;
    private static final float RANGED_ATTACK_COOLDOWN = 0.24f;
    private static final float RANGED_SHOT_SPEED = 560f;
    private static final float RANGED_SHOT_DAMAGE = 120f;
    private static final float RANGED_SHOT_RADIUS = 7f;
    private static final float RANGED_SHOT_MAX_DISTANCE = 880f;
    private static final float RANGED_HOMING_RANGE = 620f;
    private static final float RANGED_HOMING_TURN_RATE = 7.5f;

    private static final float GUARDIAN_SHIELD_RADIUS = 72f;
    private static final float GUARDIAN_SHIELD_KNOCKBACK = 230f;
    private static final float GUARDIAN_SHIELD_WALL_PUSH = 170f;

    private static final float BOSS_ANNOUNCEMENT_TIME = 3.5f;
    private static final float BOSS_FLASH_TIME = 1.1f;
    private static final float FINAL_BOSS_CONTACT_ATTACK_COOLDOWN = 1.35f;
    private static final float FINAL_BOSS_CONTACT_ATTACK_RANGE = 96f;

    private static final float HEALTH_PICKUP_RADIUS = 14f;
    private static final float HEALTH_PICKUP_HEAL = 25f;
    private static final float HEALTH_PICKUP_COLLECT_DISTANCE = 34f;
    private static final float HEALTH_PICKUP_DROP_CHANCE = 0.25f;

    private static final float HEALTH_BAR_HEIGHT = 6f;
    private static final float PLAYER_HEALTH_BAR_WIDTH = 70f;
    private static final float PLAYER_HEALTH_BAR_HEIGHT = 8f;

    private static final float ARENA_SIZE = 1800f;

    public GameplayScreen(GameContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);

        if (initialized) {
            return;
        }

        initialized = true;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.15f);
        glyphLayout = new GlyphLayout();

        float cx = GameConfig.VIEWPORT_WIDTH / 2f;
        float cy = GameConfig.VIEWPORT_HEIGHT / 2f;

        PlayerClass selectedClass = PlayerSelection.getSelectedClass();
        PlayerBuild selectedBuild = PlayerSelection.createSelectedBuild();

        player = new Player(cx, cy, selectedBuild);
        playerCircuitColor = new Color(selectedClass.getColor());
        loadFantasyArt(selectedClass);

        combatSystem = new CombatSystem();
        abilityService = new AbilityService(combatSystem);
        enemyFactory = new EnemyFactory(combatSystem);

        healthPickups = new ArrayList<HealthPickup>();
        arenaObstacles = new ArrayList<ArenaObstacle>();

        MapLoader mapLoader = new MapLoader();
        StageRepository stageRepository = new StageRepository(mapLoader);
        SpawnService spawnService = new SpawnService(enemyFactory);

        stageDirector = new StageDirector(stageRepository, spawnService);

        currentStageId = 1;
        totalStages = stageRepository.getTotalStages();

        applyArenaThemeForStage(currentStageId);
        rebuildArenaObstaclesForStage(currentStageId);

        stageDirector.loadStage(currentStageId);
        enemies = stageDirector.getActiveEnemies();

        spreadEnemiesAroundArena();
        resetStageSpecialState();

        levelUpService = new LevelUpService();
        Skin skin = context.getAssetService().getSkin();
        upgradeTreeView = new UpgradeTreeView(skin);
        context.getAudioService().playMusic(AudioService.MUSIC_ARENA, true);

        isLevelUpUiOpen = false;
        isUpgradeTreeOpen = false;
        isStageTransitionOpen = false;
        wasPlayerDashing = false;
        activeBoss = null;
        bossHealthDisplayRatio = 1f;
        bossSpawnFlashTimer = 0f;

        meleeAttackCooldown = 0f;
        guardianShieldVisualTimer = 0f;
        bossAnnouncementTimer = 0f;
        dashVisualTimer = 0f;
        rangedAttackCooldown = 0f;
        fireballCastVisualTimer = 0f;
        glacialRiftVisualTimer = 0f;
        finalBossContactAttackTimer = 0f;
        stateTime = 0f;

        killedEnemies = 0;
        collectedHealthPickups = 0;
    }

    @Override
    public void render(float delta) {
        if (delta > 0.05f) {
            delta = 0.05f;
        }

        clearScreen();

        if (handleUpgradeTreeInput()) {
            return;
        }

        if (handleLevelUpUi()) {
            return;
        }

        if (isStageTransitionOpen) {
            renderStageTransition();
            handleStageTransitionInput();
            return;
        }

        updateGame(delta);
        renderGame();

        if (player.isDead()) {
            Gdx.input.setInputProcessor(null);
            context.getScreenRouter().goToGameOver();
            return;
        }

        if (canFinishCurrentStage()) {
            if (currentStageId >= totalStages) {
                Gdx.input.setInputProcessor(null);
                context.getScreenRouter().goToVictory();
            } else {
                isStageTransitionOpen = true;
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            openPauseScreen();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.005f, 0.006f, 0.025f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private boolean handleLevelUpUi() {
        if (!levelUpService.isPendingLevelUp() && !isUpgradeTreeOpen) {
            return false;
        }

        if (upgradeTreeView == null) {
            return false;
        }

        if (!isLevelUpUiOpen) {
            isLevelUpUiOpen = true;
            isUpgradeTreeOpen = true;
            upgradeTreeView.show(levelUpService, new UpgradeTreeView.UpgradeTreeListener() {
                @Override
                public void onBranchSelected(UpgradeBranch branch) {
                    levelUpService.applyBranchUpgrade(branch, player.getBuild());
                    if (levelUpService.getPendingSkillPoints() <= 0) {
                        closeUpgradeTree();
                    } else {
                        upgradeTreeView.show(levelUpService, this);
                    }
                }

                @Override
                public void onClosed() {
                    closeUpgradeTree();
                }
            });
        }

        drawGameplayBackgroundOnly();
        upgradeTreeView.render();
        return true;
    }

    private boolean handleUpgradeTreeInput() {
        if (!Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            return false;
        }

        if (isUpgradeTreeOpen || isLevelUpUiOpen) {
            closeUpgradeTree();
        } else {
            isUpgradeTreeOpen = true;
            isLevelUpUiOpen = false;
        }

        return true;
    }

    private void closeUpgradeTree() {
        isLevelUpUiOpen = false;
        isUpgradeTreeOpen = false;
        levelUpService.clearLevelUpNoticeIfSpent();
        Gdx.input.setInputProcessor(null);
    }

    private void updateGame(float delta) {
        spawnArchersForCurrentStageIfNeeded();

        player.update(delta);
        updateDashVisualState();
        keepPlayerInsideArena();
        resolvePlayerObstacleCollisions();

        if (player.wasGuardianShieldTriggered()) {
            guardianShieldVisualTimer = player.getGuardianShieldDuration();
            knockbackEnemiesFromPlayer(GUARDIAN_SHIELD_KNOCKBACK);
            context.getAudioService().playSound(AudioService.SFX_SHIELD);
        }

        combatSystem.update(delta);
        updateTimers(delta);

        stateTime += delta;

        stageDirector.update(delta, player);
        enemies = stageDirector.getActiveEnemies();
        tuneAndDistributeNewEnemies();

        maybeSpawnFinalBossAfterMiniEnemies();

        abilityService.update(delta, player, enemies);
        handleAbilityInput();

        spreadEnemiesIfStacked();
        applyEnemyObstacleAvoidance(delta);
        applyGuardianShieldWall(delta);
        pullIdleEnemiesTowardPlayer(delta);
        resolveEnemyObstacleCollisions();
        keepEnemiesInsideArena();
        applyFinalBossContactDamage();

        handleRangedAttack();
        handleMeleeAttack();
        resolveProjectileCombat();
        collectHealthPickups();
        removeDeadEnemies();

        camera.position.set(player.getPosition().x, player.getPosition().y, 0f);
        camera.update();
    }

    private void updateTimers(float delta) {
        meleeAttackCooldown = Math.max(0f, meleeAttackCooldown - delta);
        guardianShieldVisualTimer = Math.max(0f, guardianShieldVisualTimer - delta);
        bossAnnouncementTimer = Math.max(0f, bossAnnouncementTimer - delta);
        bossSpawnFlashTimer = Math.max(0f, bossSpawnFlashTimer - delta);
        dashVisualTimer = Math.max(0f, dashVisualTimer - delta);
        rangedAttackCooldown = Math.max(0f, rangedAttackCooldown - delta);
        fireballCastVisualTimer = Math.max(0f, fireballCastVisualTimer - delta);
        glacialRiftVisualTimer = Math.max(0f, glacialRiftVisualTimer - delta);
        finalBossContactAttackTimer = Math.max(0f, finalBossContactAttackTimer - delta);

        Enemy boss = getActiveBoss();
        if (boss != null) {
            bossHealthDisplayRatio = MathUtils.lerp(
                bossHealthDisplayRatio,
                Math.max(0f, Math.min(1f, boss.getCombatStats().getHpRatio())),
                Math.min(1f, delta * 7f)
            );
        }
    }

    private void updateDashVisualState() {
        boolean isDashing = player.isDashing();

        if (isDashing && !wasPlayerDashing) {
            dashVisualTimer = DASH_VISUAL_TIME;
        }

        wasPlayerDashing = isDashing;
    }

    private void renderGame() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        drawArena();
        drawHealthPickups();
        drawGuardianShield();

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawSpriteEffects();
        drawPlayer();
        drawEnemies();

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawWorldHealthBars();
        shapeRenderer.end();

        drawNightVisionOverlay();
        drawHud();
        drawBossHealthBar();
        drawBossAnnouncement();
    }

    private void openPauseScreen() {
        Gdx.input.setInputProcessor(null);
        context.getScreenRouter().goToPause(this);
    }

    private void drawGameplayBackgroundOnly() {
        clearScreen();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawArena();
        drawHealthPickups();
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawPlayer();
        drawEnemies();
        batch.end();
    }

    private void handleStageTransitionInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
            || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            startNextStage();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            openPauseScreen();
        }
    }

    private void startNextStage() {
        currentStageId++;

        applyArenaThemeForStage(currentStageId);
        rebuildArenaObstaclesForStage(currentStageId);

        stageDirector.loadStage(currentStageId);
        enemies = stageDirector.getActiveEnemies();

        spreadEnemiesAroundArena();
        resetStageSpecialState();

        healthPickups.clear();
        abilityService.resetStageState();

        isStageTransitionOpen = false;

        meleeAttackCooldown = 0f;
        guardianShieldVisualTimer = 0f;
        bossAnnouncementTimer = 0f;
        bossSpawnFlashTimer = 0f;
        finalBossContactAttackTimer = 0f;
        activeBoss = null;
        bossHealthDisplayRatio = 1f;
    }

    private void resetStageSpecialState() {
        archersSpawnedForStage = false;
        finalBossSpawnedForStage = false;
        finalBossDefeatedForStage = currentStageId < 5;
        activeBoss = null;
        stageTunedEnemies.clear();
    }

    private boolean canFinishCurrentStage() {
        if (!stageDirector.isStageClear()) {
            return false;
        }

        if (currentStageId < 5) {
            return true;
        }

        return finalBossSpawnedForStage && finalBossDefeatedForStage;
    }

    private void renderStageTransition() {
        drawGameplayBackgroundOnly();

        uiCamera.update();

        float screenWidth = uiCamera.viewportWidth;
        float screenHeight = uiCamera.viewportHeight;

        float cardWidth = Math.min(520f, screenWidth - 60f);
        float cardHeight = Math.min(280f, screenHeight - 60f);
        float cardX = (screenWidth - cardWidth) / 2f;
        float cardY = (screenHeight - cardHeight) / 2f;

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0f, 0f, 0f, 0.72f);
        shapeRenderer.rect(0f, 0f, screenWidth, screenHeight);

        shapeRenderer.setColor(0.02f, 0.025f, 0.05f, 0.96f);
        shapeRenderer.rect(cardX, cardY, cardWidth, cardHeight);

        shapeRenderer.setColor(playerCircuitColor);
        drawRectBorder(cardX, cardY, cardWidth, cardHeight, 4f);

        shapeRenderer.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        drawCenteredText("LEVEL " + currentStageId + " COMPLETE", screenWidth / 2f, cardY + cardHeight - 52f);
        drawCenteredText("Go to level " + (currentStageId + 1) + "?", screenWidth / 2f, cardY + cardHeight - 102f);
        drawCenteredText("Next arena will be harder.", screenWidth / 2f, cardY + cardHeight - 145f);
        drawCenteredText("ENTER / SPACE - CONTINUE", screenWidth / 2f, cardY + 82f);
        drawCenteredText("ESC - PAUSE", screenWidth / 2f, cardY + 42f);

        batch.end();
    }

    private void spawnArchersForCurrentStageIfNeeded() {
        if (archersSpawnedForStage) {
            return;
        }

        archersSpawnedForStage = true;

        if (currentStageId < 2) {
            return;
        }

        int archerCount = Math.min(1 + currentStageId / 2, 5);

        for (int i = 0; i < archerCount; i++) {
            Vector2 position = randomSpawnPositionAwayFromPlayer(320f);
            enemies.add(enemyFactory.create(EnemyType.SNIPER, position.x, position.y));
        }

        spreadEnemiesIfStacked();
    }

    private void maybeSpawnFinalBossAfterMiniEnemies() {
        if (currentStageId < 5 || finalBossSpawnedForStage || !stageDirector.isStageClear() || hasAliveEnemies()) {
            return;
        }

        spawnFinalBoss();
    }

    private void spawnFinalBoss() {
        Vector2 bossPosition = randomSpawnPositionAwayFromPlayer(420f);
        Enemy boss = enemyFactory.create(EnemyType.TANK, bossPosition.x, bossPosition.y);

        float bossScale = 1f + (currentStageId - 5) * 0.42f;
        CombatStats stats = boss.getCombatStats();

        stats.setMaxHp(340f * bossScale);
        stats.setCurrentHp(340f * bossScale);
        stats.setDefense(10f + currentStageId * 1.4f);
        stats.setDamageMultiplier(1.35f + currentStageId * 0.10f);
        boss.setSpeedMultiplier(1.25f);
        boss.setMaxHpDamageRatio(0.5f);

        enemies.add(boss);
        activeBoss = boss;
        bossHealthDisplayRatio = 1f;

        finalBossSpawnedForStage = true;
        finalBossDefeatedForStage = false;
        bossAnnouncementTimer = BOSS_ANNOUNCEMENT_TIME;
        bossSpawnFlashTimer = BOSS_FLASH_TIME;
        finalBossContactAttackTimer = 0f;
        context.getAudioService().playSound(AudioService.SFX_GLACIAL_RIFT);
    }

    private Enemy getActiveBoss() {
        if (activeBoss == null) {
            return null;
        }

        if (!activeBoss.isAlive()) {
            activeBoss = null;
            return null;
        }

        return activeBoss;
    }

    private void tuneAndDistributeNewEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy == null || !enemy.isAlive() || stageTunedEnemies.contains(enemy)) {
                continue;
            }

            float stageScale = 1f + (currentStageId - 1) * 0.11f;
            CombatStats stats = enemy.getCombatStats();
            stats.setMaxHp(stats.getMaxHp() * stageScale);
            stats.setCurrentHp(stats.getMaxHp());
            stats.setDefense(stats.getDefense() + (currentStageId - 1) * 0.5f);
            stats.setDamageMultiplier(stats.getDamageMultiplier() + (currentStageId - 1) * 0.04f);

            float minDistanceFromPlayer = 170f + currentStageId * 10f;
            Vector2 spawnPosition = enemy.getPosition();
            if (spawnPosition.dst(player.getPosition()) < minDistanceFromPlayer) {
                Vector2 dir = new Vector2(spawnPosition).sub(player.getPosition());
                if (dir.len2() <= 0.001f) {
                    dir.set(1f, 0f);
                }
                dir.nor().scl(minDistanceFromPlayer + MathUtils.random(18f, 64f));
                spawnPosition.set(player.getPosition()).add(dir);
            }

            keepEnemyInsideArena(enemy);
            resolveSingleEnemyObstacleCollision(enemy);
            updateEnemyBoundingBox(enemy);
            stageTunedEnemies.add(enemy);
        }
    }

    private boolean hasAliveEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                return true;
            }
        }

        return false;
    }

    private boolean isFinalBoss(Enemy enemy) {
        return enemy.getType() == EnemyType.TANK
            && currentStageId >= 5
            && enemy.getCombatStats().getMaxHp() >= 300f;
    }

    private void applyFinalBossContactDamage() {
        Enemy boss = getActiveBoss();
        if (boss == null || finalBossContactAttackTimer > 0f || player.isDead()) {
            return;
        }

        if (boss.getPosition().dst(player.getPosition()) > FINAL_BOSS_CONTACT_ATTACK_RANGE) {
            return;
        }

        player.takeDamage(player.getCombatStats().getMaxHp() * 0.5f);
        finalBossContactAttackTimer = FINAL_BOSS_CONTACT_ATTACK_COOLDOWN;
        context.getAudioService().playSound(AudioService.SFX_ENEMY_HIT);
    }

    private Vector2 randomSpawnPositionAwayFromPlayer(float minDistanceFromPlayer) {
        float halfSize = ARENA_SIZE / 2f;
        float centerX = GameConfig.VIEWPORT_WIDTH / 2f;
        float centerY = GameConfig.VIEWPORT_HEIGHT / 2f;

        float minX = centerX - halfSize + 80f;
        float maxX = centerX + halfSize - 80f;
        float minY = centerY - halfSize + 80f;
        float maxY = centerY + halfSize - 80f;

        for (int i = 0; i < 40; i++) {
            Vector2 position = new Vector2(MathUtils.random(minX, maxX), MathUtils.random(minY, maxY));

            if (position.dst(player.getPosition()) >= minDistanceFromPlayer && !isInsideObstacle(position, 28f)) {
                return position;
            }
        }

        return new Vector2(centerX + MathUtils.random(-420f, 420f), centerY + MathUtils.random(-420f, 420f));
    }

    private boolean isInsideObstacle(Vector2 position, float radius) {
        for (ArenaObstacle obstacle : arenaObstacles) {
            if (circleIntersectsRectangle(position, radius, obstacle.bounds)) {
                return true;
            }
        }

        return false;
    }

    private void handleAbilityInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            if (abilityService.castFireball(player)) {
                fireballCastVisualTimer = ULTIMATE_CAST_VISUAL_TIME;
                context.getAudioService().playSound(AudioService.SFX_ULTIMATE);
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (abilityService.castGlacialRift(player, enemies)) {
                glacialRiftVisualTimer = ULTIMATE_CAST_VISUAL_TIME;
                context.getAudioService().playSound(AudioService.SFX_ULTIMATE);
            }
        }
    }

    private boolean isEnemyFrozen(Enemy enemy) {
        return abilityService.isEnemyFrozen(enemy);
    }

    private void spreadEnemiesAroundArena() {
        if (enemies == null) {
            return;
        }

        float angleStep = 360f / Math.max(1, enemies.size());

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);

            if (enemy == null || !enemy.isAlive()) {
                continue;
            }

            float angle = angleStep * i + MathUtils.random(-18f, 18f);
            float distance = 360f + MathUtils.random(-110f, 220f);

            enemy.getPosition().set(
                player.getPosition().x + MathUtils.cosDeg(angle) * distance,
                player.getPosition().y + MathUtils.sinDeg(angle) * distance
            );

            keepEnemyInsideArena(enemy);
            resolveSingleEnemyObstacleCollision(enemy);
            updateEnemyBoundingBox(enemy);
        }
    }

    private void spreadEnemiesIfStacked() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy a = enemies.get(i);

            if (!a.isAlive()) {
                continue;
            }

            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy b = enemies.get(j);

                if (!b.isAlive()) {
                    continue;
                }

                float minDistance = Math.max(18f, a.getHitboxSize() + b.getHitboxSize());
                float distance = a.getPosition().dst(b.getPosition());

                if (distance <= 0.001f) {
                    b.getPosition().add(MathUtils.random(-24f, 24f), MathUtils.random(-24f, 24f));
                    updateEnemyBoundingBox(b);
                    continue;
                }

                if (distance >= minDistance) {
                    continue;
                }

                Vector2 push = new Vector2(b.getPosition()).sub(a.getPosition()).nor();
                float pushAmount = (minDistance - distance) * 0.5f;

                a.getPosition().mulAdd(push, -pushAmount);
                b.getPosition().mulAdd(push, pushAmount);

                keepEnemyInsideArena(a);
                keepEnemyInsideArena(b);

                updateEnemyBoundingBox(a);
                updateEnemyBoundingBox(b);
            }
        }
    }

    private void applyArenaThemeForStage(int stageId) {
        int theme = (stageId - 1) % 5;

        if (theme == 0) {
            arenaColor = new Color(0.01f, 0.015f, 0.05f, 1f);
            arenaGridColor = new Color(0.02f, 0.34f, 0.58f, 1f);
            arenaBorderColor = new Color(0.15f, 0.82f, 1f, 1f);
        } else if (theme == 1) {
            arenaColor = new Color(0.05f, 0.01f, 0.015f, 1f);
            arenaGridColor = new Color(0.55f, 0.08f, 0.05f, 1f);
            arenaBorderColor = new Color(1f, 0.12f, 0.08f, 1f);
        } else if (theme == 2) {
            arenaColor = new Color(0.01f, 0.045f, 0.035f, 1f);
            arenaGridColor = new Color(0.05f, 0.45f, 0.28f, 1f);
            arenaBorderColor = new Color(0.15f, 1f, 0.58f, 1f);
        } else if (theme == 3) {
            arenaColor = new Color(0.03f, 0.015f, 0.06f, 1f);
            arenaGridColor = new Color(0.28f, 0.14f, 0.72f, 1f);
            arenaBorderColor = new Color(0.62f, 0.32f, 1f, 1f);
        } else {
            arenaColor = new Color(0.055f, 0.045f, 0.01f, 1f);
            arenaGridColor = new Color(0.62f, 0.45f, 0.08f, 1f);
            arenaBorderColor = new Color(1f, 0.86f, 0.12f, 1f);
        }
    }

    private void rebuildArenaObstaclesForStage(int stageId) {
        arenaObstacles.clear();

        float size = ARENA_SIZE;
        float x = GameConfig.VIEWPORT_WIDTH / 2f - size / 2f;
        float y = GameConfig.VIEWPORT_HEIGHT / 2f - size / 2f;

        int theme = (stageId - 1) % 5;

        if (theme == 0) {
            arenaObstacles.add(new ArenaObstacle(x + size * 0.25f - 34f, y + size * 0.25f - 34f, 68f, 68f, 0));
            arenaObstacles.add(new ArenaObstacle(x + size * 0.75f - 34f, y + size * 0.75f - 34f, 68f, 68f, 0));
        } else if (theme == 1) {
            arenaObstacles.add(new ArenaObstacle(x + size * 0.20f, y + size * 0.45f, 140f, 24f, 1));
            arenaObstacles.add(new ArenaObstacle(x + size * 0.65f, y + size * 0.55f, 160f, 24f, 1));
        } else if (theme == 2) {
            arenaObstacles.add(new ArenaObstacle(x + size * 0.30f - 28f, y + size * 0.70f - 28f, 56f, 56f, 0));
            arenaObstacles.add(new ArenaObstacle(x + size * 0.70f - 42f, y + size * 0.30f - 42f, 84f, 84f, 0));
            arenaObstacles.add(new ArenaObstacle(x + size * 0.50f - 22f, y + size * 0.50f - 22f, 44f, 44f, 0));
        } else if (theme == 3) {
            arenaObstacles.add(new ArenaObstacle(x + size * 0.34f, y + size * 0.34f, 380f, 34f, 1));
            arenaObstacles.add(new ArenaObstacle(x + size * 0.34f, y + size * 0.62f, 380f, 34f, 1));
        } else {
            arenaObstacles.add(new ArenaObstacle(x + size * 0.18f, y + size * 0.18f, 70f, 70f, 2));
            arenaObstacles.add(new ArenaObstacle(x + size * 0.76f, y + size * 0.18f, 70f, 70f, 2));
            arenaObstacles.add(new ArenaObstacle(x + size * 0.18f, y + size * 0.76f, 70f, 70f, 2));
            arenaObstacles.add(new ArenaObstacle(x + size * 0.76f, y + size * 0.76f, 70f, 70f, 2));
        }
    }

    private void keepPlayerInsideArena() {
        float halfSize = ARENA_SIZE / 2f;
        float centerX = GameConfig.VIEWPORT_WIDTH / 2f;
        float centerY = GameConfig.VIEWPORT_HEIGHT / 2f;

        Vector2 position = player.getPosition();

        position.x = Math.max(centerX - halfSize + 20f, Math.min(centerX + halfSize - 20f, position.x));
        position.y = Math.max(centerY - halfSize + 20f, Math.min(centerY + halfSize - 20f, position.y));
    }

    private void resolvePlayerObstacleCollisions() {
        for (ArenaObstacle obstacle : arenaObstacles) {
            pushCircleOutOfRectangle(player.getPosition(), 20f, obstacle.bounds);
        }
    }

    private void resolveEnemyObstacleCollisions() {
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }

            resolveSingleEnemyObstacleCollision(enemy);
            updateEnemyBoundingBox(enemy);
        }
    }

    private void applyEnemyObstacleAvoidance(float delta) {
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive() || isEnemyFrozen(enemy) || enemy.getVelocity().len2() <= 0.01f) {
                continue;
            }

            ArenaObstacle obstacle = findBlockingObstacle(enemy, enemy.getVelocity());

            if (obstacle == null) {
                continue;
            }

            Vector2 toPlayer = new Vector2(player.getPosition()).sub(enemy.getPosition());

            if (toPlayer.len2() <= 0.001f) {
                continue;
            }

            Vector2 sideA = new Vector2(-toPlayer.y, toPlayer.x).nor();
            Vector2 sideB = new Vector2(toPlayer.y, -toPlayer.x).nor();
            Vector2 futureA = new Vector2(enemy.getPosition()).mulAdd(sideA, 80f);
            Vector2 futureB = new Vector2(enemy.getPosition()).mulAdd(sideB, 80f);
            Vector2 chosenSide = scoreAvoidancePoint(futureA) >= scoreAvoidancePoint(futureB) ? sideA : sideB;

            Vector2 newVelocity = new Vector2(toPlayer).nor().scl(0.25f)
                .add(new Vector2(chosenSide).scl(1.55f))
                .nor()
                .scl(enemy.getEffectiveSpeed());

            enemy.setVelocity(newVelocity.x, newVelocity.y);
        }
    }

    private ArenaObstacle findBlockingObstacle(Enemy enemy, Vector2 velocity) {
        Vector2 future = new Vector2(enemy.getPosition())
            .mulAdd(new Vector2(velocity).nor(), enemy.getHitboxSize() + 44f);

        float radius = Math.max(12f, enemy.getHitboxSize());

        for (ArenaObstacle obstacle : arenaObstacles) {
            if (circleIntersectsRectangle(future, radius, obstacle.bounds)) {
                return obstacle;
            }
        }

        return null;
    }

    private float scoreAvoidancePoint(Vector2 point) {
        float score = 0f;

        for (ArenaObstacle obstacle : arenaObstacles) {
            float dx = point.x - obstacle.centerX();
            float dy = point.y - obstacle.centerY();
            score += dx * dx + dy * dy;
        }

        score -= point.dst2(player.getPosition()) * 0.18f;
        return score;
    }

    private void applyGuardianShieldWall(float delta) {
        if (!player.isGuardianShieldActive()) {
            return;
        }

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }

            Vector2 fromPlayer = new Vector2(enemy.getPosition()).sub(player.getPosition());
            float distance = fromPlayer.len();
            float minDistance = GUARDIAN_SHIELD_RADIUS + Math.max(12f, enemy.getHitboxSize());

            if (distance >= minDistance) {
                continue;
            }

            if (distance <= 0.001f) {
                fromPlayer.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
            }

            fromPlayer.nor();

            enemy.getPosition().mulAdd(fromPlayer, minDistance - distance + GUARDIAN_SHIELD_WALL_PUSH * delta);
            enemy.setVelocity(fromPlayer.x * enemy.getEffectiveSpeed(), fromPlayer.y * enemy.getEffectiveSpeed());

            keepEnemyInsideArena(enemy);
            resolveSingleEnemyObstacleCollision(enemy);
            updateEnemyBoundingBox(enemy);
        }
    }

    private boolean circleIntersectsRectangle(Vector2 circleCenter, float radius, Rectangle rectangle) {
        float closestX = Math.max(rectangle.x, Math.min(circleCenter.x, rectangle.x + rectangle.width));
        float closestY = Math.max(rectangle.y, Math.min(circleCenter.y, rectangle.y + rectangle.height));

        float dx = circleCenter.x - closestX;
        float dy = circleCenter.y - closestY;

        return dx * dx + dy * dy <= radius * radius;
    }

    private void pushCircleOutOfRectangle(Vector2 circleCenter, float radius, Rectangle rectangle) {
        float closestX = Math.max(rectangle.x, Math.min(circleCenter.x, rectangle.x + rectangle.width));
        float closestY = Math.max(rectangle.y, Math.min(circleCenter.y, rectangle.y + rectangle.height));

        float dx = circleCenter.x - closestX;
        float dy = circleCenter.y - closestY;
        float distanceSquared = dx * dx + dy * dy;

        if (distanceSquared > radius * radius) {
            return;
        }

        if (distanceSquared == 0f) {
            pushCircleFromRectangleCenter(circleCenter, radius, rectangle);
            return;
        }

        float distance = (float) Math.sqrt(distanceSquared);
        float pushAmount = radius - distance;

        circleCenter.x += dx / distance * pushAmount;
        circleCenter.y += dy / distance * pushAmount;
    }

    private void pushCircleFromRectangleCenter(Vector2 circleCenter, float radius, Rectangle rectangle) {
        float left = Math.abs(circleCenter.x - rectangle.x);
        float right = Math.abs((rectangle.x + rectangle.width) - circleCenter.x);
        float bottom = Math.abs(circleCenter.y - rectangle.y);
        float top = Math.abs((rectangle.y + rectangle.height) - circleCenter.y);
        float min = Math.min(Math.min(left, right), Math.min(bottom, top));

        if (min == left) {
            circleCenter.x = rectangle.x - radius;
        } else if (min == right) {
            circleCenter.x = rectangle.x + rectangle.width + radius;
        } else if (min == bottom) {
            circleCenter.y = rectangle.y - radius;
        } else {
            circleCenter.y = rectangle.y + rectangle.height + radius;
        }
    }

    private void pullIdleEnemiesTowardPlayer(float delta) {
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive() || isEnemyFrozen(enemy)) {
                continue;
            }

            if (enemy.getPosition().dst(player.getPosition()) <= 35f || enemy.getVelocity().len2() > 1f) {
                continue;
            }

            Vector2 direction = new Vector2(player.getPosition()).sub(enemy.getPosition());

            if (direction.len2() <= 0.001f) {
                continue;
            }

            direction.nor();
            enemy.getPosition().mulAdd(direction, enemy.getEffectiveSpeed() * delta);
            updateEnemyBoundingBox(enemy);
        }
    }

    private void knockbackEnemiesFromPlayer(float knockbackDistance) {
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }

            Vector2 direction = new Vector2(enemy.getPosition()).sub(player.getPosition());
            float distance = direction.len();

            if (distance > GUARDIAN_SHIELD_RADIUS * 2f) {
                continue;
            }

            if (distance <= 0.001f) {
                direction.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
            }

            direction.nor();

            float strength = 1f - Math.min(1f, distance / (GUARDIAN_SHIELD_RADIUS * 2f));
            enemy.getPosition().mulAdd(direction, knockbackDistance * strength);

            keepEnemyInsideArena(enemy);
            resolveSingleEnemyObstacleCollision(enemy);
            updateEnemyBoundingBox(enemy);
        }
    }

    private void keepEnemiesInsideArena() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                keepEnemyInsideArena(enemy);
                updateEnemyBoundingBox(enemy);
            }
        }
    }

    private void keepEnemyInsideArena(Enemy enemy) {
        float halfSize = ARENA_SIZE / 2f;
        float centerX = GameConfig.VIEWPORT_WIDTH / 2f;
        float centerY = GameConfig.VIEWPORT_HEIGHT / 2f;
        Vector2 position = enemy.getPosition();

        position.x = Math.max(centerX - halfSize + enemy.getHitboxSize(), Math.min(centerX + halfSize - enemy.getHitboxSize(), position.x));
        position.y = Math.max(centerY - halfSize + enemy.getHitboxSize(), Math.min(centerY + halfSize - enemy.getHitboxSize(), position.y));
    }

    private void resolveSingleEnemyObstacleCollision(Enemy enemy) {
        float radius = Math.max(12f, enemy.getHitboxSize());

        for (ArenaObstacle obstacle : arenaObstacles) {
            pushCircleOutOfRectangle(enemy.getPosition(), radius, obstacle.bounds);
        }
    }

    private void updateEnemyBoundingBox(Enemy enemy) {
        enemy.getBoundingBox().setPosition(
            enemy.getPosition().x - enemy.getHitboxSize() / 2f,
            enemy.getPosition().y - enemy.getHitboxSize() / 2f
        );
    }

    private void handleRangedAttack() {
        if (!player.getController().isAttackPressed() || rangedAttackCooldown > 0f) {
            return;
        }

        Vector2 direction = getPreferredRangedDirection();
        Enemy homingTarget = findClosestEnemy(player.getPosition(), RANGED_HOMING_RANGE);

        HitData hitData = new HitData(
            com.patternforge.echoesarena.combat.HitType.RANGED,
            RANGED_SHOT_DAMAGE,
            0f,
            player.getCombatStats(),
            player.getBuild().getMagicalStats(),
            com.patternforge.echoesarena.combat.StatusEffectType.NONE
        );

        Projectile projectile = combatSystem.spawnProjectile(
            player.getPosition().x,
            player.getPosition().y,
            direction,
            RANGED_SHOT_SPEED,
            RANGED_SHOT_RADIUS,
            RANGED_SHOT_MAX_DISTANCE,
            hitData,
            true
        );

        if (homingTarget != null) {
            projectile.setHomingTarget(homingTarget, RANGED_HOMING_TURN_RATE);
        }

        rangedAttackCooldown = RANGED_ATTACK_COOLDOWN / Math.max(0.65f, player.getBuild().getPhysicalStats().getAttackSpeed());
        context.getAudioService().playSound(AudioService.SFX_PLAYER_ATTACK);
    }

    private void handleMeleeAttack() {
        if (!Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) || meleeAttackCooldown > 0f) {
            return;
        }

        if (!player.spendMana(MELEE_ATTACK_MANA_COST)) {
            return;
        }

        meleeAttackCooldown = MELEE_ATTACK_COOLDOWN / Math.max(0.65f, player.getBuild().getPhysicalStats().getAttackSpeed());
        boolean hitAnyEnemy = false;

        Vector2 attackCenter = new Vector2(player.getPosition())
            .add(new Vector2(player.getFacing()).scl(MELEE_ATTACK_RANGE * 0.5f));

        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.getPosition().dst(attackCenter) <= MELEE_ATTACK_RANGE) {
                combatSystem.meleeHit(
                    player,
                    enemy,
                    MELEE_ATTACK_DAMAGE,
                    com.patternforge.echoesarena.combat.StatusEffectType.NONE
                );
                hitAnyEnemy = true;
            }
        }

        context.getAudioService().playSound(AudioService.SFX_MELEE_ATTACK);
    }

    private void resolveProjectileCombat() {
        List<Projectile> projectiles = combatSystem.getProjectiles();

        for (Projectile projectile : projectiles) {
            if (projectile.isExpired()) {
                continue;
            }

            if (isInsideObstacle(projectile.getPosition(), projectile.getRadius())) {
                projectile.expire();
                continue;
            }

            if (projectile.isFromPlayer()) {
                for (Enemy enemy : enemies) {
                    if (!enemy.isAlive()) {
                        continue;
                    }

                    float hitRadius = projectile.getRadius() + getProjectileHitRadius(enemy);
                    if (enemy.getPosition().dst2(projectile.getPosition()) <= hitRadius * hitRadius) {
                        combatSystem.rangedHit(projectile, enemy);
                        projectile.expire();
                        context.getAudioService().playSound(AudioService.SFX_ENEMY_HIT);
                        break;
                    }
                }
            } else {
                float hitRadius = projectile.getRadius() + 16f;
                if (player.getPosition().dst2(projectile.getPosition()) <= hitRadius * hitRadius) {
                    float hpBeforeHit = player.getCombatStats().getCurrentHp();
                    combatSystem.rangedHit(projectile, player);
                    projectile.expire();
                    if (player.getCombatStats().getCurrentHp() < hpBeforeHit) {
                        context.getAudioService().playSound(AudioService.SFX_ENEMY_HIT);
                    }
                }
            }
        }
    }

    private Enemy findClosestEnemy(Vector2 origin, float maxDistance) {
        Enemy closest = null;
        float bestDistanceSq = maxDistance * maxDistance;

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }

            float distanceSq = enemy.getPosition().dst2(origin);
            if (distanceSq < bestDistanceSq) {
                bestDistanceSq = distanceSq;
                closest = enemy;
            }
        }

        return closest;
    }

    private float getProjectileHitRadius(Enemy enemy) {
        float hitRadius = Math.max(12f, enemy.getHitboxSize() * 1.35f);
        if (isFinalBoss(enemy)) {
            hitRadius = Math.max(hitRadius, enemy.getHitboxSize() * 3.2f);
        }
        return hitRadius;
    }

    private Vector2 getPreferredRangedDirection() {
        Enemy closest = findClosestEnemy(player.getPosition(), RANGED_HOMING_RANGE);
        if (closest != null) {
            Vector2 toEnemy = new Vector2(closest.getPosition()).sub(player.getPosition());
            if (toEnemy.len2() > 0.001f) {
                player.getFacing().set(toEnemy).nor();
                return toEnemy.nor();
            }
        }

        Vector3 mouseScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        camera.unproject(mouseScreen);
        Vector2 toCursor = new Vector2(mouseScreen.x, mouseScreen.y).sub(player.getPosition());
        if (toCursor.len2() > 0.001f) {
            player.getFacing().set(toCursor).nor();
            return toCursor.nor();
        }

        return new Vector2(player.getFacing());
    }

    private void collectHealthPickups() {
        for (int i = healthPickups.size() - 1; i >= 0; i--) {
            HealthPickup pickup = healthPickups.get(i);

            if (pickup.position.dst(player.getPosition()) <= HEALTH_PICKUP_COLLECT_DISTANCE) {
                player.heal(pickup.healAmount);
                healthPickups.remove(i);
                collectedHealthPickups++;
                context.getAudioService().playSound(AudioService.SFX_HEALTH_PICKUP);
            }
        }
    }

    private void removeDeadEnemies() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);

            if (!enemy.isAlive()) {
                if (isFinalBoss(enemy)) {
                    finalBossDefeatedForStage = true;
                    activeBoss = null;
                }

                maybeDropHealthPickup(enemy.getPosition().x, enemy.getPosition().y);
                stageTunedEnemies.remove(enemy);
                enemies.remove(i);
                killedEnemies++;
                abilityService.addUltimateChargeFromKill();
                levelUpService.addXp(10);
                context.getAudioService().playSound(AudioService.SFX_ENEMY_KILL);
            }
        }
    }

    private void maybeDropHealthPickup(float x, float y) {
        if (MathUtils.randomBoolean(HEALTH_PICKUP_DROP_CHANCE)) {
            healthPickups.add(new HealthPickup(x, y, HEALTH_PICKUP_RADIUS, HEALTH_PICKUP_HEAL));
        }
    }

    private void loadFantasyArt(PlayerClass selectedClass) {
        playerTexture = loadTexture(getPlayerTexturePath(selectedClass));
        gruntTexture = loadTexture("external/anokolisa/Entities/Mobs/Skeleton Crew/Skeleton - Base/Run/Run-Sheet.png");
        swarmTexture = loadTexture("external/anokolisa/Entities/Mobs/Skeleton Crew/Skeleton - Rogue/Run/Run-Sheet.png");
        tankTexture = loadTexture("external/anokolisa/Entities/Mobs/Orc Crew/Orc - Warrior/Run/Run-Sheet.png");
        sniperTexture = loadTexture("external/anokolisa/Entities/Mobs/Skeleton Crew/Skeleton - Mage/Run/Run-Sheet.png");
        supportTexture = loadTexture("external/anokolisa/Entities/Mobs/Orc Crew/Orc - Shaman/Run/Run-Sheet.png");
        frostNovaTexture = loadTexture("art/effects/sheets/GameFXexport/SPRITESHEET_Files/IceCast_96x96.png");
        attackAirSheetTexture = loadTexture("art/effects/sheets/GameFXexport/SPRITESHEET_Files/PoisonClaw_96x96.png");
        dashSheetTexture = loadTexture("art/effects/sheets/GameFXexport/SPRITESHEET_Files/TornadoMoving_96x96.png");
        fireballSheetTexture = loadTexture("art/effects/sheets/GameFXexport/SPRITESHEET_Files/FireBall_64x64.png");
        magicSheetTexture = loadTexture("art/effects/sheets/GameFXexport/SPRITESHEET_Files/FireCast_96x96.png");
        sparksSheetTexture = loadTexture("art/effects/sheets/GameFXexport/SPRITESHEET_Files/LightCast_96.png");
        worldBackdropTexture = loadLinearTexture(resolveWorldBackdropPath());
        floorTilesTexture = loadTexture("external/anokolisa/Environment/Tilesets/Floors_Tiles.png");
        dungeonTilesTexture = loadTexture("external/anokolisa/Environment/Tilesets/Dungeon_Tiles.png");
        wallTilesTexture = loadTexture("external/anokolisa/Environment/Tilesets/Wall_Tiles.png");
        wallVariationsTexture = loadTexture("external/anokolisa/Environment/Tilesets/Wall_Variations.png");
        dungeonPropsTexture = loadTexture("external/anokolisa/Environment/Props/Static/Dungeon_Props.png");
        furnitureTexture = loadTexture("external/anokolisa/Environment/Props/Static/Furniture.png");
        esotericTexture = loadTexture("external/anokolisa/Environment/Props/Static/Esoteric.png");
        rocksTexture = loadTexture("external/anokolisa/Environment/Props/Static/Rocks.png");
        vegetationTexture = loadTexture("external/anokolisa/Environment/Props/Static/Vegetation.png");
        floorTiles = TextureRegion.split(floorTilesTexture, 16, 16);
        dungeonTiles = TextureRegion.split(dungeonTilesTexture, 16, 16);
        wallTiles = TextureRegion.split(wallTilesTexture, 16, 16);
        wallVariationTiles = TextureRegion.split(wallVariationsTexture, 16, 16);
        dungeonPropTiles = TextureRegion.split(dungeonPropsTexture, 16, 16);
        furnitureTiles = TextureRegion.split(furnitureTexture, 16, 16);
        esotericTiles = TextureRegion.split(esotericTexture, 16, 16);
        rockTiles = TextureRegion.split(rocksTexture, 16, 16);
        vegetationTiles = TextureRegion.split(vegetationTexture, 16, 16);
        fogMaskTexture = createFogMaskTexture(1024, 576, 0.20f, 0.90f);
    }

    private Texture loadTexture(String path) {
        if (!Gdx.files.internal(path).exists()) {
            return createFallbackTexture(Texture.TextureFilter.Nearest);
        }

        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return texture;
    }

    private Texture loadLinearTexture(String path) {
        if (!Gdx.files.internal(path).exists()) {
            return createFallbackTexture(Texture.TextureFilter.Linear);
        }

        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return texture;
    }

    private Texture createFallbackTexture(Texture.TextureFilter filter) {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        texture.setFilter(filter, filter);
        pixmap.dispose();
        return texture;
    }

    private Texture createFogMaskTexture(int width, int height, float visibleRadiusRatio, float maxAlpha) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        float centerX = width * 0.5f;
        float centerY = height * 0.5f;
        float minDimension = Math.min(width, height);
        float clearRadius = minDimension * visibleRadiusRatio;
        float fullDarkRadius = clearRadius + minDimension * 0.40f;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float dx = x - centerX;
                float dy = y - centerY;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                float alpha;

                if (distance <= clearRadius) {
                    alpha = 0f;
                } else {
                    float t = (distance - clearRadius) / Math.max(1f, fullDarkRadius - clearRadius);
                    t = Math.max(0f, Math.min(1f, t));
                    float smooth = t * t * (3f - 2f * t);
                    alpha = maxAlpha * smooth;
                }

                pixmap.setColor(0f, 0f, 0f, alpha);
                pixmap.drawPixel(x, y);
            }
        }

        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        return texture;
    }

    private String resolveWorldBackdropPath() {
        String[] candidates = {
            "external/menu-background/lost_kingdom_3840x2160.png",
            "external/menu-background/lost_kingdom_2560x1440.png",
            "external/menu-background/lost_kingdom_1280x720.png",
            "art/maps/fantasy_cave_night.png"
        };

        for (String path : candidates) {
            if (Gdx.files.internal(path).exists()) {
                return path;
            }
        }
        return "art/maps/fantasy_cave_night.png";
    }

    private String getPlayerTexturePath(PlayerClass selectedClass) {
        if (selectedClass == PlayerClass.BRUISER) {
            return "external/anokolisa/Entities/Npc's/Knight/Run/Run-Sheet.png";
        }

        if (selectedClass == PlayerClass.RUNNER) {
            return "external/anokolisa/Entities/Npc's/Rogue/Run/Run-Sheet.png";
        }

        return "external/anokolisa/Entities/Npc's/Wizzard/Run/Run-Sheet.png";
    }

    private void drawArena() {
        float size = ARENA_SIZE;
        float x = GameConfig.VIEWPORT_WIDTH / 2f - size / 2f;
        float y = GameConfig.VIEWPORT_HEIGHT / 2f - size / 2f;

        drawArenaFloorTexture(x + 16f, y + 16f, size - 32f, size - 32f);

        drawArenaObstacles();

        shapeRenderer.setColor(0f, 0f, 0f, 0.26f);
        drawRectBorder(x, y, size, size, 10f);
        shapeRenderer.setColor(arenaBorderColor.r, arenaBorderColor.g, arenaBorderColor.b, 0.55f);
        drawRectBorder(x + 8f, y + 8f, size - 16f, size - 16f, 2.5f);

    }

    private void drawArenaFloorTexture(float x, float y, float width, float height) {
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (worldBackdropTexture != null) {
            batch.setColor(1f, 1f, 1f, 0.45f);
            drawCoverInRegion(worldBackdropTexture, x - 120f, y - 120f, width + 240f, height + 240f);
        }
        batch.setColor(Color.WHITE);
        drawArenaTiles(x, y, width, height);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    private void drawCoverInRegion(Texture texture, float x, float y, float width, float height) {
        float scale = Math.max(width / texture.getWidth(), height / texture.getHeight());
        float drawWidth = texture.getWidth() * scale;
        float drawHeight = texture.getHeight() * scale;
        float drawX = x + (width - drawWidth) * 0.5f;
        float drawY = y + (height - drawHeight) * 0.5f;
        batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
    }

    private void drawArenaTiles(float x, float y, float width, float height) {
        TextureRegion[][] tiles = getCurrentTiles();
        float tileSize = 32f;
        int cols = (int) Math.ceil(width / tileSize);
        int rows = (int) Math.ceil(height / tileSize);

        for (int ty = 0; ty < rows; ty++) {
            for (int tx = 0; tx < cols; tx++) {
                TextureRegion tile = pickArenaTile(tiles, tx, ty);
                batch.draw(tile, x + tx * tileSize, y + ty * tileSize, tileSize, tileSize);
            }
        }

        drawArenaDecorations(x, y, cols, rows, tileSize);
    }

    private TextureRegion[][] getCurrentTiles() {
        if (currentStageId % 2 == 0) {
            return dungeonTiles;
        }

        return floorTiles;
    }

    private TextureRegion pickArenaTile(TextureRegion[][] tiles, int tx, int ty) {
        int rowCount = Math.max(1, tiles.length);
        int colCount = Math.max(1, tiles[0].length);
        int row = Math.abs(tx * 3 + ty * 5 + currentStageId * 7) % Math.min(10, rowCount);
        int col = Math.abs(tx * 11 + ty * 13 + currentStageId * 3) % Math.min(12, colCount);
        return tiles[row][col];
    }

    private void drawArenaDecorations(float x, float y, int cols, int rows, float tileSize) {
        for (int ty = 1; ty < rows - 1; ty++) {
            for (int tx = 1; tx < cols - 1; tx++) {
                int hash = Math.abs(tx * 928371 + ty * 364479 + currentStageId * 173);

                if (hash % 53 == 0) {
                    TextureRegion wall = wallVariationTiles[(hash / 5) % Math.min(8, wallVariationTiles.length)][(hash / 7) % Math.min(8, wallVariationTiles[0].length)];
                    batch.draw(wall, x + tx * tileSize, y + ty * tileSize, tileSize, tileSize);
                } else if (hash % 41 == 0) {
                    TextureRegion rock = rockTiles[(hash / 7) % Math.min(10, rockTiles.length)][(hash / 11) % Math.min(10, rockTiles[0].length)];
                    batch.draw(rock, x + tx * tileSize, y + ty * tileSize, tileSize, tileSize);
                } else if (currentStageId % 2 != 0 && hash % 29 == 0) {
                    TextureRegion grass = vegetationTiles[2 + (hash / 5) % 4][1 + ((hash / 13) % 4) * 3];
                    batch.draw(grass, x + tx * tileSize, y + ty * tileSize, tileSize, tileSize);
                } else if (currentStageId % 2 == 0 && hash % 47 == 0) {
                    TextureRegion prop = dungeonPropTiles[(hash / 3) % Math.min(4, dungeonPropTiles.length)][(hash / 9) % Math.min(12, dungeonPropTiles[0].length)];
                    batch.draw(prop, x + tx * tileSize, y + ty * tileSize, tileSize, tileSize);
                } else if (hash % 59 == 0) {
                    TextureRegion prop = furnitureTiles[(hash / 5) % Math.min(6, furnitureTiles.length)][(hash / 11) % Math.min(14, furnitureTiles[0].length)];
                    batch.draw(prop, x + tx * tileSize, y + ty * tileSize, tileSize, tileSize);
                } else if (hash % 67 == 0) {
                    TextureRegion prop = esotericTiles[(hash / 7) % Math.min(6, esotericTiles.length)][(hash / 13) % Math.min(12, esotericTiles[0].length)];
                    batch.draw(prop, x + tx * tileSize, y + ty * tileSize, tileSize, tileSize);
                }
            }
        }

        drawArenaWalls(x, y, cols, rows, tileSize);
    }

    private void drawArenaWalls(float x, float y, int cols, int rows, float tileSize) {
        for (int tx = 0; tx < cols; tx++) {
            drawWallTile(x + tx * tileSize, y, tx, 0, tileSize);
            drawWallTile(x + tx * tileSize, y + (rows - 1) * tileSize, tx, rows - 1, tileSize);
        }
        for (int ty = 0; ty < rows; ty++) {
            drawWallTile(x, y + ty * tileSize, 0, ty, tileSize);
            drawWallTile(x + (cols - 1) * tileSize, y + ty * tileSize, cols - 1, ty, tileSize);
        }
    }

    private void drawWallTile(float x, float y, int tx, int ty, float tileSize) {
        int hash = Math.abs(tx * 1619 + ty * 673 + currentStageId * 97);
        TextureRegion tile = wallTiles[(hash / 3) % Math.min(8, wallTiles.length)][(hash / 5) % Math.min(8, wallTiles[0].length)];
        batch.draw(tile, x, y, tileSize, tileSize);
    }

    private void drawArenaObstacles() {
        for (ArenaObstacle obstacle : arenaObstacles) {
            shapeRenderer.setColor(0.08f, 0.07f, 0.06f, 0.88f);
            shapeRenderer.rect(obstacle.bounds.x, obstacle.bounds.y, obstacle.bounds.width, obstacle.bounds.height);

            if (obstacle.type == 0) {
                shapeRenderer.setColor(0.40f, 0.63f, 0.74f, 0.75f);
            } else if (obstacle.type == 1) {
                shapeRenderer.setColor(0.72f, 0.45f, 0.35f, 0.72f);
            } else {
                shapeRenderer.setColor(0.68f, 0.60f, 0.36f, 0.74f);
            }

            drawRectBorder(obstacle.bounds.x, obstacle.bounds.y, obstacle.bounds.width, obstacle.bounds.height, 2f);
        }
    }

    private void drawHealthPickups() {
        for (HealthPickup pickup : healthPickups) {
            shapeRenderer.setColor(0.1f, 0.85f, 0.25f, 1f);
            shapeRenderer.circle(pickup.position.x, pickup.position.y, pickup.radius);

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rectLine(pickup.position.x - 7f, pickup.position.y, pickup.position.x + 7f, pickup.position.y, 4f);
            shapeRenderer.rectLine(pickup.position.x, pickup.position.y - 7f, pickup.position.x, pickup.position.y + 7f, 4f);
        }
    }

    private void drawMagicBlast() {
        if (fireballCastVisualTimer <= 0f) {
            return;
        }

        float ratio = fireballCastVisualTimer / ULTIMATE_CAST_VISUAL_TIME;
        float pulse = 0.5f + 0.5f * MathUtils.sin(stateTime * 28f);
        float radius = 28f + (1f - ratio) * 48f;

        shapeRenderer.setColor(0.16f, 0.72f, 1f, 0.18f + 0.24f * ratio);
        shapeRenderer.circle(player.getPosition().x, player.getPosition().y, radius);
        shapeRenderer.setColor(0.70f, 0.94f, 1f, 0.55f * ratio);
        shapeRenderer.circle(player.getPosition().x, player.getPosition().y, 12f + pulse * 8f);
    }

    private void drawFreezeVisual() {
        if (abilityService.getFreezeVisualTimer() <= 0f && glacialRiftVisualTimer <= 0f) {
            return;
        }

        float freezeRatio = abilityService.getFreezeVisualTime() <= 0f
            ? 0f
            : abilityService.getFreezeVisualTimer() / abilityService.getFreezeVisualTime();
        float castRatio = ULTIMATE_CAST_VISUAL_TIME <= 0f ? 0f : glacialRiftVisualTimer / ULTIMATE_CAST_VISUAL_TIME;
        float ratio = Math.max(freezeRatio, castRatio);
        float radius = 52f + (1f - ratio) * abilityService.getFreezeVisualRadius();

        shapeRenderer.setColor(0.52f, 0.90f, 1f, 0.16f + 0.22f * ratio);
        shapeRenderer.circle(player.getPosition().x, player.getPosition().y, radius);
        shapeRenderer.setColor(0.82f, 0.98f, 1f, 0.42f * ratio);
        for (int i = 0; i < 12; i++) {
            float angle = i * 30f + stateTime * 80f;
            float inner = radius * 0.35f;
            float outer = radius * (0.78f + 0.10f * MathUtils.sin(stateTime * 5f + i));
            float x1 = player.getPosition().x + MathUtils.cosDeg(angle) * inner;
            float y1 = player.getPosition().y + MathUtils.sinDeg(angle) * inner;
            float x2 = player.getPosition().x + MathUtils.cosDeg(angle) * outer;
            float y2 = player.getPosition().y + MathUtils.sinDeg(angle) * outer;
            shapeRenderer.rectLine(x1, y1, x2, y2, 3f);
        }
    }

    private void drawGuardianShield() {
        if (!player.isGuardianShieldActive() && guardianShieldVisualTimer <= 0f) {
            return;
        }

        float duration = player.getGuardianShieldDuration();
        float remaining = Math.max(player.getGuardianShieldDurationRemaining(), guardianShieldVisualTimer);
        float ratio = duration <= 0f ? 0f : remaining / duration;

        float outerRadius = GUARDIAN_SHIELD_RADIUS * (0.88f + 0.12f * MathUtils.sin(stateTime * 9f));
        float innerRadius = outerRadius * (0.72f + 0.08f * ratio);

        shapeRenderer.setColor(0.10f, 0.55f, 1f, 0.75f);
        shapeRenderer.circle(player.getPosition().x, player.getPosition().y, outerRadius);

        shapeRenderer.setColor(0.65f, 0.92f, 1f, 0.92f);
        shapeRenderer.circle(player.getPosition().x, player.getPosition().y, innerRadius);
    }

    private void drawFireballs() {
        for (AbilityService.AbilityProjectile fireball : abilityService.getFireballs()) {
            float pulse = 0.5f + 0.5f * MathUtils.sin(stateTime * 18f);
            float radius = abilityService.getFireballRadius();
            shapeRenderer.setColor(0.12f, 0.55f, 1f, 0.42f);
            shapeRenderer.circle(fireball.getPosition().x, fireball.getPosition().y, radius * (2.4f + pulse));
            shapeRenderer.setColor(0.78f, 0.96f, 1f, 0.92f);
            shapeRenderer.circle(fireball.getPosition().x, fireball.getPosition().y, radius * (1.05f + pulse * 0.25f));
        }
    }

    private void drawPlayer() {
        float size = 72f;

        batch.setColor(Color.WHITE);
        drawAnimatedSheet(playerTexture, player.getPosition().x, player.getPosition().y, 64, 64, size, 0.10f);
        batch.setColor(Color.WHITE);
    }

    private void drawPlayerAttack() {
        if (!isPlayerAttackAnimating()) {
            return;
        }

        float progress = 1f - (meleeAttackCooldown / Math.max(0.001f, MELEE_ATTACK_COOLDOWN));
        Vector2 center = new Vector2(player.getPosition()).add(new Vector2(player.getFacing()).scl(MELEE_ATTACK_RANGE * 0.35f));
        float radius = 12f + 30f * Math.min(1f, progress / 0.45f);

        shapeRenderer.setColor(1f, 0.82f, 0.35f, 0.24f);
        shapeRenderer.circle(center.x, center.y, radius);
    }

    private void drawEnemies() {
        for (Enemy enemy : enemies) {
            Texture texture = getEnemyTexture(enemy.getType());
            float size = Math.max(56f, enemy.getHitboxSize() * 4.4f);

            if (enemy.getType() == EnemyType.SNIPER) {
                size *= 1.15f;
            }

            if (isFinalBoss(enemy)) {
                size *= 1.75f;
            }

            drawAnimatedSheet(texture, enemy.getPosition().x, enemy.getPosition().y, 64, 64, size, 0.11f);
        }
    }

    private void drawAnimatedSheet(Texture texture, float centerX, float centerY, int frameWidth, int frameHeight, float size, float frameDuration) {
        int frames = Math.max(1, texture.getWidth() / frameWidth);
        int frame = ((int) (stateTime / frameDuration)) % frames;
        TextureRegion region = new TextureRegion(texture, frame * frameWidth, 0, frameWidth, frameHeight);

        batch.draw(region, centerX - size / 2f, centerY - size / 2f, size, size);
    }

    private boolean isPlayerAttackAnimating() {
        return meleeAttackCooldown > MELEE_ATTACK_COOLDOWN - MELEE_ATTACK_ANIMATION_TIME;
    }

    private Texture getEnemyTexture(EnemyType type) {
        if (type == EnemyType.SWARM) {
            return swarmTexture;
        }

        if (type == EnemyType.TANK) {
            return tankTexture;
        }

        if (type == EnemyType.SNIPER) {
            return sniperTexture;
        }

        if (type == EnemyType.SUPPORT) {
            return supportTexture;
        }

        return gruntTexture;
    }

    private void drawWorldHealthBars() {
        drawEntityHealthBar(
            player.getPosition().x,
            player.getPosition().y + 34f,
            PLAYER_HEALTH_BAR_WIDTH,
            PLAYER_HEALTH_BAR_HEIGHT,
            player.getCombatStats().getHpRatio(),
            Color.LIME
        );

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }

            float barWidth = Math.max(36f, enemy.getHitboxSize() * 2f);
            float yOffset = enemy.getHitboxSize() + 14f;
            Color color = enemy.getType() == EnemyType.SNIPER ? Color.ORANGE : Color.RED;

            if (isFinalBoss(enemy)) {
                barWidth = 110f;
                yOffset += 28f;
                color = Color.PURPLE;
            }

            drawEntityHealthBar(enemy.getPosition().x, enemy.getPosition().y + yOffset, barWidth, HEALTH_BAR_HEIGHT, enemy.getCombatStats().getHpRatio(), color);
        }
    }

    private void drawEntityHealthBar(float centerX, float centerY, float width, float height, float ratio, Color fillColor) {
        float safeRatio = Math.max(0f, Math.min(1f, ratio));
        float x = centerX - width / 2f;

        shapeRenderer.setColor(0f, 0f, 0f, 0.9f);
        shapeRenderer.rect(x - 1f, centerY - 1f, width + 2f, height + 2f);

        shapeRenderer.setColor(0.18f, 0.18f, 0.18f, 1f);
        shapeRenderer.rect(x, centerY, width, height);

        shapeRenderer.setColor(fillColor);
        shapeRenderer.rect(x, centerY, width * safeRatio, height);
    }

    private void drawProjectiles() {
        for (Projectile projectile : combatSystem.getProjectiles()) {
            Vector2 velocity = projectile.getVelocity();
            if (velocity.len2() <= 0.001f) {
                continue;
            }

            Vector2 tail = new Vector2(projectile.getPosition()).mulAdd(new Vector2(velocity).nor(), -projectile.getRadius() * 7.5f);
            if (projectile.isFromPlayer()) {
                shapeRenderer.setColor(0.08f, 0.52f, 1f, 0.28f);
                shapeRenderer.rectLine(projectile.getPosition().x, projectile.getPosition().y, tail.x, tail.y, projectile.getRadius() * 2.2f);
                shapeRenderer.setColor(0.48f, 0.88f, 1f, 0.92f);
                shapeRenderer.rectLine(projectile.getPosition().x, projectile.getPosition().y, tail.x, tail.y, projectile.getRadius() * 0.75f);
                shapeRenderer.setColor(0.88f, 0.98f, 1f, 0.98f);
            } else {
                shapeRenderer.setColor(1f, 0.42f, 0.30f, 0.75f);
            }
            shapeRenderer.rectLine(projectile.getPosition().x, projectile.getPosition().y, tail.x, tail.y, projectile.getRadius() * 0.9f);
            shapeRenderer.circle(projectile.getPosition().x, projectile.getPosition().y, projectile.getRadius() * 0.92f);
        }
    }

    private void drawSpriteEffects() {
        drawMagicBlastSprite();
        drawFreezeSprite();
        drawDashSprite();
        drawSlashSprite();
        drawFireballSprites();
        drawProjectileSprites();
    }

    private void drawMagicBlastSprite() {
        float timer = Math.max(abilityService.getMagicVisualTimer(), fireballCastVisualTimer);
        if (timer <= 0f) {
            return;
        }

        float duration = fireballCastVisualTimer > abilityService.getMagicVisualTimer()
            ? ULTIMATE_CAST_VISUAL_TIME
            : abilityService.getMagicVisualTime();
        float progress = timer / Math.max(0.001f, duration);
        float size = abilityService.getMagicVisualRadius() * (2f - progress * 0.25f);
        drawEffectSheet(magicSheetTexture, player.getPosition().x, player.getPosition().y, size, 96, 96, 0.78f, 1f - progress);
    }

    private void drawFreezeSprite() {
        if (abilityService.getFreezeVisualTimer() <= 0f) {
            return;
        }

        float ratio = abilityService.getFreezeVisualTimer() / abilityService.getFreezeVisualTime();
        float size = abilityService.getFreezeVisualRadius() * (1.9f - ratio * 0.15f);
        drawEffectSheet(frostNovaTexture, player.getPosition().x, player.getPosition().y, size, 96, 96, 0.82f, 1f - ratio);
    }

    private void drawDashSprite() {
        if (dashVisualTimer <= 0f) {
            return;
        }

        float progress = 1f - dashVisualTimer / DASH_VISUAL_TIME;
        Vector2 dashCenter = new Vector2(player.getPosition()).sub(new Vector2(player.getFacing()).scl(34f));
        drawEffectSheet(dashSheetTexture, dashCenter.x, dashCenter.y, 96f, 96, 96, 0.70f, progress);
    }

    private void drawSlashSprite() {
        if (!isPlayerAttackAnimating()) {
            return;
        }

        Vector2 attackCenter = new Vector2(player.getPosition())
            .add(new Vector2(player.getFacing()).scl(MELEE_ATTACK_RANGE * 0.25f));

        float progress = (MELEE_ATTACK_COOLDOWN - meleeAttackCooldown) / MELEE_ATTACK_ANIMATION_TIME;
        float clampedProgress = Math.min(1f, progress);
        float size = 82f + 48f * clampedProgress;
        float alpha = 0.72f * (1f - clampedProgress * 0.45f);
        Vector2 facing = new Vector2(player.getFacing());
        if (facing.len2() <= 0.001f) {
            facing.set(1f, 0f);
        }
        facing.nor();
        float rotation = facing.angleDeg() - 10f;
        drawEffectSheetRotated(attackAirSheetTexture, attackCenter.x, attackCenter.y, size, 96, 96, alpha, clampedProgress, rotation);
    }

    private void drawFireballSprites() {
        for (AbilityService.AbilityProjectile fireball : abilityService.getFireballs()) {
            float size = abilityService.getFireballRadius() * 5.5f;
            float progress = (stateTime * 12f) % 1f;
            drawEffectSheet(fireballSheetTexture, fireball.getPosition().x, fireball.getPosition().y, size, 64, 64, 1f, progress);
        }
    }

    private void drawProjectileSprites() {
        for (Projectile projectile : combatSystem.getProjectiles()) {
            float size = projectile.isFromPlayer() ? projectile.getRadius() * 8.5f : projectile.getRadius() * 5f;
            float progress = (stateTime * 18f) % 1f;
            float rotation = projectile.getVelocity().angleDeg();
            drawEffectSheetRotated(sparksSheetTexture, projectile.getPosition().x, projectile.getPosition().y, size, 96, 96, 0.86f, progress, rotation);
        }
    }

    private void drawEffectSheet(Texture texture, float centerX, float centerY, float size, int frameWidth, int frameHeight, float alpha, float progress) {
        int frames = Math.max(1, texture.getWidth() / frameWidth);
        int frame = Math.min(frames - 1, Math.max(0, (int) (progress * frames)));
        TextureRegion region = new TextureRegion(texture, frame * frameWidth, 0, frameWidth, frameHeight);

        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(region, centerX - size / 2f, centerY - size / 2f, size, size);
        batch.setColor(Color.WHITE);
    }

    private void drawEffectSheetRotated(Texture texture, float centerX, float centerY, float size, int frameWidth,
                                        int frameHeight, float alpha, float progress, float rotation) {
        int frames = Math.max(1, texture.getWidth() / frameWidth);
        int frame = Math.min(frames - 1, Math.max(0, (int) (progress * frames)));
        TextureRegion region = new TextureRegion(texture, frame * frameWidth, 0, frameWidth, frameHeight);

        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(region, centerX - size / 2f, centerY - size / 2f, size / 2f, size / 2f,
            size, size, 1f, 1f, rotation);
        batch.setColor(Color.WHITE);
    }

    private void drawCenteredTexture(Texture texture, float centerX, float centerY, float size, float alpha) {
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(texture, centerX - size / 2f, centerY - size / 2f, size, size);
        batch.setColor(Color.WHITE);
    }

    private void drawNightVisionOverlay() {
        if (fogMaskTexture == null) {
            return;
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        float viewportX = camera.position.x - camera.viewportWidth * 0.5f;
        float viewportY = camera.position.y - camera.viewportHeight * 0.5f;
        batch.draw(
            fogMaskTexture,
            viewportX,
            viewportY,
            camera.viewportWidth,
            camera.viewportHeight
        );

        if (bossSpawnFlashTimer > 0f) {
            float ratio = bossSpawnFlashTimer / BOSS_FLASH_TIME;
            batch.setColor(0.35f, 0.08f, 0.08f, 0.16f * ratio);
            batch.draw(
                fogMaskTexture,
                viewportX,
                viewportY,
                camera.viewportWidth,
                camera.viewportHeight
            );
        }

        batch.setColor(Color.WHITE);
        batch.end();
    }

    private void drawHud() {
        uiCamera.update();

        float screenWidth = uiCamera.viewportWidth;
        float screenHeight = uiCamera.viewportHeight;

        float topPanelWidth = Math.min(820f, screenWidth - 28f);
        float topPanelHeight = 64f;
        float topPanelX = (screenWidth - topPanelWidth) * 0.5f;
        float topPanelY = screenHeight - topPanelHeight - 10f;

        float bottomPanelWidth = Math.min(830f, screenWidth - 28f);
        float bottomPanelHeight = 132f;
        float bottomPanelX = (screenWidth - bottomPanelWidth) * 0.5f;
        float bottomPanelY = 14f;

        float barX = bottomPanelX + 28f;
        float barWidth = Math.min(286f, bottomPanelWidth * 0.37f);
        float slotSize = 58f;
        float slotGap = 12f;
        float slotsX = bottomPanelX + bottomPanelWidth - (slotSize * 3f + slotGap * 2f) - 26f;
        float slotsY = bottomPanelY + 36f;

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        drawHudPanel(topPanelX, topPanelY, topPanelWidth, topPanelHeight);
        drawHudPanel(bottomPanelX, bottomPanelY, bottomPanelWidth, bottomPanelHeight);

        drawBar(barX, bottomPanelY + 82f, barWidth, 20f, player.getCombatStats().getHpRatio(), new Color(0.95f, 0.12f, 0.10f, 1f));
        drawBar(barX, bottomPanelY + 54f, barWidth, 14f, player.getManaProfile().getManaRatio(), new Color(0.10f, 0.42f, 1f, 1f));
        drawBar(barX, bottomPanelY + 30f, barWidth, 10f, levelUpService.getXpProgress(), new Color(0.25f, 0.95f, 0.45f, 1f));

        drawAbilitySlot(slotsX, slotsY, slotSize, player.getDashCooldownRatio(), Color.ORANGE);
        drawAbilitySlot(slotsX + (slotSize + slotGap), slotsY, slotSize, player.getGuardianShieldCooldownRatio(), Color.CYAN);
        drawAbilitySlot(slotsX + (slotSize + slotGap) * 2f, slotsY, slotSize, getUltimateRatio(), new Color(1f, 0.78f, 0.12f, 1f));

        shapeRenderer.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        CombatStats combatStats = player.getCombatStats();
        int totalWaves = stageDirector.getTotalWaves();
        int currentWave = Math.min(totalWaves, stageDirector.getCurrentWaveIndex() + 1);

        drawCenteredText("STAGE " + currentStageId + " / " + totalStages, topPanelX + topPanelWidth * 0.18f, topPanelY + 42f);
        drawCenteredText("WAVE " + currentWave + " / " + totalWaves, topPanelX + topPanelWidth * 0.50f, topPanelY + 42f);
        drawCenteredText("KILLS " + killedEnemies + "   MEDKITS " + collectedHealthPickups, topPanelX + topPanelWidth * 0.80f, topPanelY + 42f);

        drawHudText("HP  " + format(combatStats.getCurrentHp()) + " / " + format(combatStats.getMaxHp()), barX, bottomPanelY + 104f);
        drawHudText("MANA  " + format(player.getManaProfile().getCurrentMana()) + " / " + format(player.getManaProfile().getMaxMana()), barX, bottomPanelY + 74f);
        drawHudText("LVL " + levelUpService.getCurrentLevel() + "   XP " + levelUpService.getCurrentXp() + "/" + levelUpService.getXpForNextLevel(), barX, bottomPanelY + 50f);

        drawAbilityText("SPACE", "DASH", slotsX, slotsY, slotSize);
        drawAbilityText("AUTO", "SHIELD", slotsX + (slotSize + slotGap), slotsY, slotSize);
        drawAbilityText("Q / E", abilityService.isUltimateReady() ? "READY" : Math.round(abilityService.getUltimateCharge()) + "%", slotsX + (slotSize + slotGap) * 2f, slotsY, slotSize);

        batch.end();
    }

    private void drawBossHealthBar() {
        Enemy boss = getActiveBoss();
        if (boss == null) {
            return;
        }

        uiCamera.update();
        float screenWidth = uiCamera.viewportWidth;
        float barWidth = Math.min(screenWidth - 90f, 860f);
        float barHeight = 28f;
        float x = (screenWidth - barWidth) / 2f;
        float y = uiCamera.viewportHeight - 84f;

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.85f);
        shapeRenderer.rect(x - 4f, y - 4f, barWidth + 8f, barHeight + 8f);
        shapeRenderer.setColor(0.14f, 0.04f, 0.04f, 1f);
        shapeRenderer.rect(x, y, barWidth, barHeight);
        shapeRenderer.setColor(0.85f, 0.15f, 0.20f, 1f);
        shapeRenderer.rect(x, y, barWidth * Math.max(0f, Math.min(1f, bossHealthDisplayRatio)), barHeight);
        shapeRenderer.setColor(1f, 0.45f, 0.50f, 1f);
        drawRectBorder(x, y, barWidth, barHeight, 2.5f);
        shapeRenderer.end();

    }

    private void drawBossAnnouncement() {
        if (bossAnnouncementTimer <= 0f) {
            return;
        }

        uiCamera.update();

        float ratio = bossAnnouncementTimer / BOSS_ANNOUNCEMENT_TIME;

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.22f * ratio);
        shapeRenderer.rect(0f, 0f, uiCamera.viewportWidth, uiCamera.viewportHeight);
        shapeRenderer.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        font.setColor(1f, 0.32f, 0.32f, 0.85f * ratio + 0.15f);
        drawCenteredText("BOSS APPROACHES", uiCamera.viewportWidth / 2f, uiCamera.viewportHeight - 96f);
        font.setColor(Color.WHITE);
        batch.end();
    }

    private float getUltimateRatio() {
        return abilityService.getUltimateRatio();
    }

    private void drawHudPanel(float x, float y, float width, float height) {
        shapeRenderer.setColor(0f, 0f, 0f, 0.62f);
        shapeRenderer.rect(x - 2f, y - 2f, width + 4f, height + 4f);

        shapeRenderer.setColor(0.02f, 0.03f, 0.06f, 0.88f);
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.setColor(0.05f, 0.08f, 0.12f, 0.95f);
        shapeRenderer.rect(x + 4f, y + 4f, width - 8f, height - 8f);

        shapeRenderer.setColor(playerCircuitColor.r, playerCircuitColor.g, playerCircuitColor.b, 0.82f);
        drawRectBorder(x, y, width, height, 2f);
    }

    private void drawBar(float x, float y, float width, float height, float ratio, Color fillColor) {
        float safeRatio = Math.max(0f, Math.min(1f, ratio));

        shapeRenderer.setColor(0f, 0f, 0f, 0.75f);
        shapeRenderer.rect(x - 2f, y - 2f, width + 4f, height + 4f);

        shapeRenderer.setColor(0.04f, 0.05f, 0.07f, 1f);
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.setColor(fillColor);
        shapeRenderer.rect(x, y, width * safeRatio, height);
    }

    private void drawAbilitySlot(float x, float y, float size, float ratio, Color fillColor) {
        float safeRatio = Math.max(0f, Math.min(1f, ratio));

        shapeRenderer.setColor(0f, 0f, 0f, 0.86f);
        shapeRenderer.rect(x - 3f, y - 3f, size + 6f, size + 6f);

        shapeRenderer.setColor(0.035f, 0.04f, 0.06f, 1f);
        shapeRenderer.rect(x, y, size, size);

        shapeRenderer.setColor(fillColor.r, fillColor.g, fillColor.b, 0.28f);
        shapeRenderer.rect(x + 5f, y + 5f, size - 10f, (size - 10f) * safeRatio);

        shapeRenderer.setColor(fillColor);
        drawRectBorder(x, y, size, size, 2f);
    }

    private void drawRectBorder(float x, float y, float width, float height, float thickness) {
        shapeRenderer.rectLine(x, y, x + width, y, thickness);
        shapeRenderer.rectLine(x, y + height, x + width, y + height, thickness);
        shapeRenderer.rectLine(x, y, x, y + height, thickness);
        shapeRenderer.rectLine(x + width, y, x + width, y + height, thickness);
    }

    private void drawHudText(String text, float x, float y) {
        font.draw(batch, text, x, y);
    }

    private void drawCenteredText(String text, float centerX, float y) {
        glyphLayout.setText(font, text);
        font.draw(batch, text, centerX - glyphLayout.width / 2f, y);
    }

    private void drawAbilityText(String key, String label, float x, float y, float size) {
        glyphLayout.setText(font, key);
        font.draw(batch, key, x + size / 2f - glyphLayout.width / 2f, y + size - 14f);

        glyphLayout.setText(font, label);
        font.draw(batch, label, x + size / 2f - glyphLayout.width / 2f, y + 17f);
    }

    private String format(float value) {
        return String.format("%.1f", value);
    }

    @Override
    public void resize(int width, int height) {
        if (camera != null) {
            camera.setToOrtho(false, width, height);
        }

        if (uiCamera != null) {
            uiCamera.setToOrtho(false, width, height);
        }

        if (fogMaskTexture != null) {
            fogMaskTexture.dispose();
        }
        int fogWidth = Math.max(256, width);
        int fogHeight = Math.max(144, height);
        fogMaskTexture = createFogMaskTexture(fogWidth, fogHeight, 0.20f, 0.90f);

        if (upgradeTreeView != null) {
            upgradeTreeView.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }

        if (batch != null) {
            batch.dispose();
        }

        if (font != null) {
            font.dispose();
        }

        if (upgradeTreeView != null) {
            upgradeTreeView.dispose();
        }

        disposeTexture(playerTexture);
        disposeTexture(gruntTexture);
        disposeTexture(swarmTexture);
        disposeTexture(tankTexture);
        disposeTexture(sniperTexture);
        disposeTexture(supportTexture);
        disposeTexture(frostNovaTexture);
        disposeTexture(attackAirSheetTexture);
        disposeTexture(dashSheetTexture);
        disposeTexture(fireballSheetTexture);
        disposeTexture(magicSheetTexture);
        disposeTexture(sparksSheetTexture);
        disposeTexture(fogMaskTexture);
        disposeTexture(worldBackdropTexture);
        disposeTexture(floorTilesTexture);
        disposeTexture(dungeonTilesTexture);
        disposeTexture(wallTilesTexture);
        disposeTexture(wallVariationsTexture);
        disposeTexture(dungeonPropsTexture);
        disposeTexture(furnitureTexture);
        disposeTexture(esotericTexture);
        disposeTexture(rocksTexture);
        disposeTexture(vegetationTexture);
    }

    private void disposeTexture(Texture texture) {
        if (texture != null) {
            texture.dispose();
        }
    }

    public Player getPlayer() {
        return player;
    }
}
