package com.honeycomb.hexagon;

public class HexagonAssembly {
    private final ModuleRegistry mModuleRegistry;
    private final ControllerRegistry mControllerRegistry;
    private final ServiceRegistry mServiceRegistry;

    private final ModuleRegistrar mModuleRegistrar;
    private final ModuleResolver mModuleResolver;
    private final ServiceManager mServiceManager;

    private HexagonAssembly(Builder builder) {
        mModuleRegistry = builder.moduleRegistry;
        mControllerRegistry = builder.controllerRegistry;
        mServiceRegistry = builder.serviceRegistry;

        mModuleRegistrar = builder.registrar;
        mModuleRegistrar.attachTo(this);

        mModuleResolver = builder.resolver;
        mModuleResolver.attachTo(this);

        mServiceManager = builder.serviceManager;
        mServiceManager.attachTo(this);
    }

    //==============================================================================================
    // Getters
    //==============================================================================================

    public ModuleRegistry moduleRegistry() {
        return mModuleRegistry;
    }

    public ControllerRegistry controllerRegistry() {
        return mControllerRegistry;
    }

    public ServiceRegistry serviceRegistry() {
        return mServiceRegistry;
    }

    public ModuleRegistrar moduleRegistrar() {
        return mModuleRegistrar;
    }

    public ModuleResolver moduleResolver() {
        return mModuleResolver;
    }

    public ServiceManager serviceManager() {
        return mServiceManager;
    }

    //==============================================================================================
    // Factory methods
    //==============================================================================================

    public static HexagonAssembly create() {
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

        public HexagonAssembly build() {
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

            return new HexagonAssembly(this);
        }
    }
}
