package com.daybreakr.hexagon.core;

import java.util.List;

public class ComponentSettingImpl extends ComponentSetting {
    private final ComponentInfo mComponentInfo;

    private List<ComponentSetting> mDependencies;
    private List<ComponentSetting> mDependedOns;

    private int mPrivateFlags;

    private static final int FLAG_ENABLED = 1 << 1;                // 0x00000010
    private static final int FLAG_AVAILABLE = 1 << 2;              // 0x00000100
    private static final int FLAG_AVAILABILITY_DIRTY = 1 << 3;     // 0x00001000

    public ComponentSettingImpl(ComponentInfo componentInfo) {
        mComponentInfo = componentInfo;
    }

    @Override
    public void enable() throws HexagonAvailabilityException {
        if (isEnabled()) {
            return;
        }

        if (mComponentInfo.prerequisites != null) {
            for (Condition prerequisite : mComponentInfo.prerequisites) {
                if (!prerequisite.isSatisfied()) {
                    throw new HexagonAvailabilityException(mComponentInfo.name,
                            "Prerequisite not satisfied: " + prerequisite.getDescription());
                }
            }
        }

        mPrivateFlags |= FLAG_ENABLED;
        invalidateAvailability();
    }

    @Override
    public void disable() {
        if (!isEnabled()) {
            return;
        }

        mPrivateFlags &= ~FLAG_ENABLED;
        invalidateAvailability();
    }

    @Override
    public boolean isAvailable() {
        // Check availability if dirty.
        if (isAvailabilityDirty()) {
            boolean available = true;
            if (isEnabled()) {
                if (mDependencies != null) {
                    for (ComponentSetting dependency : mDependencies) {
                        if (!dependency.isAvailable()) {
                            available = false;
                            break;
                        }
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

    @Override
    void invalidateAvailability() {
        mPrivateFlags |= FLAG_AVAILABILITY_DIRTY;

        if (mDependedOns != null) {
            for (ComponentSetting setting : mDependedOns) {
                setting.invalidateAvailability();
            }
        }
    }

    /* For test */ boolean isEnabled() {
        return (mPrivateFlags & FLAG_ENABLED) != 0;
    }

    /* For test */ boolean isAvailabilityDirty() {
        return (mPrivateFlags & FLAG_AVAILABILITY_DIRTY) != 0;
    }

    @Override
    public String toString() {
        return mComponentInfo.name;
    }
}
