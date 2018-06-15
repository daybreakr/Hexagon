package com.honeycomb.hexagon.sample.hexagonx;

import com.honeycomb.hexagon.HexagonAssembly;
import com.honeycomb.hexagon.register.ModuleList;
import com.honeycomb.hexagon.sample.modules.HexagonSampleModuleList;

class DynamicEngineInjection {

    static DynamicEngineInjection create() {
        return new DynamicEngineInjection();
    }

    HexagonAssembly provideEngine() {
        return HexagonAssembly.create();
    }

    ModuleList provideBootstrapModules() {
        return new HexagonSampleModuleList();
    }
}
