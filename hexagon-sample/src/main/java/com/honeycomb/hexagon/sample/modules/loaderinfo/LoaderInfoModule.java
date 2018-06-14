package com.honeycomb.hexagon.sample.modules.loaderinfo;

import com.honeycomb.hexagon.register.ModuleRegistration;

public class LoaderInfoModule extends ModuleRegistration {

    @Override
    protected void onRegister() {
        label("LoaderInfo");

        controller(LoaderInfo.class, provideLoaderInfo());
    }

    private static LoaderInfo provideLoaderInfo() {
        return new LoaderInfoImpl();
    }
}
