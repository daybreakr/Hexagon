package com.honeycomb.hexagonx.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexagonServiceSetting {
    public final String name;

    private List<HexagonServiceSetting> mDependencies;
    private List<HexagonServiceSetting> mDependedOn;

    private List<Condition> mConditions;

    private boolean mEnabled;

    public HexagonServiceSetting(String name) {
        this.name = name;
    }

    public List<HexagonServiceSetting> dependencies() {
        if (mDependencies != null) {
            return Collections.unmodifiableList(mDependencies);
        }
        return Collections.emptyList();
    }

    public List<HexagonServiceSetting> dependedOn() {
        if (mDependedOn != null) {
            return Collections.unmodifiableList(mDependedOn);
        }
        return Collections.emptyList();
    }

    public HexagonServiceSetting dependsOn(HexagonServiceSetting dependency) {
        if (dependency != null) {
            if (mDependencies == null) {
                mDependencies = new ArrayList<>();
            }
            if (!mDependencies.contains(dependency)) {
                mDependencies.add(dependency);
            }

            if (dependency.mDependedOn == null) {
                dependency.mDependedOn = new ArrayList<>();
            }
            if (!dependency.mDependedOn.contains(this)) {
                dependency.mDependedOn.add(this);
            }
        }
        return this;
    }

    public List<Condition> conditions() {
        if (mConditions != null) {
            return Collections.unmodifiableList(mConditions);
        }
        return Collections.emptyList();
    }

    public HexagonServiceSetting condition(Condition condition) {
        if (condition != null) {
            if (mConditions == null) {
                mConditions = new ArrayList<>();
            }
            if (!mConditions.contains(condition)) {
                mConditions.add(condition);
            }
        }
        return this;
    }

    public boolean isAvailable() {
        if (!isEnabled()) {
            return false;
        }

        for (HexagonServiceSetting dependency : dependencies()) {
            if (!dependency.isAvailable()) {
                return false;
            }
        }

        return true;
    }

    /* For test */ boolean isEnabled() {
        return mEnabled;
    }

    public void enable() throws HexagonAvailabilityException {
        if (mEnabled) {
            return;
        }

        for (Condition condition : conditions()) {
            if (!condition.isSatisfied()) {
                throw new HexagonAvailabilityException(this.name,
                        "Condition not satisfied: " + condition.getDescription());
            }
        }

        mEnabled = true;
    }

    public void disable() {
        mEnabled = false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
