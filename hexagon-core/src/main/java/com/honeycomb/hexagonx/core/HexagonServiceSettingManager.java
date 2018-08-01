package com.honeycomb.hexagonx.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HexagonServiceSettingManager {
    private final Map<String, HexagonServiceSetting> mSettings = new HashMap<>();

    private void enable(String serviceName, Set<String> resolved)
            throws HexagonAvailabilityException {
        try {
            HexagonServiceSetting setting = mSettings.get(serviceName);
            if (setting == null) {
                // Service not registered.
                throw new HexagonAvailabilityException(serviceName, false);
            }

            // Already enabled, quick return.
            if (setting.isEnabled()) {
                return;
            }

            // Try enable dependencies first.
            if (setting.dependencies != null) {
                for (String name : setting.dependencies) {
                    if (resolved != null && resolved.contains(name)) {
                        // Quick failure if resolved dependency was disabled.
                        if (!isEnabled(name)) {
                            throw new HexagonAvailabilityException(serviceName, true,
                                    "Dependent service was disabled: " + name);
                        }
                    } else {
                        try {
                            enable(name, resolved);
                        } catch (HexagonAvailabilityException e) {
                            throw new HexagonAvailabilityException(serviceName, true,
                                    "Failed to enable dependent service: " + name, e);
                        }
                    }
                }
            }

            // Check required conditions, including the dependencies.
            if (setting.conditions != null) {
                for (Condition condition : setting.conditions) {
                    if (!condition.isSatisfied()) {
                        throw new HexagonAvailabilityException(serviceName, true,
                                "Condition not satisfied: " + condition.getDescription());
                    }
                }
            }

            setting.enable();

            // Try enable relative services.
            if (setting.dependedBy != null) {
                for (String name : setting.dependedBy) {
                    try {
                        enable(name, resolved);
                    } catch (HexagonAvailabilityException ignored) {
                    }
                }
            }
        } finally {
            if (resolved == null) {
                resolved = new HashSet<>();
            }
            if (!resolved.contains(serviceName)) {
                resolved.add(serviceName);
            }
        }
    }

    public void disable(String serviceName) throws HexagonAvailabilityException {
        HexagonServiceSetting setting = mSettings.get(serviceName);
        if (setting == null) {
            throw new HexagonAvailabilityException(serviceName, false);
        }

        if (!setting.isEnabled()) {
            return;
        }

        setting.disable();

        // Try disable relative services.
        if (setting.dependedBy != null) {
            for (String name : setting.dependedBy) {
                try {
                    disable(name);
                } catch (HexagonAvailabilityException ignored) {
                }
            }
        }
    }

    public boolean isEnabled(String serviceName) {
        HexagonServiceSetting setting = mSettings.get(serviceName);
        return setting != null && setting.isEnabled();
    }
}
