package com.honeycomb.hexagon.register;

import com.honeycomb.provider.ReflectiveProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ModuleList {
    private Map<String, ModuleRegistration> mModules;

    protected abstract void register();

    public List<ModuleRegistration> modules() {
        if (mModules == null) {
            synchronized (this) {
                if (mModules == null) {
                    mModules = new LinkedHashMap<>();
                    register();
                }
            }
        }
        return new ArrayList<>(mModules.values());
    }

    protected void module(ModuleRegistration module) {
        if (module != null) {
            module.register();

            String name = module.name();
            if (mModules.containsKey(name)) {
                // TODO: Warn when module already registered.
                return;
            }
            mModules.put(name, module);
        }
    }

    protected void module(Class<? extends ModuleRegistration> moduleClass) {
        if (moduleClass != null) {
            try {
                module(new ReflectiveProvider<>(moduleClass).get());
            } catch (Exception e) {
                // TODO: Warn if failed to create module.
            }
        }
    }
}
