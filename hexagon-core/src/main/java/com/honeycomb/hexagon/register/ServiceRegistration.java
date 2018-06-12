package com.honeycomb.hexagon.register;

import com.honeycomb.basement.provider.IProvider;
import com.honeycomb.basement.provider.InstanceProvider;
import com.honeycomb.basement.provider.ReflectiveProvider;
import com.honeycomb.basement.provider.SingletonProviderWrapper;
import com.honeycomb.hexagon.core.IService;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServiceRegistration<T extends IService> {
    private final Class<T> mServiceClass;
    private IProvider<T> mProvider;
    private boolean mEnabled = true;
    private Set<String> mCategories;

    private T mInstance;

    public ServiceRegistration(T service) {
        if (service == null) {
            throw new NullPointerException("Service object is null");
        }
        @SuppressWarnings("unchecked")
        Class<T> serviceClass = (Class<T>) service.getClass();
        mServiceClass = serviceClass;
        mInstance = service;
    }

    public ServiceRegistration(Class<T> serviceClass) {
        this(serviceClass, null);
    }

    public ServiceRegistration(Class<T> serviceClass, IProvider<T> provider) {
        if (serviceClass == null) {
            throw new NullPointerException("Service class is null");
        }
        mServiceClass = serviceClass;
        mProvider = provider;
    }

    //==============================================================================================
    // Getters
    //==============================================================================================

    public Class<T> serviceClass() {
        return mServiceClass;
    }

    public IProvider<T> provider() {
        // Infer the provider if not specified.
        if (mProvider == null) {
            if (mInstance != null) {
                mProvider = new InstanceProvider<>(mInstance);
            } else if (mServiceClass != null && !mServiceClass.isInterface()) {
                mProvider = new ReflectiveProvider<>(mServiceClass);
            } else {
                throw new IllegalArgumentException("Cannot infer provider for " + mServiceClass);
            }
        }

        // Wrap a singleton provider over the original provider to make the service a singleton.
        if (!mProvider.isSingleton()) {
            mProvider = new SingletonProviderWrapper<>(mProvider);
        }
        return mProvider;
    }

    public boolean enabled() {
        return mEnabled;
    }

    public Set<String> categories() {
        if (mCategories != null) {
            return Collections.unmodifiableSet(mCategories);
        }
        return Collections.emptySet();
    }

    //==============================================================================================
    // Setters
    //==============================================================================================

    public ServiceRegistration<T> provider(IProvider<T> provider) {
        mProvider = provider;
        return this;
    }

    public ServiceRegistration<T> enable() {
        mEnabled = true;
        return this;
    }

    public ServiceRegistration<T> disable() {
        mEnabled = false;
        return this;
    }

    public ServiceRegistration<T> categories(String... categories) {
        if (categories != null && categories.length > 0) {
            if (mCategories == null) {
                mCategories = new LinkedHashSet<>();
            }
            Collections.addAll(mCategories, categories);
        }
        return this;
    }

    public ServiceRegistration<T> service(T service) {
        mInstance = service;
        return this;
    }
}
