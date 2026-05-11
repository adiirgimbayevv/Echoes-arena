package com.patternforge.echoesarena.progression;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UnlockService {

    private final Set<String> unlockedIds;

    public UnlockService() {
        this.unlockedIds = new HashSet<>();
    }

    public void unlock(String id) {
        unlockedIds.add(id);
    }

    public boolean isUnlocked(String id) {
        if (id == null || id.isEmpty()) {
            return true;
        }
        return unlockedIds.contains(id);
    }

    public void lockAll() {
        unlockedIds.clear();
    }

    public Set<String> getUnlockedIds() {
        return Collections.unmodifiableSet(unlockedIds);
    }

    public void restoreFrom(Set<String> ids) {
        unlockedIds.clear();
        unlockedIds.addAll(ids);
    }
}
