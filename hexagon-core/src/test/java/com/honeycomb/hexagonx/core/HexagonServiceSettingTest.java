package com.honeycomb.hexagonx.core;

import org.junit.Before;
import org.junit.Test;

public class HexagonServiceSettingTest {
    private HexagonServiceSetting mServiceA;
    private HexagonServiceSetting mServiceB;
    private HexagonServiceSetting mServiceC;

    @Before
    public void setUp() {
        mServiceA = new HexagonServiceSetting("A");
        mServiceB = new HexagonServiceSetting("B");
        mServiceC = new HexagonServiceSetting("C");
    }

    @Test
    public void testDependsOn() {
        mServiceA.dependsOn(mServiceB);

        assert mServiceA.dependencies().size() == 1;

        assert mServiceB.dependedOn().size() == 1;

        assert mServiceA.dependencies().get(0) == mServiceB;
        assert mServiceB.dependedOn().get(0) == mServiceA;
    }

    @Test
    public void testCondition() {
        mServiceA.condition(new FixedCondition(true));

        assert mServiceA.conditions().size() == 1;

        assert mServiceA.conditions().get(0).isSatisfied();
    }

    @Test
    public void testIsAvailable() throws HexagonAvailabilityException {
        mServiceA.dependsOn(mServiceB);
        mServiceB.dependsOn(mServiceC);

        mServiceA.enable();
        mServiceB.enable();
        mServiceC.enable();
        assert mServiceA.isAvailable();
        assert mServiceB.isAvailable();
        assert mServiceC.isAvailable();

        mServiceA.enable();
        mServiceB.disable();
        mServiceC.enable();
        assert !mServiceA.isAvailable();
        assert !mServiceB.isAvailable();
        assert mServiceC.isAvailable();

        mServiceA.enable();
        mServiceB.enable();
        mServiceC.disable();
        assert !mServiceA.isAvailable();
        assert !mServiceB.isAvailable();
        assert !mServiceC.isAvailable();
    }
}
