package com.honeycomb.hexagonx.core;

import com.honeycomb.provider.IProvider;
import com.honeycomb.provider.InstanceProvider;
import com.honeycomb.provider.ReflectiveProvider;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.fail;

public class HexagonServiceRegistryTest {
    private HexagonServiceRegistry mRegistry;

    @Before
    public void setUp() {
        mRegistry = new HexagonServiceRegistry();
    }

    @Test
    public void testRegisterService_withClass() throws HexagonAvailabilityException {
        TestServiceImpl impl = new TestServiceImpl();

        mRegistry.registerService(TestService.class, provider(impl));

        TestService testService = mRegistry.getService(TestService.class);

        assert testService != null;
        assert testService == impl;
    }

    @Test
    public void testRegisterService_withArbitraryName() throws HexagonAvailabilityException {
        final String serviceName = "test";
        TestServiceImpl impl = new TestServiceImpl();

        mRegistry.registerService(serviceName, provider(impl));

        TestService testService = mRegistry.getService(serviceName);

        assert testService != null;
        assert testService == impl;
    }

    public void testAlias_registerWithImplAndObtainWithInterface() throws HexagonAvailabilityException {
        TestServiceImpl impl = new TestServiceImpl();

        mRegistry.registerService(TestServiceImpl.class, implProvider(impl));

        try {
            TestService testService = mRegistry.getService(TestService.class);
            fail();
        } catch (HexagonAvailabilityException expected) {
        }
    }

    private static IProvider<TestService> provider(TestService impl) {
        return new InstanceProvider<>(impl);
    }

    private static IProvider<? extends TestService> provider(Class<? extends TestService> clazz) {
        return new ReflectiveProvider<>(clazz);
    }

    private static IProvider<TestServiceImpl> implProvider(TestServiceImpl impl) {
        return new InstanceProvider<>(impl);
    }

    private interface TestService extends HexagonService {
    }

    private static class TestServiceImpl implements TestService {
    }
}
