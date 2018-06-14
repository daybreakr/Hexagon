package com.honeycomb.hexagon.register;

import com.honeycomb.basement.provider.IProvider;
import com.honeycomb.hexagon.core.Condition;
import com.honeycomb.hexagon.core.IController;
import com.honeycomb.hexagon.core.IService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ModuleRegistration {
    private final String mName = getClass().getName();

    private String mLabel;
    private boolean mEnabled = true;
    private List<Condition> mPrerequisites;
    private List<String> mDependencies;

    private List<ControllerRegistration<?>> mControllers;
    private List<ServiceRegistration<?>> mServices;

    private final AtomicBoolean mRegistered = new AtomicBoolean(false);

    public void register() {
        // Make sure this registration only register once.
        if (mRegistered.compareAndSet(false, true)) {
            onRegister();
        }
    }

    protected abstract void onRegister();

    //==============================================================================================
    // Getters
    //==============================================================================================

    public String name() {
        return mName;
    }

    public String label() {
        return mLabel;
    }

    public boolean enabled() {
        return mEnabled;
    }

    public List<Condition> prerequisites() {
        if (mPrerequisites != null) {
            return Collections.unmodifiableList(mPrerequisites);
        }
        return Collections.emptyList();
    }

    public List<String> dependencies() {
        if (mDependencies != null) {
            return Collections.unmodifiableList(mDependencies);
        }
        return Collections.emptyList();
    }

    public List<ControllerRegistration<?>> controllers() {
        if (mControllers != null) {
            return Collections.unmodifiableList(mControllers);
        }
        return Collections.emptyList();
    }

    public List<ServiceRegistration<?>> services() {
        if (mServices != null) {
            return Collections.unmodifiableList(mServices);
        }
        return Collections.emptyList();
    }

    //==============================================================================================
    // Setters
    //==============================================================================================

    protected ModuleRegistration label(String label) {
        mLabel = label;
        return this;
    }

    protected ModuleRegistration enable() {
        mEnabled = true;
        return this;
    }

    protected ModuleRegistration disable() {
        mEnabled = false;
        return this;
    }

    protected ModuleRegistration require(Condition condition) {
        if (condition != null) {
            if (mPrerequisites == null) {
                mPrerequisites = new LinkedList<>();
            }
            if (!mPrerequisites.contains(condition)) {
                mPrerequisites.add(condition);
                for (String dependency : condition.dependencies()) {
                    dependsOn(dependency);
                }
            }
        }
        return this;
    }

    protected ModuleRegistration dependsOn(Class<? extends ModuleRegistration> moduleClass) {
        final String moduleName = moduleClass.getName();
        return dependsOn(moduleName);
    }

    protected ModuleRegistration dependsOn(String moduleName) {
        if (moduleName != null) {
            if (mDependencies == null) {
                mDependencies = new LinkedList<>();
            }
            if (!mDependencies.contains(moduleName)) {
                mDependencies.add(moduleName);
            }
        }
        return this;
    }

    protected <T extends IController> ControllerRegistration<T> register(ControllerRegistration<T> controller) {
        if (controller != null) {
            if (mControllers == null) {
                mControllers = new LinkedList<>();
            }
            if (!mControllers.contains(controller)) {
                mControllers.add(controller);
            }
        }
        return controller;
    }

    protected <T extends IService> ServiceRegistration<T> register(ServiceRegistration<T> service) {
        if (service != null) {
            if (mServices == null) {
                mServices = new LinkedList<>();
            }
            if (!mServices.contains(service)) {
                mServices.add(service);
            }
        }
        return service;
    }

    //==============================================================================================
    // Register controller
    //==============================================================================================

    protected <T extends IController> ControllerRegistration<T> controller(T controller) {
        return register(new ControllerRegistration<>(controller));
    }

    protected <T extends IController> ControllerRegistration<T> controller(Class<T> implClass) {
        return register(new ControllerRegistration<>(implClass));
    }

    protected <T extends IController> ControllerRegistration<T> controller(Class<T> apiClass,
                                                                           T controller) {
        return register(new ControllerRegistration<>(apiClass, controller));
    }

    protected <T extends IController> ControllerRegistration<T> controller(Class<T> apiClass,
                                                                           Class<? extends T> implClass) {
        return register(new ControllerRegistration<>(apiClass, implClass));
    }

    protected <T extends IController> ControllerRegistration<T> controller(Class<T> apiClass,
                                                                           IProvider<T> provider) {
        return register(new ControllerRegistration<>(apiClass, provider));
    }

    //==============================================================================================
    // Register service
    //==============================================================================================

    protected <T extends IService> ServiceRegistration<T> service(T service) {
        return register(new ServiceRegistration<>(service));
    }

    protected <T extends IService> ServiceRegistration<T> service(Class<T> serviceClass) {
        return register(new ServiceRegistration<>(serviceClass));
    }

    protected <T extends IService> ServiceRegistration<T> service(Class<T> serviceClass,
                                                                  IProvider<T> provider) {
        return register(new ServiceRegistration<>(serviceClass, provider));
    }
}
