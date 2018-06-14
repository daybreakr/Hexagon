package com.honeycomb.hexagon.sample.modules.update;

import com.honeycomb.basement.provider.IProvider;
import com.honeycomb.basement.provider.SingletonProvider;
import com.honeycomb.hexagon.sample.hexagonx.DynamicServices;
import com.honeycomb.hexagon.sample.hexagonx.register.DynamicModuleRegistration;
import com.honeycomb.hexagon.sample.modules.loaderinfo.LoaderInfo;

public class UpdateModule extends DynamicModuleRegistration {

    @Override
    protected void onRegister() {
        minLoaderVersion(LoaderInfo.VERSION_CODES.V_1_0_11);

        controller(UpdateController.class, provideUpdateController());

        service(CheckForUpdatesService.class, provideCheckForUpdatesService())
                .categorise(DynamicServices.CATEGORY_STARTUP);
    }

    private static IProvider<UpdateController> provideUpdateController() {
        return new SingletonProvider<UpdateController>() {
            @Override
            protected UpdateController createInstance() {
                return new UpdateController();
            }
        };
    }

    private static IProvider<CheckForUpdatesService> provideCheckForUpdatesService() {
        return new SingletonProvider<CheckForUpdatesService>() {
            @Override
            public CheckForUpdatesService createInstance() {
                return new CheckForUpdatesService();
            }
        };
    }
}
