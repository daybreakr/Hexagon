package com.honeycomb.hexagonx.core;

class FixedCondition extends Condition {
    private final boolean mSatisfied;

    FixedCondition(boolean satisfied) {
        mSatisfied = satisfied;
    }

    @Override
    public boolean isSatisfied() {
        return mSatisfied;
    }
}
