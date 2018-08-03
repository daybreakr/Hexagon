package com.honeycomb.hexagonx.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexagonComponentSetting {
    public final String name;

    private List<HexagonComponentSetting> mDependencies;
    private List<HexagonComponentSetting> mDependedOn;

    private List<Condition> mConditions;

    private int mPrivateFlags;

    private static final int FLAG_ENABLED = 1 << 1;                // 0x00000010
    private static final int FLAG_AVAILABLE = 1 << 2;              // 0x00000100
    private static final int FLAG_AVAILABILITY_DIRTY = 1 << 3;     // 0x00001000

    public HexagonComponentSetting(String name) {
        this.name = name;
    }

    public List<HexagonComponentSetting> dependencies() {
        if (mDependencies != null) {
            return Collections.unmodifiableList(mDependencies);
        }
        return Collections.emptyList();
    }

    public List<HexagonComponentSetting> dependedOn() {
        if (mDependedOn != null) {
            return Collections.unmodifiableList(mDependedOn);
        }
        return Collections.emptyList();
    }

    public HexagonComponentSetting dependsOn(HexagonComponentSetting dependency) {
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

    public HexagonComponentSetting condition(Condition condition) {
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

    public void enable() throws HexagonAvailabilityException {
        if (isEnabled()) {
            return;
        }

        for (Condition condition : conditions()) {
            if (!condition.isSatisfied()) {
                throw new HexagonAvailabilityException(this.name,
                        "Condition not satisfied: " + condition.getDescription());
            }
        }

        mPrivateFlags |= FLAG_ENABLED;
        invalidateAvailability();
    }

    public void disable() {
        if (!isEnabled()) {
            return;
        }

        mPrivateFlags &= ~FLAG_ENABLED;
        invalidateAvailability();
    }

    public boolean isAvailable() {
        // Check availability if dirty.
        if (isAvailabilityDirty()) {
            boolean available = true;
            if (isEnabled()) {
                for (HexagonComponentSetting dependency : dependencies()) {
                    if (!dependency.isAvailable()) {
                        available = false;
                        break;
                    }
                }
            } else {
                available = false;
            }
            if (available) {
                mPrivateFlags |= FLAG_AVAILABLE;
            } else {
                mPrivateFlags &= ~FLAG_AVAILABLE;
            }
            mPrivateFlags &= ~FLAG_AVAILABILITY_DIRTY;
        }
        return (mPrivateFlags & FLAG_AVAILABLE) != 0;
    }

    /* For test */ boolean isEnabled() {
        return (mPrivateFlags & FLAG_ENABLED) != 0;
    }

    /* For test */ boolean isAvailabilityDirty() {
        return (mPrivateFlags & FLAG_AVAILABILITY_DIRTY) != 0;
    }

    private void invalidateAvailability() {
        mPrivateFlags |= FLAG_AVAILABILITY_DIRTY;

        for (HexagonComponentSetting dependedOn : dependedOn()) {
            dependedOn.invalidateAvailability();
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
