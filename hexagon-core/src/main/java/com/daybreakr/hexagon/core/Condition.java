package com.daybreakr.hexagon.core;

public abstract class Condition {

    public abstract boolean isSatisfied();

    public String getDescription() {
        return getClass().getSimpleName();
    }
}
