package com.daybreakr.hexagon.core;

import java.util.List;

public abstract class ComponentInfo {
    public final ComponentType type;
    public String name;
    public List<Condition> prerequisites;
    public List<Dependency> dependencies;

    ComponentInfo(ComponentType type) {
        this.type = type;
    }
}
