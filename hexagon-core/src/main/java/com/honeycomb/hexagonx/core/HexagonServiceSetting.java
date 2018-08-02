package com.honeycomb.hexagonx.core;

import java.util.ArrayList;
import java.util.List;

public class HexagonServiceSetting {
    public final String name;
    public String label;

    public List<HexagonServiceSetting> dependencies;
    public List<HexagonServiceSetting> dependedOn;

    public List<Condition> conditions;

    public boolean enabled;
    public boolean active;

    public HexagonServiceSetting(String name) {
        this(name, name);
    }

    public HexagonServiceSetting(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public boolean isAvailable() {
        return this.enabled && this.active;
    }

    public HexagonServiceSetting dependsOn(HexagonServiceSetting dependency) {
        if (dependency != null) {
            if (this.dependencies == null) {
                this.dependencies = new ArrayList<>();
            }
            if (!this.dependencies.contains(dependency)) {
                this.dependencies.add(dependency);
            }

            if (dependency.dependedOn == null) {
                dependency.dependedOn = new ArrayList<>();
            }
            if (!dependency.dependedOn.contains(this)) {
                dependency.dependedOn.add(this);
            }
        }
        return this;
    }

    public HexagonServiceSetting condition(Condition condition) {
        if (condition != null) {
            if (this.conditions == null) {
                this.conditions = new ArrayList<>();
            }
            if (!this.conditions.contains(condition)) {
                this.conditions.add(condition);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
