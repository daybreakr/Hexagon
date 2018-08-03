package com.daybreakr.hexagon.core;

public class ComponentSettingPhantom extends ComponentSetting {
    private final Dependency mDependency;

    public ComponentSettingPhantom(Dependency dependency) {
        mDependency = dependency;
    }

    @Override
    public void enable() throws HexagonAvailabilityException {
        // Phantom component cannot be enabled.
        throw new HexagonNotRegisteredException(mDependency.name);
    }

    @Override
    public void disable() {
        // Phantom component no need to disable, do nothing.
    }

    @Override
    public boolean isAvailable() {
        // Phantom component always unavailable.
        return false;
    }
}
