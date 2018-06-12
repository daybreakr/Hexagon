package com.honeycomb.hexagon.register;

import com.honeycomb.hexagon.HexagonEngine;
import com.honeycomb.hexagon.core.ControllerInfo;
import com.honeycomb.hexagon.core.ModuleInfo;
import com.honeycomb.hexagon.core.ServiceInfo;

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

    private void registerModule(ModuleRegistration module) {
        ModuleInfo info = new ModuleInfo(module.name());
        info.enabled(module.enabled());

        mEngine.getModuleRegistry().register(info);
    }

    private void registerController(ControllerRegistration<?> controller,
                                    ModuleRegistration module) {
        final String name = controller.apiClass().getName();
        final boolean enabled = module.enabled() && controller.enabled();

        ControllerInfo info = new ControllerInfo(name, module.name());
        info.enabled(enabled);

        mEngine.getControllerRegistry().register(info, controller.provider());
    }

    private void registerService(ServiceRegistration<?> service, ModuleRegistration module) {
        final String name = service.serviceClass().getName();
        final boolean enabled = module.enabled() && service.enabled();

        ServiceInfo info = new ServiceInfo(name, module.name());
        info.enabled(enabled);
        info.categorise(service.categories());

        mEngine.getServiceRegistry().register(info, service.provider());
    }
}
