package com.honeycomb.hexagon.register;

import com.honeycomb.hexagon.core.Condition;

public abstract class RegistrationCondition extends Condition {

    protected Condition dependsOn(Class<? extends ModuleRegistration> moduleClass) {
        final String moduleName = moduleClass != null ? moduleClass.getName() : null;
        return dependsOn(moduleName);
    }
}
