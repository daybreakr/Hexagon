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
        alias(serviceClass, serviceName);
        registerService(serviceName, provider);
    }

    public <T extends HexagonService> void registerService(String serviceName,
                                                           IProvider<T> provider) {
        if (serviceName == null || provider == null) {
            throw new NullPointerException("Invalid registration.");
        }

        mServiceProviders.put(serviceName, provider);
    }

    public void alias(Class<? extends HexagonService> aliasClass,
                      Class<? extends HexagonService> serviceClass) {
        if (aliasClass == null || serviceClass == null) {
            throw new NullPointerException("Invalid alias");
        }
        if (!aliasClass.isAssignableFrom(serviceClass)) {
            throw new IllegalArgumentException("Alias class " + aliasClass
                    + " is not the super class of " + serviceClass);
        }

        String serviceName = getServiceName(serviceClass);
        alias(aliasClass, serviceName);
    }

    public void alias(Class<? extends HexagonService> aliasClass, String serviceName) {
        if (aliasClass == null) {
            throw new NullPointerException("Invalid alias");
        }

        mServiceNames.put(aliasClass, serviceName);
    }

    public <T extends HexagonService> T getService(Class<T> serviceClass)
            throws HexagonAvailabilityException {
        if (serviceClass == null) {
            throw new NullPointerException("No service class specified.");
        }

        String serviceName = mServiceNames.get(serviceClass);
        if (serviceName == null) {
            throw new HexagonAvailabilityException(serviceClass.getName(), false,
                    "Alias not registered.");
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
            throw new HexagonAvailabilityException(serviceName, false, "Provider not registered.");
        }

        // Provide service instance.
        HexagonService instance;
        try {
            instance = provider.get();
            if (instance == null) {
                throw new RuntimeException("Service instance is null.");
            }
        } catch (Exception e) {
            throw new HexagonAvailabilityException(serviceName, true,
                    "Failed to provide instance.", e);
        }

        // Convert to target class.
        try {
            @SuppressWarnings("unchecked")
            T service = (T) instance;
            return service;
        } catch (ClassCastException e) {
            throw new HexagonAvailabilityException(serviceName, true, e.getMessage(), e);
        }
    }

    private String getServiceName(Class<? extends HexagonService> serviceClass) {
        return serviceClass.getName();
    }
}
