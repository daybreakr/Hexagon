package com.honeycomb.hexagon.sample.modules;

import com.honeycomb.hexagon.register.ModuleList;
import com.honeycomb.hexagon.sample.modules.dynamicinfo.DynamicInfoModule;
import com.honeycomb.hexagon.sample.modules.loaderinfo.LoaderInfoModule;
import com.honeycomb.hexagon.sample.modules.server.ServerModule;
import com.honeycomb.hexagon.sample.modules.update.UpdateModule;

public class HexagonSampleModuleList extends ModuleList {

    @Override
    protected void register() {
        module(new DynamicInfoModule());
        module(new LoaderInfoModule());
        module(new ServerModule());
        module(new UpdateModule());
    }
}
