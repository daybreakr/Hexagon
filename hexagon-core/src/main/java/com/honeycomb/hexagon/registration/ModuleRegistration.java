package com.honeycomb.hexagon.registration;

import com.honeycomb.basement.provider.IProvider;
import com.honeycomb.hexagon.core.controller.IController;
import com.honeycomb.hexagon.core.service.IService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ModuleRegistration {
    private String mName;
    private boolean mEnabled = true;
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
        if (mName != null) {
            return mName;
        }
        return getClass().getName();
    }

    public boolean enabled() {
        return mEnabled;
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

    protected ModuleRegistration name(String name) {
        mName = name;
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

    //==============================================================================================
    // Register controller
    //==============================================================================================

    protected <T extends IController> ControllerRegistration<T> controller(T controller) {
        return controller(new ControllerRegistration<>(controller));
    }

    protected <T extends IController> ControllerRegistration<T> controller(Class<T> implClass) {
        return controller(new ControllerRegistration<>(implClass));
    }

    protected <T extends IController> ControllerRegistration<T> controller(Class<T> apiClass,
                                                                           T controller) {
        return controller(new ControllerRegistration<>(apiClass, controller));
    }

    protected <T extends IController> ControllerRegistration<T> controller(Class<T> apiClass,
                                                                           Class<? extends T> implClass) {
        return controller(new ControllerRegistration<>(apiClass, implClass));
    }

    protected <T extends IController> ControllerRegistration<T> controller(Class<T> apiClass,
                                                                           IProvider<T> provider) {
        return controller(new ControllerRegistration<>(apiClass, provider));
    }

    protected <T extends IController> ControllerRegistration<T> controller(ControllerRegistration<T> controller) {
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

    //==============================================================================================
    // Register service
    //==============================================================================================

    protected <T extends IService> ServiceRegistration<T> service(T service) {
        return service(new ServiceRegistration<>(service));
    }

    protected <T extends IService> ServiceRegistration<T> service(Class<T> serviceClass) {
        return service(new ServiceRegistration<>(serviceClass));
    }

    protected <T extends IService> ServiceRegistration<T> service(Class<T> serviceClass,
                                                                  IProvider<T> provider) {
        return service(new ServiceRegistration<>(serviceClass, provider));
    }

    protected <T extends IService> ServiceRegistration<T> service(ServiceRegistration<T> service) {
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
}
