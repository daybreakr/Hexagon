package com.honeycomb.hexagonx.core;

public class HexagonAvailabilityException extends Exception {
    private final String mComponentName;
    private final boolean mIsRegistered;

    public HexagonAvailabilityException(String componentName, boolean isRegistered) {
        super();
        mComponentName = componentName;
        mIsRegistered = isRegistered;
    }

    public HexagonAvailabilityException(String componentName, boolean isRegistered,
                                        String message) {
        super(message);
        mComponentName = componentName;
        mIsRegistered = isRegistered;
    }

    public HexagonAvailabilityException(String componentName, boolean isRegistered,
                                        String message, Throwable cause) {
        super(message, cause);
        mComponentName = componentName;
        mIsRegistered = isRegistered;
    }

    public String getComponentName() {
        return mComponentName;
    }

    public boolean isRegistered() {
        return mIsRegistered;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Hexagon component: ").append(getComponentName());
        if (isRegistered()) {
            message.append(" not active. ");
        } else {
            message.append(" not registered. ");
        }
        String extra = super.getMessage();
        if (extra != null) {
            message.append(extra);
        }
        return message.toString();
    }
}
