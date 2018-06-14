package com.honeycomb.hexagon;

import com.honeycomb.hexagon.core.ModuleInfo;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public Set<String> getRegisteredModuleNames() {
        return new LinkedHashSet<>(mModules.keySet());
    }

    public List<ModuleInfo> getRegisteredModules() {
        List<ModuleInfo> modules = new LinkedList<>();
        for (ModuleInfo module : mModules.values()) {
            modules.add(new ModuleInfo(module));
        }
        return modules;
    }

    public void resolved(String name, boolean enabled) {
        ModuleInfo module = mModules.get(name);
        if (module != null) {
            module.enabled(enabled);
            module.resolve();
        }
    }
}
