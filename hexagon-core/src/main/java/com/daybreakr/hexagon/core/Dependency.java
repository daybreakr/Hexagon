package com.daybreakr.hexagon.core;

public class Dependency {

    public enum Level {
        OPTIONAL,
        REQUIRED,
    }

    public final ComponentType type;
    public final String name;
    public final Level level;

    public Dependency(ComponentType type, String name) {
        this(type, name, null);
    }

    public Dependency(ComponentType type, String name, Level level) {
        this.type = type;
        this.name = name;
        if (level == null) {
            level = Level.REQUIRED;
        }
        this.level = level;
    }
}
