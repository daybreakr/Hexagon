package com.honeycomb.hexagon.core;

import com.honeycomb.basement.provider.IProvider;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceRegistry {
    // Contains all registered service info indexing by service name.
    private final Map<String, ServiceInfo> mServices = new HashMap<>();

    // Contains all registered service providers indexing by service name.
    private final Map<String, IProvider<? extends IService>> mProviders = new HashMap<>();

    private final Map<String, Set<String>> mCategories = new HashMap<>();

    // Added to default category if no category specified when registered.
    public static final String CATEGORY_DEFAULT = "default";

    public void register(ServiceInfo serviceInfo, IProvider<? extends IService> provider) {
        final String name = serviceInfo.name();

        // Make a copy of the original service info.
        serviceInfo = new ServiceInfo(serviceInfo);

        // Register service info.
        mServices.put(name, serviceInfo);

        // Register service instance provider.
        mProviders.put(name, provider);

        // Register service categories.
        // set to default category if not specified any
        if (serviceInfo.categories().isEmpty()) {
            serviceInfo.categorise(CATEGORY_DEFAULT);
        }
        for (String category : serviceInfo.categories()) {
            registerCategory(name, category);
        }
    }

    public void unregister(String name) {
        ServiceInfo service = getServiceInfo(name);
        if (service != null) {
            mServices.remove(name);
            mProviders.remove(name);
            for (String category : service.categories()) {
                Set<String> set = mCategories.get(category);
                if (set != null) {
                    set.remove(name);
                }
                if (set == null || set.isEmpty()) {
                    mCategories.remove(category);
                }
            }
        }
    }

    public boolean isRegistered(String name) {
        return mServices.containsKey(name);
    }

    public ServiceInfo getServiceInfo(String name) {
        ServiceInfo service = mServices.get(name);
        if (service != null) {
            return new ServiceInfo(service);
        }
        return null;
    }

    public List<ServiceInfo> getRegisteredServices() {
        List<ServiceInfo> services = new LinkedList<>();
        for (ServiceInfo service : mServices.values()) {
            services.add(new ServiceInfo(service));
        }
        return services;
    }

    public List<ServiceInfo> getRegisteredServices(String category) {
        List<ServiceInfo> services = new LinkedList<>();
        Set<String> names = mCategories.get(category);
        if (names != null) {
            for (String name : names) {
                ServiceInfo service = getServiceInfo(name);
                if (service != null) {
                    services.add(service);
                }
            }
        }
        return services;
    }

    public <T extends IService> T getService(Class<T> serviceClass) {
        String name = serviceClass.getName();
        return getService(name);
    }

    public <T extends IService> T getService(String name) {
        IService s = provideService(name);
        if (s != null) {
            try {
                @SuppressWarnings("unchecked")
                T service = (T) s;
                return service;
            } catch (ClassCastException ignored) {
            }
        }
        return null;
    }

    public List<IService> getServices() {
        List<IService> services = new LinkedList<>();
        for (String name : mServices.keySet()) {
            IService service = getService(name);
            if (service != null) {
                services.add(service);
            }
        }
        return services;
    }

    public List<IService> getServices(String category) {
        List<IService> services = new LinkedList<>();
        Set<String> names = mCategories.get(category);
        if (names != null) {
            for (String name : names) {
                IService service = getService(name);
                if (service != null) {
                    services.add(service);
                }
            }
        }
        return services;
    }

    public List<String> getRegisteredCategories() {
        return new LinkedList<>(mCategories.keySet());
    }

    private void registerCategory(String name, String category) {
        Set<String> services = mCategories.get(category);
        if (services == null) {
            services = new LinkedHashSet<>();
            mCategories.put(category, services);
        }
        services.add(name);
    }

    private IService provideService(String name) {
        ServiceInfo service = getServiceInfo(name);
        if (service != null && service.enabled()) {
            IProvider<? extends IService> provider = mProviders.get(name);
            if (provider != null) {
                return provider.get();
            }
        }
        return null;
    }
}
