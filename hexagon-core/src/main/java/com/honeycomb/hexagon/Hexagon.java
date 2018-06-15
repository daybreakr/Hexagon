package com.honeycomb.hexagon;

import com.honeycomb.hexagon.core.ControllerInfo;
import com.honeycomb.hexagon.core.IController;
import com.honeycomb.hexagon.core.IService;
import com.honeycomb.hexagon.core.ModuleInfo;
import com.honeycomb.hexagon.core.ServiceInfo;
import com.honeycomb.hexagon.register.ModuleList;

import java.util.List;

public class Hexagon {
    private static HexagonEngine sEngine;

    private Hexagon() {
    }

    public static void initialize() {
        initialize(HexagonAssembly.create());
    }

    public static void initialize(HexagonAssembly assembly) {
        initialize(new HexagonEngine(assembly));
    }

    public static void initialize(HexagonEngine engine) {
        if (engine != null) {
            sEngine = engine;
        }
    }

    public static HexagonEngine engine() {
        if (sEngine == null) {
            synchronized (Hexagon.class) {
                if (sEngine == null) {
                    initialize();
                }
            }
        }
        return sEngine;
    }

    //==============================================================================================
    // Install
    //==============================================================================================

    public static void install(ModuleList moduleList) {
        engine().install(moduleList);
    }

    //==============================================================================================
    // Module interfaces
    //==============================================================================================

    public static List<ModuleInfo> modules() {
        return engine().modules();
    }

    //==============================================================================================
    // Controller interfaces
    //==============================================================================================

    public ControllerInfo controllerInfo(String name) {
        return engine().controllerInfo(name);
    }

    public static <T extends IController> T controller(Class<T> controllerClass) {
        return engine().controller(controllerClass);
    }

    //==============================================================================================
    // Service interfaces
    //==============================================================================================

    public ServiceInfo serviceInfo(String name) {
        return engine().serviceInfo(name);
    }

    public static boolean start(Class<? extends IService> serviceClass) {
        return engine().start(serviceClass);
    }

    public static boolean startServices(String category) {
        return engine().startAll(category);
    }

    public static boolean stop(Class<? extends IService> serviceClass) {
        return engine().stop(serviceClass);
    }

    public static boolean stopServices(String category) {
        return engine().stopAll(category);
    }

    public static boolean stopAllServices() {
        return engine().stopAll();
    }
}
