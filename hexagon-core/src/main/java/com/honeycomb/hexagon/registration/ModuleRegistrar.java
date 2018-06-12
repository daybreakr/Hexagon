package com.honeycomb.hexagon.registration;

import com.honeycomb.hexagon.HexagonEngine;
import com.honeycomb.hexagon.core.controller.ControllerInfo;
import com.honeycomb.hexagon.core.module.ModuleInfo;
import com.honeycomb.hexagon.core.service.ServiceInfo;

public class ModuleRegistrar {
    private HexagonEngine mEngine;

    public void setEngine(HexagonEngine engine) {
        mEngine = engine;
    }

    public void register(ModuleList moduleList) {
        if (mEngine == null) {
            throw new IllegalStateException("Engine not attached.");
        }

        for (ModuleRegistration module : moduleList.modules()) {

            // Register module.
            registerModule(module);

            // Register controllers.
            for (ControllerRegistration<?> controller : module.controllers()) {
                registerController(controller, module);
            }

            // Register services.
            for (ServiceRegistration<?> service : module.services()) {
                registerService(service, module);
            }
        }
    }

    //==============================================================================================
    // Module registration
    //==============================================================================================

    private void registerModule(ModuleRegistration module) {
        ModuleInfo info = buildModuleInfo(module);
        mEngine.getModuleRegistry().register(info);
    }

    private ModuleInfo buildModuleInfo(ModuleRegistration module) {
        ModuleInfo info = new ModuleInfo();
        info.name = module.name();
        info.enabled = module.enabled();
        return info;
    }

    //==============================================================================================
    // Controller registration
    //==============================================================================================

    private void registerController(ControllerRegistration<?> controller,
                                    ModuleRegistration module) {
        final String apiName = controller.apiClass().getName();
        ControllerInfo info = buildControllerInfo(apiName, controller, module);
        mEngine.getControllerRegistry().register(info, controller.provider());
    }

    private ControllerInfo buildControllerInfo(String name, ControllerRegistration<?> controller,
                                               ModuleRegistration module) {
        ControllerInfo info = new ControllerInfo();
        info.name = name;
        info.enabled = controller.enabled();
        info.module = module.name();
        return info;
    }

    //==============================================================================================
    // Service registration
    //==============================================================================================

    private void registerService(ServiceRegistration<?> service, ModuleRegistration module) {
        ServiceInfo serviceInfo = buildServiceInfo(service, module);
        mEngine.getServiceRegistry().register(serviceInfo, service.provider());
    }

    private ServiceInfo buildServiceInfo(ServiceRegistration<?> service,
                                         ModuleRegistration module) {
        ServiceInfo info = new ServiceInfo();
        info.name = service.serviceClass().getName();
        info.enabled = service.enabled();
        info.module = module.name();
        info.categories.addAll(service.categories());
        return info;
    }
}
