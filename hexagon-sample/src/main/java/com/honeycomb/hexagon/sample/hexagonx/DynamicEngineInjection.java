package com.honeycomb.hexagon.sample.hexagonx;

import com.honeycomb.hexagon.HexagonEngine;
import com.honeycomb.hexagon.register.ModuleList;
import com.honeycomb.hexagon.sample.modules.DynamicModuleList;

class DynamicEngineInjection {

    static DynamicEngineInjection create() {
        return new DynamicEngineInjection();
    }

    HexagonEngine provideEngine() {
        return HexagonEngine.create();
    }

    ModuleList provideBootstrapModules() {
        return new DynamicModuleList();
    }
}
