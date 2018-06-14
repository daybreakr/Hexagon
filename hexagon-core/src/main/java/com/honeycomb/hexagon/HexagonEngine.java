package com.honeycomb.hexagon;

import com.honeycomb.hexagon.register.ModuleList;

public class HexagonEngine {
    private final ModuleRegistry mModuleRegistry;
    private final ControllerRegistry mControllerRegistry;
    private final ServiceRegistry mServiceRegistry;

    private final ModuleRegistrar mModuleRegistrar;
    private final ModuleResolver mModuleResolver;
    private final ServiceManager mServiceManager;

    private HexagonEngine(Builder builder) {
        mModuleRegistry = builder.moduleRegistry;
        mControllerRegistry = builder.controllerRegistry;
        mServiceRegistry = builder.serviceRegistry;

        mModuleRegistrar = builder.registrar;
        mModuleRegistrar.setEngine(this);

        mModuleResolver = builder.resolver;
        mModuleResolver.setEngine(this);

        mServiceManager = builder.serviceManager;
        mServiceManager.setEngine(this);
    }

    //==============================================================================================
    // Getters
    //==============================================================================================

    public ModuleRegistry getModuleRegistry() {
        return mModuleRegistry;
    }

    public ControllerRegistry getControllerRegistry() {
        return mControllerRegistry;
    }

    public ServiceRegistry getServiceRegistry() {
        return mServiceRegistry;
    }

    public ModuleRegistrar getModuleRegistrar() {
        return mModuleRegistrar;
    }

    public ModuleResolver getModuleResolver() {
        return mModuleResolver;
    }

    public ServiceManager getServiceManager() {
        return mServiceManager;
    }

    //==============================================================================================
    // Install
    //==============================================================================================

    public void installModules(ModuleList moduleList) {
        // Register modules.
        mModuleRegistrar.register(moduleList);

        // Resolve all modules after new modules added.
        mModuleResolver.resolve();
    }

    //==============================================================================================
    // Factory methods
    //==============================================================================================

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

        ModuleRegistrar registrar;
        ModuleResolver resolver;
        ServiceManager serviceManager;

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

        public Builder moduleRegistrar(ModuleRegistrar registrar) {
            this.registrar = registrar;
            return this;
        }

        public Builder moduleResolver(ModuleResolver resolver) {
            this.resolver = resolver;
            return this;
        }

        public Builder serviceManager(ServiceManager manager) {
            this.serviceManager = manager;
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

            if (registrar == null) {
                registrar = new ModuleRegistrar();
            }

            if (resolver == null) {
                resolver = new ModuleResolver();
            }

            if (serviceManager == null) {
                serviceManager = new ServiceManager();
            }

            return new HexagonEngine(this);
        }
    }
}
