package com.honeycomb.hexagon.register;

import com.honeycomb.basement.condition.ICondition;
import com.honeycomb.basement.provider.IProvider;
import com.honeycomb.basement.provider.InstanceProvider;
import com.honeycomb.basement.provider.ReflectiveProvider;
import com.honeycomb.basement.provider.SingletonProviderWrapper;
import com.honeycomb.hexagon.core.IService;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ServiceRegistration<T extends IService> {
    private final Class<T> mServiceClass;
    private String mLabel;
    private boolean mEnabled = true;
    private List<ICondition> mPrerequisites;
    private List<String> mDependencies;

    private Set<String> mCategories;

    private IProvider<T> mProvider;

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

    public String label() {
        return mLabel;
    }

    public boolean enabled() {
        return mEnabled;
    }

    public List<ICondition> prerequisites() {
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

    public Set<String> categories() {
        if (mCategories != null) {
            return Collections.unmodifiableSet(mCategories);
        }
        return Collections.emptySet();
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

    //==============================================================================================
    // Setters
    //==============================================================================================

    protected ServiceRegistration<T> label(String label) {
        mLabel = label;
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

    public ServiceRegistration<T> require(ICondition condition) {
        if (condition != null) {
            if (mPrerequisites == null) {
                mPrerequisites = new LinkedList<>();
            }
            if (!mPrerequisites.contains(condition)) {
                mPrerequisites.add(condition);
            }
        }
        return this;
    }

    protected ServiceRegistration<T> dependsOn(Class<? extends ModuleRegistration> moduleClass) {
        final String moduleName = moduleClass.getName();
        return dependsOn(moduleName);
    }

    public ServiceRegistration<T> dependsOn(String moduleName) {
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

    public ServiceRegistration<T> categorise(String... categories) {
        if (categories != null && categories.length > 0) {
            if (mCategories == null) {
                mCategories = new LinkedHashSet<>();
            }
            Collections.addAll(mCategories, categories);
        }
        return this;
    }

    public ServiceRegistration<T> provide(IProvider<T> provider) {
        mProvider = provider;
        return this;
    }

    public ServiceRegistration<T> provide(T service) {
        mInstance = service;
        return this;
    }
}
