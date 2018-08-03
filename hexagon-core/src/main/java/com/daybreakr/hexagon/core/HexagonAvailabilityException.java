package com.daybreakr.hexagon.core;

public class HexagonAvailabilityException extends Exception {
    private final String mComponentName;

    public HexagonAvailabilityException(String componentName) {
        super();
        mComponentName = componentName;
    }

    public HexagonAvailabilityException(String componentName, String message) {
        super(message);
        mComponentName = componentName;
    }

    public HexagonAvailabilityException(String componentName, String message, Throwable cause) {
        super(message, cause);
        mComponentName = componentName;
    }

    public String getComponentName() {
        return mComponentName;
    }

    public String getExtraMessage() {
        return super.getMessage();
    }

    @Override
    public String getMessage() {
        return "Hexagon component: " + getComponentName() + " not available. " + getExtraMessage();
    }
}
