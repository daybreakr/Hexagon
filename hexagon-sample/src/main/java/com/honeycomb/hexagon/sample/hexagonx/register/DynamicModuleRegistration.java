package com.honeycomb.hexagon.sample.hexagonx.register;

import com.honeycomb.hexagon.register.ModuleRegistration;
import com.honeycomb.hexagon.sample.hexagonx.conditions.MinLoaderVersionCondition;

public abstract class DynamicModuleRegistration extends ModuleRegistration {

    protected DynamicModuleRegistration minLoaderVersion(int minLoaderVersion) {
        require(new MinLoaderVersionCondition(minLoaderVersion));
        return this;
    }
}
