
package com.patternforge.echoesarena.progression;

public class UpgradeOption {

    private final String id;
    private final String displayName;
    private final String description;
    private final UpgradeCategory category;
    private final UpgradeTarget target;
    private final float value;
    private final boolean isPercentage;
    private final String requiredUnlockId;

    public UpgradeOption(String id, String displayName, String description,
                         UpgradeCategory category, UpgradeTarget target,
                         float value, boolean isPercentage, String requiredUnlockId) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.target = target;
        this.value = value;
        this.isPercentage = isPercentage;
        this.requiredUnlockId = requiredUnlockId;
    }

    public UpgradeOption(String id, String displayName, String description,
                         UpgradeCategory category, UpgradeTarget target,
                         float value, boolean isPercentage) {
        this(id, displayName, description, category, target, value, isPercentage, null);
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public UpgradeCategory getCategory() {
        return category;
    }

    public UpgradeTarget getTarget() {
        return target;
    }

    public float getValue() {
        return value;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public String getRequiredUnlockId() {
        return requiredUnlockId;
    }

    public boolean hasRequirement() {
        return requiredUnlockId != null && !requiredUnlockId.isEmpty();
    }
}
