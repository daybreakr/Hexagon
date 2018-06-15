package com.honeycomb.hexagon.sample.hexagonx.conditions;

import com.honeycomb.hexagon.Hexagon;
import com.honeycomb.hexagon.register.RegistrationCondition;
import com.honeycomb.hexagon.sample.modules.loaderinfo.LoaderInfo;
import com.honeycomb.hexagon.sample.modules.loaderinfo.LoaderInfoModule;

public class MinLoaderVersionCondition extends RegistrationCondition {
    private final int mMinLoaderVersion;

    public MinLoaderVersionCondition(int minLoaderVersion) {
        mMinLoaderVersion = minLoaderVersion;

        dependsOn(LoaderInfoModule.class);
    }

    @Override
    public boolean isSatisfied() {
        LoaderInfo loaderInfo = Hexagon.controller(LoaderInfo.class);
        return loaderInfo != null && loaderInfo.getVersionCode() >= mMinLoaderVersion;
    }
}
