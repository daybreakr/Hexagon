package com.daybreakr.hexagon.core;

public class ComponentSettingWrapper extends ComponentSetting {
    private ComponentSetting mDelegate;

    public void setDelegate(ComponentSetting delegate) {
        if (delegate != null) {
            mDelegate = delegate;
        }
    }

    public ComponentSetting getDelegate() {
        return mDelegate;
    }

    @Override
    public void enable() throws HexagonAvailabilityException {
        enforceDelegate();
        mDelegate.enable();
    }

    @Override
    public void disable() {
        enforceDelegate();
        mDelegate.disable();
    }

    @Override
    public boolean isAvailable() {
        enforceDelegate();
        return mDelegate.isAvailable();
    }

    private void enforceDelegate() {
        if (mDelegate == null) {
            throw new IllegalStateException("Delegate not attached.");
        }
    }
}
