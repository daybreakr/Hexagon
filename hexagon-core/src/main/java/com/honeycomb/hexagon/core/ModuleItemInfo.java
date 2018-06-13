package com.honeycomb.hexagon.core;

import com.honeycomb.basement.condition.ICondition;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class ModuleItemInfo {
    private final String mName;
    private String mLabel;
    private boolean mEnabled = true;
    private final List<ICondition> mPrerequisites;
    private final List<String> mDependencies;

    private boolean mResolved = false;

    public ModuleItemInfo(String name) {
        mName = name;
        mPrerequisites = new LinkedList<>();
        mDependencies = new LinkedList<>();
    }

    public ModuleItemInfo(ModuleItemInfo origin) {
        mName = origin.mName;
        mLabel = origin.mLabel;
        mEnabled = origin.mEnabled;
        mPrerequisites = new LinkedList<>(origin.mPrerequisites);
        mDependencies = new LinkedList<>(origin.mDependencies);
        mResolved = origin.mResolved;
    }

    //==============================================================================================
    // Getters
    //==============================================================================================

    public String name() {
        return mName;
    }

    public String label() {
        if (mLabel != null) {
            return mLabel;
        }
        return name();
    }

    public boolean enabled() {
        return mEnabled;
    }

    public List<ICondition> prerequisites() {
        return Collections.unmodifiableList(mPrerequisites);
    }

    public List<String> dependencies() {
        return Collections.unmodifiableList(mDependencies);
    }

    public boolean resolved() {
        return mResolved;
    }

    public boolean active() {
        return enabled() && resolved();
    }

    //==============================================================================================
    // Setters
    //==============================================================================================

    public void label(String label) {
        mLabel = label;
    }

    public void enabled(boolean enabled) {
        mEnabled = enabled;
    }

    public void prerequisites(Collection<ICondition> prerequisites) {
        if (prerequisites != null && !prerequisites.isEmpty()) {
            mPrerequisites.addAll(prerequisites);
        }
    }

    public void dependencies(Collection<String> dependencies) {
        if (dependencies != null && !dependencies.isEmpty()) {
            mDependencies.addAll(dependencies);
        }
    }

    public void resolved(boolean resolved) {
        mResolved = resolved;
    }

    public void enable() {
        mEnabled = true;
    }

    public void disable() {
        mEnabled = false;
    }

    public void resolve() {
        mResolved = true;
    }
}
