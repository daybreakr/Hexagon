package com.honeycomb.hexagon.core;

public class NamedInfo {
    private final String mName;

    public NamedInfo(String name) {
        mName = name;
    }

    public NamedInfo(NamedInfo origin) {
        mName = origin.mName;
    }

    public String name() {
        return mName;
    }
}
