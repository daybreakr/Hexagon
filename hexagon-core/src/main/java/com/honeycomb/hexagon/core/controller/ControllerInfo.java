package com.honeycomb.hexagon.core.controller;

public class ControllerInfo {
    public String name;
    public String module;
    public boolean enabled = true;

    public ControllerInfo() {
    }

    public ControllerInfo(ControllerInfo origin) {
        this.name = origin.name;
        this.module = origin.module;
        this.enabled = origin.enabled;
    }
}
