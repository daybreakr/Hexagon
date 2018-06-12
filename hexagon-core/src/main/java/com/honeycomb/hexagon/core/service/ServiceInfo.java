package com.honeycomb.hexagon.core.service;

import java.util.HashSet;
import java.util.Set;

public class ServiceInfo {
    public String name;
    public String module;
    public boolean enabled = true;
    public final Set<String> categories;

    public ServiceInfo() {
        this.categories = new HashSet<>();
    }

    public ServiceInfo(ServiceInfo origin) {
        this.name = origin.name;
        this.module = origin.module;
        this.enabled = origin.enabled;
        this.categories = new HashSet<>(origin.categories);
    }
}
