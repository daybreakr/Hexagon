package com.honeycomb.hexagon.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Condition {
    private List<String> mDependencies;

    public abstract boolean isSatisfied();

    public List<String> dependencies() {
        if (mDependencies != null) {
            return Collections.unmodifiableList(mDependencies);
        }
        return Collections.emptyList();
    }
    
    protected Condition dependsOn(String moduleName) {
        if (moduleName != null) {
            if (mDependencies == null) {
                mDependencies = new LinkedList<>();
            }
            if (!mDependencies.contains(moduleName)) {
                mDependencies.add(moduleName);
            }
        }
        return this;
    }
}
