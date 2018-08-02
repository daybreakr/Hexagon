package com.honeycomb.hexagonx.core;

import org.junit.Test;

public class HexagonServiceSettingTest {

    @Test
    public void testDependsOn() {
        HexagonServiceSetting serviceA = new HexagonServiceSetting("service-A");
        HexagonServiceSetting serviceB = new HexagonServiceSetting("service-B");

        serviceA.dependsOn(serviceB);

        assert serviceA.dependencies != null;
        assert serviceA.dependencies.size() == 1;

        assert serviceB.dependedOn != null;
        assert serviceB.dependedOn.size() == 1;

        assert serviceA.dependencies.get(0) == serviceB;
        assert serviceB.dependedOn.get(0) == serviceA;
    }
}
