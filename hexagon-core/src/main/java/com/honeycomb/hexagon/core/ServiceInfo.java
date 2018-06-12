package com.honeycomb.hexagon.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServiceInfo extends ModuleComponentInfo {
    private final Set<String> mCategories;

    public ServiceInfo(String name, String module) {
        super(name, module);
        mCategories = new HashSet<>();
    }

    public ServiceInfo(ServiceInfo origin) {
        super(origin);
        mCategories = new HashSet<>(origin.mCategories);
    }

    public Set<String> categories() {
        return Collections.unmodifiableSet(mCategories);
    }

    public void categorise(String... categories) {
        if (categories != null && categories.length > 0) {
            Collections.addAll(mCategories, categories);
        }
    }

    public void categorise(Collection<String> categories) {
        if (categories != null && categories.size() > 0) {
            mCategories.addAll(categories);
        }
    }
}
