package com.honeycomb.hexagon.core;

public class ResolvableInfo extends NamedInfo {
    private boolean mEnabled;
    private boolean mResolved;

    public ResolvableInfo(String name) {
        super(name);
        mEnabled = true;
        mResolved = false;
    }

    public ResolvableInfo(ResolvableInfo origin) {
        super(origin);
        mEnabled = origin.mEnabled;
        mResolved = origin.mResolved;
    }

    public boolean enabled() {
        // Only enable resolved items.
        return resolved() && mEnabled;
    }

    public boolean resolved() {
        return mResolved;
    }

    public void enabled(boolean enabled) {
        mEnabled = enabled;
    }

    public void enable() {
        mEnabled = true;
    }

    public void disable() {
        mEnabled = false;
    }

    public void resolved(boolean resolved) {
        mResolved = resolved;
    }

    public void resolve() {
        mResolved = true;
    }
}
