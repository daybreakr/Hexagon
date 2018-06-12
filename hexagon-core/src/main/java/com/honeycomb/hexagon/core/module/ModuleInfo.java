package com.honeycomb.hexagon.core.module;

public class ModuleInfo {
    public String name;
    public boolean enabled = true;

    public ModuleInfo() {
    }

    public ModuleInfo(ModuleInfo origin) {
        this.name = origin.name;
        this.enabled = origin.enabled;
    }
}
