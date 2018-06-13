package com.honeycomb.hexagon.core;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModuleInfo extends ModuleItemInfo {
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

    //==============================================================================================
    // Getters
    //==============================================================================================

    public Set<String> controllers() {
        return Collections.unmodifiableSet(mControllers);
    }

    public Set<String> services() {
        return Collections.unmodifiableSet(mServices);
    }

    //==============================================================================================
    // Setters
    //==============================================================================================

    public void controllers(Collection<String> controllers) {
        if (controllers != null && !controllers.isEmpty()) {
            mControllers.addAll(controllers);
        }
    }

    public void services(Collection<String> services) {
        if (services != null && !services.isEmpty()) {
            mServices.addAll(services);
        }
    }
}
