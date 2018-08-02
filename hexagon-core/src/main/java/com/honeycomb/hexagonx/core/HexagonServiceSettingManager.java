package com.honeycomb.hexagonx.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HexagonServiceSettingManager {
    private final Map<String, HexagonServiceSetting> mSettings = new HashMap<>();

    public void add(HexagonServiceSetting setting) {
        if (setting != null) {
            mSettings.put(setting.name, setting);
        }
    }

    public List<HexagonServiceSetting> getBootList() {
        List<HexagonServiceSetting> bootList = new LinkedList<>();
        for (HexagonServiceSetting setting : mSettings.values()) {
            addToBootList(setting, bootList);
        }
        return bootList;
    }

    private void addToBootList(HexagonServiceSetting setting,
                               List<HexagonServiceSetting> bootList) {
        if (bootList.contains(setting)) {
            // Do nothing if already in boot list.
            return;
        }
        // Add dependencies to boot list first.
        if (setting.dependencies != null) {
            for (HexagonServiceSetting dependency : setting.dependencies) {
                addToBootList(dependency, bootList);
            }
        }
        // Add specific setting to boot list at last.
        bootList.add(setting);
    }

    public void activate(String serviceName) throws HexagonAvailabilityException {
        activate(serviceName, true);
    }

    public void activate(String serviceName, boolean enableDependedOn)
            throws HexagonAvailabilityException {
        HexagonServiceSetting setting = mSettings.get(serviceName);
        if (setting == null) {
            throw new HexagonNotRegisteredException(serviceName);
        }

        setting.active = true;

        enableSetting(setting, enableDependedOn);
    }

    private void enableSetting(HexagonServiceSetting setting, boolean enableDependedOn)
            throws HexagonAvailabilityException {
        if (setting.enabled) {
            return;
        }

        // Check dependencies first.
        if (setting.dependencies != null) {
            for (HexagonServiceSetting dependency : setting.dependencies) {
                if (!dependency.isAvailable()) {
                    throw new HexagonAvailabilityException(setting.name,
                            "Dependency not available: " + dependency);
                }
            }
        }

        // Check conditions.
        if (setting.conditions != null) {
            for (Condition condition : setting.conditions) {
                if (!condition.isSatisfied()) {
                    throw new HexagonAvailabilityException(setting.name,
                            "Condition not satisfied: " + condition.getDescription());
                }
            }
        }

        // All passed, enable component
        setting.enabled = true;

        // Enable relative components if needed.
        if (enableDependedOn && setting.dependedOn != null) {
            for (HexagonServiceSetting dependedOn : setting.dependedOn) {
                try {
                    enableSetting(dependedOn, true);
                } catch (HexagonAvailabilityException ignored) {
                }
            }
        }
    }

    public void deactivate(String serviceName) throws HexagonAvailabilityException {
        deactivate(serviceName, true);
    }

    public void deactivate(String serviceName, boolean disableDependedOn)
            throws HexagonAvailabilityException {
        HexagonServiceSetting setting = mSettings.get(serviceName);
        if (setting == null) {
            throw new HexagonNotRegisteredException(serviceName);
        }

        setting.active = false;

        // Only disable depended on components.
        if (disableDependedOn && setting.dependedOn != null) {
            for (HexagonServiceSetting dependedOn : setting.dependedOn) {
                disableSetting(dependedOn, true);
            }
        }
    }

    @SuppressWarnings({"SameParameterValue"})
    private void disableSetting(HexagonServiceSetting setting, boolean disableDependedOn) {
        if (!setting.enabled) {
            return;
        }

        setting.enabled = false;

        if (disableDependedOn && setting.dependedOn != null) {
            for (HexagonServiceSetting dependedOn : setting.dependedOn) {
                disableSetting(dependedOn, true);
            }
        }
    }
}
