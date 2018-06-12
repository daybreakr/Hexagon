package com.honeycomb.hexagon.core.controller;

import com.honeycomb.basement.provider.IProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ControllerRegistry {
    // Contains all registered controller info indexing by controller name.
    private final Map<String, ControllerInfo> mControllers = new HashMap<>();

    // Contains all registered controller providers indexing by controller name.
    private final Map<String, IProvider<? extends IController>> mProviders = new HashMap<>();

    public void register(ControllerInfo controllerInfo, IProvider<? extends IController> provider) {
        // Make a copy of the original controller info.
        controllerInfo = new ControllerInfo(controllerInfo);

        // Register controller info.
        mControllers.put(controllerInfo.name, controllerInfo);

        // Register controller instance provider.
        mProviders.put(controllerInfo.name, provider);
    }

    public void unregister(String name) {
        mControllers.remove(name);
        mProviders.remove(name);
    }

    public boolean isRegistered(String name) {
        return mControllers.containsKey(name);
    }

    public ControllerInfo getControllerInfo(String name) {
        ControllerInfo controller = mControllers.get(name);
        if (controller != null) {
            return new ControllerInfo(controller);
        }
        return null;
    }

    public List<ControllerInfo> getRegisteredControllers() {
        List<ControllerInfo> controllers = new LinkedList<>();
        for (ControllerInfo controller : mControllers.values()) {
            controllers.add(new ControllerInfo(controller));
        }
        return controllers;
    }

    public <T extends IController> T getController(Class<T> controllerClass) {
        String name = controllerClass.getName();
        return getController(name);
    }

    public <T extends IController> T getController(String name) {
        IController c = provideController(name);
        if (c != null) {
            try {
                @SuppressWarnings("unchecked")
                T controller = (T) c;
                return controller;
            } catch (ClassCastException ignored) {
            }
        }
        return null;
    }

    private IController provideController(String name) {
        if (isEnabled(name)) {
            IProvider<? extends IController> provider = mProviders.get(name);
            if (provider != null) {
                return provider.get();
            }
        }
        return null;
    }

    private boolean isEnabled(String name) {
        ControllerInfo controller = getControllerInfo(name);
        return controller != null && controller.enabled;
    }
}
