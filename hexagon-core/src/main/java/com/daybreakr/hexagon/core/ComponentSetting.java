package com.daybreakr.hexagon.core;

public abstract class ComponentSetting {

    public abstract void enable() throws HexagonAvailabilityException;

    public abstract void disable();

    public abstract boolean isAvailable();

    void invalidateAvailability() {
        // Optional implementation.
    }
}
