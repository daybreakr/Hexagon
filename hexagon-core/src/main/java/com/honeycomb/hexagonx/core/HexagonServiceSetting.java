package com.honeycomb.hexagonx.core;

import java.util.List;

public class HexagonServiceSetting {
    public final String name;
    public final String label;

    public List<String> dependencies;
    public List<String> dependedBy;

    public List<Condition> conditions;

    private boolean mEnabled;

    public HexagonServiceSetting(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void enable() throws HexagonAvailabilityException {
        if (isEnabled()) {
            return;
        }

        if (conditions != null) {
            for (Condition condition : conditions) {
                if (!condition.isSatisfied()) {
                    availabilityError("Condition not satisfied: " + condition.getDescription());
                    return;
                }
            }
        }


    }

    public boolean disable() {

    }

    private void availabilityError(String message) throws HexagonAvailabilityException {
        throw new HexagonAvailabilityException(this.name, true, message);
    }
}
