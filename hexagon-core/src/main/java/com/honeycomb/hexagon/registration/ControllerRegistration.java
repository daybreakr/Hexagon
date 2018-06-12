package com.honeycomb.hexagon.registration;

import com.honeycomb.basement.provider.IProvider;
import com.honeycomb.basement.provider.InstanceProvider;
import com.honeycomb.basement.provider.ReflectiveProvider;
import com.honeycomb.basement.provider.SingletonProviderWrapper;
import com.honeycomb.hexagon.core.controller.IController;

public class ControllerRegistration<T extends IController> {
    private final Class<T> mApiClass;
    private IProvider<T> mProvider;
    private boolean mEnabled = true;

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

    public boolean enabled() {
        return mEnabled;
    }

    //==============================================================================================
    // Setters
    //==============================================================================================

    public ControllerRegistration<T> provider(IProvider<T> provider) {
        mProvider = provider;
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

    public ControllerRegistration<T> impl(T impl) {
        mImpl = impl;
        return this;
    }

    public ControllerRegistration<T> implClass(Class<? extends T> implClass) {
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
