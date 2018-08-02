package com.honeycomb.hexagonx.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HexagonServiceSettingManager {
    private final Map<String, HexagonServiceSetting> mSettings = new HashMap<>();

    public void add(HexagonServiceSetting setting) {
        if (setting != null) {
            mSettings.put(setting.name, setting);
        }
    }

    public List<HexagonServiceSetting> getAll() {
        return new ArrayList<>(mSettings.values());
    }

    public void enable(String serviceName) throws HexagonAvailabilityException {
        HexagonServiceSetting setting = mSettings.get(serviceName);
        if (setting == null) {
            throw new HexagonNotRegisteredException(serviceName);
        }

        setting.enable();
    }

    public void disable(String serviceName) throws HexagonAvailabilityException {
        HexagonServiceSetting setting = mSettings.get(serviceName);
        if (setting == null) {
            throw new HexagonNotRegisteredException(serviceName);
        }

        setting.disable();
    }
}
