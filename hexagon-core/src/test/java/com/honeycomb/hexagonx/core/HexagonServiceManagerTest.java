package com.honeycomb.hexagonx.core;

import com.honeycomb.provider.IProvider;
import com.honeycomb.provider.InstanceProvider;
import com.honeycomb.provider.ReflectiveProvider;

import org.junit.Before;
import org.junit.Test;

public class HexagonServiceManagerTest {
    private HexagonComponentManager<HexagonService> mManager;

    @Before
    public void setUp() {
        mManager = new HexagonComponentManager<>();
    }

    @Test
    public void testRegister() {
        TestServiceImpl impl = new TestServiceImpl();
    }

    private static <T> IProvider<T> provider(T instance) {
        return new InstanceProvider<>(instance);
    }

    private static <T> IProvider<T> provider(Class<T> componentClass) {
        return new ReflectiveProvider<>(componentClass);
    }

    private interface TestServiceInterface extends HexagonService {
    }

    private class TestServiceImpl implements TestServiceInterface {
    }
}
