package com.honeycomb.hexagonx.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HexagonServiceSettingManagerTest {
    private HexagonServiceSettingManager mManager;
    private List<HexagonServiceSetting> mTestServices;

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
        mTestServices = new ArrayList<>();

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
    public void testGetBootList() {
        List<HexagonServiceSetting> bootList = mManager.getBootList();
        assert bootList.size() == mTestServices.size();

        for (HexagonServiceSetting service : bootList) {
            int serviceIndex = bootList.indexOf(service);
            assert serviceIndex >= 0;

            if (service.dependencies != null) {
                for (HexagonServiceSetting dependency : service.dependencies) {
                    int dependencyIndex = bootList.indexOf(dependency);
                    assert dependencyIndex >= 0;

                    assert dependencyIndex < serviceIndex;
                }
            }
        }
    }

    @Test
    public void testActivate_all() {
        activateAll();

        for (HexagonServiceSetting setting : mTestServices) {
            assert setting.active;
        }

        assert !mServiceA.enabled;
        assert mServiceB.enabled;
        assert !mServiceC.enabled;
        assert !mServiceD.enabled;
        assert !mServiceE.enabled;

        assert mServiceX.enabled;
        assert mServiceY.enabled;
        assert mServiceZ.enabled;
    }

    @Test
    public void testDeactivate() throws HexagonAvailabilityException {
        activateAll();

        mManager.deactivate(mServiceZ.name);

        assert mServiceZ.enabled;
        assert !mServiceZ.active;

        assert !mServiceY.enabled;
        assert mServiceY.active;

        assert !mServiceX.enabled;
        assert mServiceX.active;
    }

    @Test
    public void testReactivate() throws HexagonAvailabilityException {
        activateAll();

        mManager.deactivate(mServiceZ.name);
        mManager.activate(mServiceZ.name);

        assert mServiceX.isAvailable();
        assert mServiceY.isAvailable();
        assert mServiceZ.isAvailable();
    }

    private void activateAll() {
        for (HexagonServiceSetting service : mManager.getBootList()) {
            try {
                mManager.activate(service.name, false);
            } catch (HexagonAvailabilityException ignored) {
            }
        }
    }

    private HexagonServiceSetting serviceSetting(String name) {
        HexagonServiceSetting setting = new HexagonServiceSetting(name);
        mTestServices.add(setting);
        mManager.add(setting);
        return setting;
    }

    private static Condition always(boolean satisfied) {
        return new FixedCondition(satisfied);
    }

    private static final class FixedCondition extends Condition {
        private final boolean mSatisfied;

        FixedCondition(boolean satisfied) {
            mSatisfied = satisfied;
        }

        @Override
        public boolean isSatisfied() {
            return mSatisfied;
        }
    }
}
