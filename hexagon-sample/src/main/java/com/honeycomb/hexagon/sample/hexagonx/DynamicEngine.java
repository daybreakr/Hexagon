package com.honeycomb.hexagon.sample.hexagonx;

import com.honeycomb.hexagon.HexagonEngine;
import com.honeycomb.hexagon.core.IController;
import com.honeycomb.hexagon.core.IService;
import com.honeycomb.hexagon.register.ModuleList;

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
            ModuleList bootstrapModules = injection.provideBootstrapModules();
            sEngine.installModules(bootstrapModules);
        }
    }

    public static HexagonEngine get() {
        return sEngine;
    }

    //==============================================================================================
    // Controller interfaces
    //==============================================================================================

    public static <T extends IController> T controller(Class<T> controllerClass) {
        return sEngine.getController(controllerClass);
    }

    //==============================================================================================
    // Service interfaces
    //==============================================================================================

    public static boolean start(Class<? extends IService> serviceClass) {
        return sEngine.startService(serviceClass);
    }

    public static boolean startAll(String category) {
        return sEngine.startServices(category);
    }

    public static boolean stop(Class<? extends IService> serviceClass) {
        return sEngine.stopService(serviceClass);
    }

    public static boolean stopAll(String category) {
        return sEngine.stopServices(category);
    }
}
