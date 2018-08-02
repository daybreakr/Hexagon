package com.honeycomb.hexagonx.core;

import com.honeycomb.provider.IProvider;

import java.util.HashMap;
import java.util.Map;

public class HexagonServiceRegistry {
    // Both service class and alias class were pointed to the same name,
    // thus we can provide service with alias.
    private final Map<Class<? extends HexagonService>, String> mServiceNames = new HashMap<>();

    private final Map<String, IProvider<? extends HexagonService>> mServiceProviders =
            new HashMap<>();

    public <T extends HexagonService> void registerService(Class<T> serviceClass,
                                                           IProvider<T> provider) {
        if (serviceClass == null || provider == null) {
            throw new NullPointerException("Invalid registration.");
        }

        String serviceName = getServiceName(serviceClass);

        // Register the service provider with the service name.
        registerService(serviceName, provider);

        // Register the service class as the alias of the service name.
        alias(serviceClass, serviceName);
    }

    public <T extends HexagonService> void registerService(String serviceName,
                                                           IProvider<T> provider) {
        if (serviceName == null || provider == null) {
            throw new NullPointerException("Invalid registration.");
        }

        // Register the service provider with the service name.
        mServiceProviders.put(serviceName, provider);
    }

    public void alias(Class<? extends HexagonService> aliasClass,
                      Class<? extends HexagonService> serviceClass) {
        if (aliasClass == null || serviceClass == null) {
            throw new NullPointerException("Invalid alias.");
        }
        if (!aliasClass.isAssignableFrom(serviceClass)) {
            throw new IllegalArgumentException("Service class: " + serviceClass
                    + " cannot assign to alias class: " + aliasClass);
        }

        String serviceName = getServiceName(serviceClass);

        // Register the alias class as the alias of the service name.
        alias(aliasClass, serviceName);
    }

    public void alias(Class<? extends HexagonService> aliasClass, String serviceName) {
        if (aliasClass == null) {
            throw new NullPointerException("Invalid alias.");
        }

        // Register the alias class as the alias of the service name.
        mServiceNames.put(aliasClass, serviceName);
    }

    public <T extends HexagonService> T getService(Class<T> serviceClass)
            throws HexagonAvailabilityException {
        if (serviceClass == null) {
            throw new NullPointerException("No service class specified.");
        }

        String serviceName = mServiceNames.get(serviceClass);
        if (serviceName == null) {
            throw  new HexagonNotRegisteredException(getServiceName(serviceClass));
        }

        return getService(serviceName);
    }

    public <T extends HexagonService> T getService(String serviceName)
            throws HexagonAvailabilityException {
        if (serviceName == null) {
            throw new NullPointerException("No service name specified.");
        }

        // Find registered provider.
        IProvider<? extends HexagonService> provider = mServiceProviders.get(serviceName);
        if (provider == null) {
            throw  new HexagonNotRegisteredException(serviceName);
        }

        // Provide service instance.
        HexagonService instance;
        try {
            instance = provider.get();
            if (instance == null) {
                throw new RuntimeException("Provides null instance.");
            }
        } catch (Exception e) {
            throw new HexagonAvailabilityException(serviceName, "Failed to provide instance.", e);
        }

        // Convert to target class.
        try {
            @SuppressWarnings("unchecked")
            T service = (T) instance;
            return service;
        } catch (ClassCastException e) {
            throw new HexagonAvailabilityException(serviceName, e.getMessage(), e);
        }
    }

    private String getServiceName(Class<? extends HexagonService> serviceClass) {
        return serviceClass.getName();
    }
}
