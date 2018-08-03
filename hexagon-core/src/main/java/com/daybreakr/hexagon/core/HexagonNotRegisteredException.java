package com.daybreakr.hexagon.core;

public class HexagonNotRegisteredException extends HexagonAvailabilityException {

    public HexagonNotRegisteredException(String componentName) {
        super(componentName);
    }

    public HexagonNotRegisteredException(String componentName, String message) {
        super(componentName, message);
    }

    public HexagonNotRegisteredException(String componentName, String message, Throwable cause) {
        super(componentName, message, cause);
    }

    @Override
    public String getExtraMessage() {
        return "Such component not registered. " + super.getExtraMessage();
    }
}
