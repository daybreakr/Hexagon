package com.honeycomb.hexagon.sample.modules.loaderinfo;

import com.honeycomb.hexagon.core.IController;

public abstract class LoaderInfo implements IController {

    public abstract int getVersionCode();

    public abstract String getAppServerBaseUrl();

    public abstract String getConsoleServerBaseUrl();

    public abstract String getSecret();

    public static class VERSION_CODES {
        public static final int V_1_0_0 = 10000;
        public static final int V_1_0_11 = 10011;
        public static final int V_2_0_0 = 20000;
    }
}
