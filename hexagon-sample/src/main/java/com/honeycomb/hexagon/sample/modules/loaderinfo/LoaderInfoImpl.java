package com.honeycomb.hexagon.sample.modules.loaderinfo;

public class LoaderInfoImpl extends LoaderInfo {

    @Override
    public int getVersionCode() {
        return VERSION_CODES.V_2_0_0;
    }

    @Override
    public String getAppServerBaseUrl() {
        return "http://app.mmappstore.com:8080/";
    }

    @Override
    public String getConsoleServerBaseUrl() {
        return "http://nest.mmappstore.com:8080/";
    }

    @Override
    public String getSecret() {
        return "FDA0889D0037AB89A07B";
    }
}
