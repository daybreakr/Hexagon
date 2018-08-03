package com.honeycomb.hexagonx.core;

class TestConditions {

    private TestConditions() {
    }

    static Condition always(boolean satisfied) {
        return new FixedCondition(satisfied);
    }

    private static class FixedCondition extends Condition {
        private final boolean mSatisfied;

        FixedCondition(boolean satisfied) {
            mSatisfied = satisfied;
        }

        @Override
        public boolean isSatisfied() {
            return mSatisfied;
        }

        @Override
        public String getDescription() {
            return super.getDescription() + ", satisfied=" + mSatisfied;
        }
    }
}
