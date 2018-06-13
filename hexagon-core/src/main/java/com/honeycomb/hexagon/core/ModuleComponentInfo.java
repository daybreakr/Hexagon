package com.honeycomb.hexagon.core;

public abstract class ModuleComponentInfo extends ModuleItemInfo {
    private final String mModule;

    public ModuleComponentInfo(String name, String module) {
        super(name);
        mModule = module;
    }

    public ModuleComponentInfo(ModuleComponentInfo origin) {
        super(origin);
        mModule = origin.mModule;
    }

    public String module() {
        return mModule;
    }
}
