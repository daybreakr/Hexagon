package com.honeycomb.hexagon;

import com.honeycomb.hexagon.core.ControllerInfo;
import com.honeycomb.hexagon.core.IController;
import com.honeycomb.hexagon.core.IService;
import com.honeycomb.hexagon.core.ModuleInfo;
import com.honeycomb.hexagon.core.ServiceInfo;
import com.honeycomb.hexagon.register.ModuleList;

import java.util.List;

public class HexagonEngine extends HexagonComponent {

    public HexagonEngine(HexagonAssembly assembly) {
        if (assembly == null) {
            throw new NullPointerException("Hexagon assembly is null.");
        }
        attachTo(assembly);
    }

    //==============================================================================================
    // Install
    //==============================================================================================

    public void install(ModuleList moduleList) {
        if (moduleList != null) {
            assembly().moduleRegistrar().register(moduleList);

            assembly().moduleResolver().resolve();
        }
    }

    //==============================================================================================
    // Module interfaces
    //==============================================================================================

    public List<ModuleInfo> modules() {
        return assembly().moduleRegistry().getRegisteredModules();
    }

    //==============================================================================================
    // Controller interfaces
    //==============================================================================================

    public ControllerInfo controllerInfo(String name) {
        return assembly().controllerRegistry().getControllerInfo(name);
    }

    public <T extends IController> T controller(Class<T> controllerClass) {
        return assembly().controllerRegistry().getController(controllerClass);
    }

    //==============================================================================================
    // Service interfaces
    //==============================================================================================

    public ServiceInfo serviceInfo(String name) {
        return assembly().serviceRegistry().getServiceInfo(name);
    }

    public boolean start(Class<? extends IService> serviceClass) {
        return assembly().serviceManager().startService(serviceClass);
    }

    public boolean startAll(String category) {
        return assembly().serviceManager().startServices(category);
    }

    public boolean stop(Class<? extends IService> serviceClass) {
        return assembly().serviceManager().stopService(serviceClass);
    }

    public boolean stopAll(String category) {
        return assembly().serviceManager().stopServices(category);
    }

    public boolean stopAll() {
        return assembly().serviceManager().stopAllServices();
    }
}
