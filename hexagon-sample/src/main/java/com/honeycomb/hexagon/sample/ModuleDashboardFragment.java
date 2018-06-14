package com.honeycomb.hexagon.sample;

import android.app.Fragment;

import java.util.List;

public class ModuleDashboardFragment extends Fragment {


    private List<ModuleModel> loadModules() {
        // TODO
        return null;
    }

    private static class ModuleModel extends ModuleItemModel {
        List<ModuleItemModel> controller;
        List<ModuleItemModel> services;
    }

    private static class ModuleItemModel {
        String label;
        boolean active;
    }
}
