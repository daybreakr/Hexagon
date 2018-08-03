package com.honeycomb.hexagonx.core;

import org.junit.Before;
import org.junit.Test;

import static com.honeycomb.hexagonx.core.TestConditions.always;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class HexagonComponentSettingTest {
    private HexagonComponentSetting mComponentA;
    private HexagonComponentSetting mComponentB;
    private HexagonComponentSetting mComponentC;
    private HexagonComponentSetting mComponentD;
    private HexagonComponentSetting mComponentE;

    @Before
    public void setUp() {
        mComponentA = componentSetting("A");
        mComponentB = componentSetting("B");
        mComponentC = componentSetting("C");
        mComponentD = componentSetting("D");
        mComponentE = componentSetting("E");
    }

    @Test
    public void testDependsOn() {
        mComponentA.dependsOn(mComponentB);

        assertEquals(mComponentA.dependencies().size(), 1);
        assertEquals(mComponentB.dependedOn().size(), 1);

        assertEquals(mComponentA.dependencies().get(0), mComponentB);
        assertEquals(mComponentB.dependedOn().get(0), mComponentA);
    }

    @Test
    public void testCondition() {
        mComponentA.condition(always(true));

        assertEquals(mComponentA.conditions().size(), 1);
        assertTrue(mComponentA.conditions().get(0).isSatisfied());
    }

    @Test
    public void testEnable_successWithNoCondition() throws HexagonAvailabilityException {
        enforceDisabled(mComponentA);

        mComponentA.enable();

        assertTrue(mComponentA.isEnabled());
    }

    @Test
    public void testEnable_successWithSatisfiedCondition() throws HexagonAvailabilityException {
        mComponentA.condition(always(true));
        enforceDisabled(mComponentA);

        mComponentA.enable();

        assertTrue(mComponentA.isEnabled());
    }

    @Test
    public void testEnable_failWithUnsatisfiedCondition() {
        mComponentA.condition(always(false));
        enforceDisabled(mComponentA);

        try {
            mComponentA.enable();
            fail();
        } catch (HexagonAvailabilityException expected) {
            assertThat(expected.getComponentName(), equalTo(mComponentA.name));
            assertThat(expected.getExtraMessage(), startsWith("Condition not satisfied: "));
        }
    }

    @Test
    public void testDisable() throws HexagonAvailabilityException {
        enforceEnabled(mComponentA);

        // Check disable
        mComponentA.disable();

        assertFalse(mComponentA.isEnabled());

        // Check duplicate call on disabled state.
        mComponentA.disable();

        assertFalse(mComponentA.isEnabled());
    }

    @Test
    public void testAvailability_initialized() {
        assertFalse(mComponentA.isEnabled());
        assertFalse(mComponentA.isAvailabilityDirty());
    }

    @Test
    public void testIsAvailable_allAvailableWhenAllEnabled() throws HexagonAvailabilityException {
        mComponentA.dependsOn(mComponentB);
        mComponentB.dependsOn(mComponentC);
        enforceEnabled(mComponentA, mComponentB, mComponentC);

        assertTrue(mComponentA.isAvailable());
        assertTrue(mComponentB.isAvailable());
        assertTrue(mComponentC.isAvailable());
    }

    @Test
    public void testIsAvailable_notAvailableWhenAnyDependenciesDisabled()
            throws HexagonAvailabilityException {
        // A -> B -> C - >E
        //       \-> D
        mComponentA.dependsOn(mComponentB);
        mComponentB.dependsOn(mComponentC).dependsOn(mComponentD);
        mComponentC.dependsOn(mComponentE);
        enforceEnabled(mComponentA, mComponentB, mComponentC, mComponentD, mComponentE);

        mComponentC.disable();

        // Check self not available
        assertFalse(mComponentC.isAvailable());

        // Check explicitly depended on not available.
        assertFalse(mComponentB.isAvailable());

        // Check implicitly depended on not available.
        assertFalse(mComponentA.isAvailable());

        // Check available if not depended on it.
        assertTrue(mComponentD.isAvailable());
        assertTrue(mComponentE.isAvailable());
    }

    @Test
    public void testIsAvailabilityDirty_dirtyWhenSelfEnabledStateChanged()
            throws HexagonAvailabilityException {
        enforceDisabled(mComponentA);

        mComponentA.enable();

        // Make sure enabled state changed.
        assertTrue(mComponentA.isEnabled());

        // Check availability dirty if enabled state changed.
        assertTrue(mComponentA.isAvailabilityDirty());
    }

    @Test
    public void testIsAvailabilityDirty_dirtyWhenDependentEnabledStateChanged()
            throws HexagonAvailabilityException {
        mComponentA.dependsOn(mComponentB);
        mComponentB.dependsOn(mComponentC);
        enforceEnabled(mComponentA, mComponentB, mComponentC);

        // Make sure availability been refreshed.
        assertTrue(mComponentA.isAvailabilityDirty());
        assertTrue(mComponentB.isAvailabilityDirty());
        assertTrue(mComponentC.isAvailabilityDirty());
        mComponentA.isAvailable();
        mComponentB.isAvailable();
        mComponentC.isAvailable();
        assertFalse(mComponentA.isAvailabilityDirty());
        assertFalse(mComponentB.isAvailabilityDirty());
        assertFalse(mComponentC.isAvailabilityDirty());

        mComponentB.disable();

        assertTrue(mComponentA.isAvailabilityDirty());
        assertTrue(mComponentB.isAvailabilityDirty());
        assertFalse(mComponentC.isAvailabilityDirty());
    }

    @Test
    public void testIsAvailabilityDirty_notDirtyWhenAvailabilityRefreshed()
            throws HexagonAvailabilityException {
        // Make sure the enabled state changed.
        mComponentA.disable();
        mComponentA.enable();

        // Expected dirty when enabled state changed.
        assertTrue(mComponentA.isAvailabilityDirty());

        // Read and refreshed the availability flags.
        assertTrue(mComponentA.isAvailable());

        // Expected not dirty when availability refreshed.
        assertFalse(mComponentA.isAvailabilityDirty());
    }

    private void enforceEnabled(HexagonComponentSetting... settings)
            throws HexagonAvailabilityException {
        if (settings != null && settings.length > 0) {
            for (HexagonComponentSetting setting : settings) {
                setting.enable();
                assertTrue(setting.isEnabled());
            }
        }
    }

    private void enforceDisabled(HexagonComponentSetting... settings) {
        if (settings != null && settings.length > 0) {
            for (HexagonComponentSetting setting : settings) {
                setting.disable();
                assertFalse(setting.isEnabled());
            }
        }
    }

    private HexagonComponentSetting componentSetting(String name) {
        return new HexagonComponentSetting(name);
    }
}
