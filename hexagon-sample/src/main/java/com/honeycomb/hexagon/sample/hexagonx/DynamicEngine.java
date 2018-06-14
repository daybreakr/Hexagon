package com.honeycomb.hexagon.sample.hexagonx;

import com.honeycomb.hexagon.HexagonEngine;
import com.honeycomb.hexagon.core.ControllerInfo;
import com.honeycomb.hexagon.core.IController;
import com.honeycomb.hexagon.core.IService;
import com.honeycomb.hexagon.core.ModuleInfo;
import com.honeycomb.hexagon.core.ServiceInfo;
import com.honeycomb.hexagon.register.ModuleList;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DynamicEngine {
    private static HexagonEngine sEngine;

    private static final AtomicBoolean sStarted = new AtomicBoolean(false);

    public static void start() {
        if (sStarted.compareAndSet(false, true)) {
            DynamicEngineInjection injection = DynamicEngineInjection.create();

            // Create Engine.
            sEngine = injection.provideEngine();

            // Install bootstrap modules.
            install(injection.provideBootstrapModules());
        }
    }

    public static HexagonEngine get() {
        return sEngine;
    }

    //==============================================================================================
    // Install
    //==============================================================================================

    public static void install(ModuleList moduleList) {
        sEngine.installModules(moduleList);
    }

    //==============================================================================================
    // Module interfaces
    //==============================================================================================

    public static List<ModuleInfo> modules() {
        return sEngine.getModuleRegistry().getRegisteredModules();
    }

    //==============================================================================================
    // Controller interfaces
    //==============================================================================================

    public ControllerInfo controllerInfo(String name) {
        return sEngine.getControllerRegistry().getControllerInfo(name);
    }

    public static <T extends IController> T controller(Class<T> controllerClass) {
        return sEngine.getControllerRegistry().getController(controllerClass);
    }

    //==============================================================================================
    // Service interfaces
    //==============================================================================================

    public ServiceInfo serviceInfo(String name) {
        return sEngine.getServiceRegistry().getServiceInfo(name);
    }

    public static boolean start(Class<? extends IService> serviceClass) {
        return sEngine.getServiceManager().startService(serviceClass);
    }

    public static boolean startAll(String category) {
        return sEngine.getServiceManager().startServices(category);
    }

    public static boolean stop(Class<? extends IService> serviceClass) {
        return sEngine.getServiceManager().stopService(serviceClass);
    }

    public static boolean stopAll(String category) {
        return sEngine.getServiceManager().stopServices(category);
    }
}
