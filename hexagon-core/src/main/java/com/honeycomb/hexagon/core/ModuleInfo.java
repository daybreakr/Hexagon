package com.honeycomb.hexagon.core;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModuleInfo extends ResolvableInfo {
    private final Set<String> mControllers;
    private final Set<String> mServices;

    public ModuleInfo(String name) {
        super(name);
        mControllers = new LinkedHashSet<>();
        mServices = new LinkedHashSet<>();
    }

    public ModuleInfo(ModuleInfo origin) {
        super(origin);
        mControllers = new LinkedHashSet<>(origin.mControllers);
        mServices = new LinkedHashSet<>(origin.mServices);
    }

    public Set<String> controllers() {
        return Collections.unmodifiableSet(mControllers);
    }

    public Set<String> services() {
        return Collections.unmodifiableSet(mServices);
    }

    public void controller(String controllerName) {
        if (controllerName != null) {
            mControllers.add(controllerName);
        }
    }

    public void service(String service) {
        if (service != null) {
            mServices.add(service);
        }
    }
}
