package com.honeycomb.hexagonx.core;

import com.honeycomb.provider.IProvider;

import java.util.HashMap;
import java.util.Map;

public class HexagonComponentManager<COMP> {
    private final Map<String, HexagonComponent<? extends COMP>> mComponents = new HashMap<>();

    private final Map<Class<? extends COMP>, String> mComponentNames = new HashMap<>();

    public <T extends COMP> void register(Class<T> componentClass, HexagonComponent<T> component) {
        if (componentClass == null || component == null) {
            throw new NullPointerException("Invalid registration.");
        }

        String name = getDefaultName(componentClass);

        // Register the component with the name.
        register(name, component);

        // Register the component class as the alias of the name.
        alias(componentClass, name);
    }

    public void register(String name, HexagonComponent<? extends COMP> component) {
        if (name == null || component == null) {
            throw new NullPointerException("Invalid registration.");
        }

        mComponents.put(name, component);
    }

    public void alias(Class<? extends COMP> aliasClass, Class<? extends COMP> componentClass) {
        if (aliasClass == null || componentClass == null) {
            throw new NullPointerException("Invalid alias.");
        }

        if (!aliasClass.isAssignableFrom(componentClass)) {
            throw new IllegalArgumentException("Component class: " + componentClass
                    + " cannot assign to alias class: " + aliasClass);
        }

        String name = getDefaultName(componentClass);

        alias(aliasClass, name);
    }

    public void alias(Class<? extends COMP> aliasClass, String name) {
        if (aliasClass == null) {
            throw new NullPointerException("Invalid alias.");
        }

        // TODO: Check alias type?

        mComponentNames.put(aliasClass, name);
    }

    public void enable(Class<? extends COMP> componentClass) throws HexagonAvailabilityException {
        String name = mComponentNames.get(componentClass);
        if (name == null) {
            throw new HexagonNotRegisteredException(componentClass.getName());
        }

        enable(name);
    }

    public void enable(String name) throws HexagonAvailabilityException {
        HexagonComponent<? extends COMP> component = mComponents.get(name);
        if (component == null) {
            throw new HexagonNotRegisteredException(name);
        }

        component.setting.enable();
    }

    public void disable(Class<? extends COMP> componentClass) throws HexagonAvailabilityException {
        String name = mComponentNames.get(componentClass);
        if (name == null) {
            throw new HexagonNotRegisteredException(componentClass.getName());
        }

        disable(name);
    }

    public void disable(String name) throws HexagonNotRegisteredException {
        HexagonComponent<? extends COMP> component = mComponents.get(name);
        if (component == null) {
            throw new HexagonNotRegisteredException(name);
        }

        component.setting.disable();
    }

    public <T extends COMP> T get(Class<T> componentClass) throws HexagonAvailabilityException {
        if (componentClass == null) {
            throw new NullPointerException("No component class specified.");
        }

        // Find name with alias class if registered.
        String name = mComponentNames.get(componentClass);
        if (name == null) {
            throw new HexagonNotRegisteredException(getDefaultName(componentClass));
        }

        return get(name);
    }

    public <T extends COMP> T get(String name) throws HexagonAvailabilityException {
        if (name == null) {
            throw new NullPointerException("No component name specified.");
        }

        HexagonComponent<? extends COMP> component = mComponents.get(name);
        if (component == null) {
            throw new HexagonNotRegisteredException(name);
        }

        // Check component available.
        if (!component.setting.isAvailable()) {
            throw new HexagonAvailabilityException(name, "Setting not available.");
        }

        // Provides component instance.
        COMP comp;
        try {
            comp = component.provider.get();
            if (comp == null) {
                throw new NullPointerException("Provides null instance.");
            }
        } catch (Exception e) {
            throw new HexagonAvailabilityException(name, "Failed to provide instance.", e);
        }

        // Convert to target class.
        try {
            @SuppressWarnings("unchecked")
            T instance = (T) comp;
            return instance;
        } catch (ClassCastException e) {
            throw new HexagonAvailabilityException(name, e.getMessage(), e);
        }
    }

    private String getDefaultName(Class<? extends COMP> componentClass) {
        return componentClass.getName();
    }

    private static class HexagonComponent<T> {
        final HexagonComponentSetting setting;
        final IProvider<? extends T> provider;

        public HexagonComponent(HexagonComponentSetting setting, IProvider<? extends T> provider) {
            this.setting = setting;
            this.provider = provider;
        }
    }
}
