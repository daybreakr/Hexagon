package com.honeycomb.hexagon.sample;

import android.app.Application;

import com.honeycomb.hexagon.Hexagon;
import com.honeycomb.hexagon.sample.modules.HexagonSampleModuleList;

public class HexagonSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hexagon.install(new HexagonSampleModuleList());
    }
}
