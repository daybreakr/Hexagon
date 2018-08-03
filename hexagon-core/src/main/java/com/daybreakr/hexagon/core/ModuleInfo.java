package com.daybreakr.hexagon.core;

import java.util.List;

public class ModuleInfo extends ComponentInfo {
    public List<ComponentInfo> components;

    public ModuleInfo() {
        super(ComponentType.MODULE);
    }
}
