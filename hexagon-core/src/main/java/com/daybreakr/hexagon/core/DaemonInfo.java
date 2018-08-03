package com.daybreakr.hexagon.core;

import java.util.List;

public class DaemonInfo extends ComponentInfo {
    List<String> categories;

    public DaemonInfo() {
        super(ComponentType.DAEMON);
    }
}
