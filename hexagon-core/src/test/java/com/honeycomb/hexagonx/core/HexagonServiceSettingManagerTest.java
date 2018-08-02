package com.honeycomb.hexagonx.core;

import org.junit.Before;
import org.junit.Test;

public class HexagonServiceSettingManagerTest {
    private HexagonServiceSettingManager mManager;

    private HexagonServiceSetting mServiceA;
    private HexagonServiceSetting mServiceB;
    private HexagonServiceSetting mServiceC;
    private HexagonServiceSetting mServiceD;
    private HexagonServiceSetting mServiceE;

    private HexagonServiceSetting mServiceX;
    private HexagonServiceSetting mServiceY;
    private HexagonServiceSetting mServiceZ;

    @Before
    public void setUp() {
        mManager = new HexagonServiceSettingManager();

        mServiceA = serviceSetting("A").condition(always(true));
        mServiceB = serviceSetting("B").condition(always(true));
        mServiceC = serviceSetting("C").condition(always(false));
        mServiceD = serviceSetting("D").condition(always(true));
        mServiceE = serviceSetting("E").condition(always(true));

        mServiceX = serviceSetting("X").condition(always(true));
        mServiceY = serviceSetting("Y").condition(always(true));
        mServiceZ = serviceSetting("Z").condition(always(true));

        mServiceD.dependsOn(mServiceA);
        mServiceD.dependsOn(mServiceE);
        mServiceA.dependsOn(mServiceC);
        mServiceE.dependsOn(mServiceC);
        mServiceE.dependsOn(mServiceB);

        mServiceX.dependsOn(mServiceY);
        mServiceY.dependsOn(mServiceZ);
    }

    @Test
    public void testEnable_all() {
        enableAll();

        assert !mServiceA.isAvailable();
        assert mServiceB.isAvailable();
        assert !mServiceC.isAvailable();
        assert !mServiceD.isAvailable();
        assert !mServiceE.isAvailable();

        assert mServiceX.isAvailable();
        assert mServiceY.isAvailable();
        assert mServiceZ.isAvailable();
    }

    @Test
    public void testDisable() throws HexagonAvailabilityException {
        enableAll();

        mManager.disable(mServiceZ.name);

        assert !mServiceX.isAvailable();
        assert !mServiceY.isAvailable();
        assert !mServiceZ.isAvailable();
    }

    @Test
    public void testReEnable() throws HexagonAvailabilityException {
        enableAll();

        mManager.disable(mServiceZ.name);
        mManager.enable(mServiceZ.name);

        assert mServiceX.isAvailable();
        assert mServiceY.isAvailable();
        assert mServiceZ.isAvailable();
    }

    private void enableAll() {
        for (HexagonServiceSetting service : mManager.getAll()) {
            try {
                mManager.enable(service.name);
            } catch (HexagonAvailabilityException ignored) {
            }
        }
    }

    private HexagonServiceSetting serviceSetting(String name) {
        HexagonServiceSetting setting = new HexagonServiceSetting(name);
        mManager.add(setting);
        return setting;
    }

    private static Condition always(boolean satisfied) {
        return new FixedCondition(satisfied);
    }
}
