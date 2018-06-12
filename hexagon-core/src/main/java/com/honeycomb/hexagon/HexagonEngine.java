package com.honeycomb.hexagon;

import com.honeycomb.hexagon.core.controller.ControllerRegistry;
import com.honeycomb.hexagon.core.module.ModuleRegistry;
import com.honeycomb.hexagon.core.service.ServiceManager;
import com.honeycomb.hexagon.core.service.ServiceRegistry;
import com.honeycomb.hexagon.registration.ModuleRegistrar;

public class HexagonEngine {
    private final ModuleRegistry mModuleRegistry;
    private final ControllerRegistry mControllerRegistry;
    private final ServiceRegistry mServiceRegistry;

    private final ServiceManager mServiceManager;
    private final ModuleRegistrar mModuleRegistrar;

    private HexagonEngine(Builder builder) {
        mModuleRegistry = builder.moduleRegistry;
        mControllerRegistry = builder.controllerRegistry;
        mServiceRegistry = builder.serviceRegistry;

        mServiceManager = builder.serviceManager;
        mServiceManager.setEngine(this);

        mModuleRegistrar = builder.registrar;
        mModuleRegistrar.setEngine(this);
    }

    public ModuleRegistry getModuleRegistry() {
        return mModuleRegistry;
    }

    public ControllerRegistry getControllerRegistry() {
        return mControllerRegistry;
    }

    public ServiceRegistry getServiceRegistry() {
        return mServiceRegistry;
    }

    public ServiceManager getServiceManager() {
        return mServiceManager;
    }

    public ModuleRegistrar getModuleRegistrar() {
        return mModuleRegistrar;
    }

    public static HexagonEngine create() {
        return buildUpon().build();
    }

    public static Builder buildUpon() {
        return new Builder();
    }

    public static class Builder {
        ModuleRegistry moduleRegistry;
        ControllerRegistry controllerRegistry;
        ServiceRegistry serviceRegistry;

        ServiceManager serviceManager;
        ModuleRegistrar registrar;

        public Builder moduleRegistry(ModuleRegistry registry) {
            this.moduleRegistry = registry;
            return this;
        }

        public Builder controllerRegistry(ControllerRegistry registry) {
            this.controllerRegistry = registry;
            return this;
        }

        public Builder serviceRegistry(ServiceRegistry registry) {
            this.serviceRegistry = registry;
            return this;
        }

        public Builder serviceManager(ServiceManager manager) {
            this.serviceManager = manager;
            return this;
        }

        public Builder moduleRegistrar(ModuleRegistrar registrar) {
            this.registrar = registrar;
            return this;
        }

        public HexagonEngine build() {
            if (moduleRegistry == null) {
                moduleRegistry = new ModuleRegistry();
            }

            if (controllerRegistry == null) {
                controllerRegistry = new ControllerRegistry();
            }

            if (serviceRegistry == null) {
                serviceRegistry = new ServiceRegistry();
            }

            if (serviceManager == null) {
                serviceManager = new ServiceManager();
            }

            if (registrar == null) {
                registrar = new ModuleRegistrar();
            }

            return new HexagonEngine(this);
        }
    }
}
