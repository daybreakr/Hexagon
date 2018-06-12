package com.honeycomb.hexagon.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModuleRegistry {
    // Contains all registered module info indexing by module name.
    private final Map<String, ModuleInfo> mModules = new HashMap<>();

    public void register(ModuleInfo moduleInfo) {
        // Make a copy of the original module info.
        moduleInfo = new ModuleInfo(moduleInfo);

        // Register module info.
        mModules.put(moduleInfo.name(), moduleInfo);
    }

    public void unregister(String name) {
        mModules.remove(name);
    }

    public boolean isRegistered(String name) {
        return mModules.containsKey(name);
    }

    public ModuleInfo getModuleInfo(String name) {
        ModuleInfo module = mModules.get(name);
        if (module != null) {
            return new ModuleInfo(module);
        }
        return null;
    }

    public List<ModuleInfo> getRegisteredModules() {
        List<ModuleInfo> modules = new LinkedList<>();
        for (ModuleInfo module : mModules.values()) {
            modules.add(new ModuleInfo(module));
        }
        return modules;
    }

    public void resolve(String moduleName, boolean enabled) {
        ModuleInfo module = mModules.get(moduleName);
        if (module != null) {
            module.resolve();
            if (enabled) {
                module.enable();
            }
        }
    }
}
