package com.honeycomb.hexagon.sample.modules.dynamicinfo;

import com.honeycomb.hexagon.register.ModuleRegistration;

public class DynamicInfoModule extends ModuleRegistration {

    @Override
    protected void onRegister() {
        label("DynamicInfo");

        controller(DynamicInfo.class, provideDynamicInfo());
    }

    private static DynamicInfo provideDynamicInfo() {
        return new DynamicInfoImpl();
    }
}
