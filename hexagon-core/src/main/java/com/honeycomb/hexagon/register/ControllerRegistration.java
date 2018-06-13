package com.honeycomb.hexagon.register;

import com.honeycomb.basement.condition.ICondition;
import com.honeycomb.basement.provider.IProvider;
import com.honeycomb.basement.provider.InstanceProvider;
import com.honeycomb.basement.provider.ReflectiveProvider;
import com.honeycomb.basement.provider.SingletonProviderWrapper;
import com.honeycomb.hexagon.core.IController;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ControllerRegistration<T extends IController> {
    private final Class<T> mApiClass;
    private String mLabel;
    private boolean mEnabled = true;
    private List<ICondition> mPrerequisites;
    private List<String> mDependencies;

    private IProvider<T> mProvider;

    private T mImpl;
    private Class<? extends T> mImplClass;
    private boolean mSingleton = true;

    public ControllerRegistration(T impl) {
        if (impl == null) {
            throw new NullPointerException("Impl object is null");
        }
        @SuppressWarnings("unchecked")
        Class<T> implClass = (Class<T>) impl.getClass();
        mApiClass = implClass;
        mImpl = impl;
    }

    public ControllerRegistration(Class<T> apiClass, T impl) {
        if (apiClass == null) {
            throw new NullPointerException("API class is null");
        }
        mApiClass = apiClass;
        mImpl = impl;
    }

    public ControllerRegistration(Class<T> implClass) {
        this(implClass, implClass);
    }

    public ControllerRegistration(Class<T> apiClass, Class<? extends T> implClass) {
        if (apiClass == null) {
            throw new NullPointerException("API class is null");
        }
        mApiClass = apiClass;
        mImplClass = implClass;
    }

    public ControllerRegistration(Class<T> apiClass, IProvider<T> provider) {
        if (apiClass == null) {
            throw new NullPointerException("API class is null");
        }
        mApiClass = apiClass;
        mProvider = provider;
    }

    //==============================================================================================
    // Getters
    //==============================================================================================

    public Class<T> apiClass() {
        return mApiClass;
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

    public IProvider<T> provider() {
        // Infer the provider if not specified.
        if (mProvider == null) {
            if (mImpl != null) {
                mProvider = new InstanceProvider<>(mImpl);
            } else if (mImplClass != null) {
                @SuppressWarnings("unchecked")
                IProvider<T> provider = (IProvider<T>) new ReflectiveProvider<>(mImplClass);
                mProvider = provider;
            } else {
                throw new IllegalArgumentException("Cannot infer provider for " + mApiClass);
            }
        }

        // Wrap a singleton provider over the original provider to make the controller a singleton.
        IProvider<T> provider = mProvider;
        if (mSingleton && !provider.isSingleton()) {
            provider = new SingletonProviderWrapper<>(provider);
        }
        return provider;
    }

    //==============================================================================================
    // Setters
    //==============================================================================================

    protected ControllerRegistration<T> label(String label) {
        mLabel = label;
        return this;
    }

    public ControllerRegistration<T> enable() {
        mEnabled = true;
        return this;
    }

    public ControllerRegistration<T> disable() {
        mEnabled = false;
        return this;
    }

    public ControllerRegistration<T> require(ICondition condition) {
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

    protected ControllerRegistration<T> dependsOn(Class<? extends ModuleRegistration> moduleClass) {
        final String moduleName = moduleClass.getName();
        return dependsOn(moduleName);
    }

    public ControllerRegistration<T> dependsOn(String moduleName) {
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

    public ControllerRegistration<T> provide(IProvider<T> provider) {
        mProvider = provider;
        return this;
    }

    public ControllerRegistration<T> provide(T impl) {
        mImpl = impl;
        return this;
    }

    public ControllerRegistration<T> provide(Class<? extends T> implClass) {
        mImplClass = implClass;
        return this;
    }

    public ControllerRegistration<T> singleton() {
        mSingleton = true;
        return this;
    }

    public ControllerRegistration<T> nonsingleton() {
        mSingleton = false;
        return this;
    }
}
