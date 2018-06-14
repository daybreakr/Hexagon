package com.honeycomb.hexagon.sample;

import android.app.Application;

import com.honeycomb.hexagon.sample.hexagonx.DynamicEngine;

public class HexagonSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicEngine.start();
    }
}
