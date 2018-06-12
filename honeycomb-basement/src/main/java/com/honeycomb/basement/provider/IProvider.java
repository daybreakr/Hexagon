package com.honeycomb.basement.provider;

public interface IProvider<T> {

    T get();

    boolean isSingleton();
}
