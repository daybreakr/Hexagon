package com.honeycomb.hexagon.sample;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honeycomb.hexagon.Hexagon;
import com.honeycomb.hexagon.core.ControllerInfo;
import com.honeycomb.hexagon.core.ModuleInfo;
import com.honeycomb.hexagon.core.ModuleItemInfo;
import com.honeycomb.hexagon.core.ServiceInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ModuleDashboardFragment extends Fragment {
    private ModulesAdapter mModulesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_module_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context context = getActivity();

        mModulesAdapter = new ModulesAdapter(context);

        RecyclerView modulesView = view.findViewById(R.id.modules);
        modulesView.setLayoutManager(new LinearLayoutManager(context));
        modulesView.setAdapter(mModulesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshModules();
    }

    private void refreshModules() {
        mModulesAdapter.setItems(loadModuleItems());
    }

    //==============================================================================================
    // View
    //==============================================================================================

    private static class ModulesAdapter extends RecyclerView.Adapter<ModulesViewHolder> {
        private final List<ModuleItemModel> mModuleItems = new ArrayList<>();

        private LayoutInflater mInflater;

        ModulesAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        void setItems(List<ModuleItemModel> items) {
            if (items != null) {
                mModuleItems.clear();
                mModuleItems.addAll(items);
                notifyDataSetChanged();
            }
        }

        private ModuleItemModel getItem(int position) {
            return mModuleItems.get(position);
        }

        @Override
        public int getItemCount() {
            return mModuleItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @NonNull
        @Override
        public ModulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.module_item, parent, false);
            return new ModulesViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ModulesViewHolder holder, int position) {
            ModuleItemModel item = getItem(position);
            StringBuilder label = new StringBuilder(item.label);
            switch (item.type) {
                case ModuleItemModel.TYPE_MODULE:
                    label.insert(0, "M: ");
                    break;
                case ModuleItemModel.TYPE_CONTROLLER:
                    label.insert(0, "    C: ");
                    break;
                case ModuleItemModel.TYPE_SERVICE:
                    label.insert(0, "    S: ");
                    break;
            }
            holder.label.setText(label);
            if (item.active) {
                holder.label.setTextColor(Color.parseColor("#4CAF50"));
            } else {
                holder.label.setTextColor(Color.parseColor("#F44336"));
            }
        }
    }

    private static class ModulesViewHolder extends RecyclerView.ViewHolder {
        TextView label;

        ModulesViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
        }
    }

    //==============================================================================================
    // Model
    //==============================================================================================

    private List<ModuleItemModel> loadModuleItems() {
        List<ModuleItemModel> items = new LinkedList<>();

        for (ModuleModel module : loadModules()) {
            items.add(module);
            if (module.controllers != null) {
                items.addAll(module.controllers);
            }
            if (module.services != null) {
                items.addAll(module.services);
            }
        }

        return items;
    }

    private List<ModuleModel> loadModules() {
        List<ModuleModel> modules = new LinkedList<>();

        for (ModuleInfo info : Hexagon.modules()) {
            ModuleModel model = new ModuleModel(info);
            model.controllers = loadControllers(info.controllers());
            model.services = loadServices(info.services());
            modules.add(model);
        }

        return modules;
    }

    private List<ModuleItemModel> loadControllers(Set<String> names) {
        List<ModuleItemModel> controllers = new LinkedList<>();

        for (String name : names) {
            ControllerInfo controller = Hexagon.controllerInfo(name);
            if (controller != null) {
                controllers.add(new ModuleItemModel(controller, ModuleItemModel.TYPE_CONTROLLER));
            }
        }

        return controllers;
    }

    private List<ModuleItemModel> loadServices(Set<String> names) {
        List<ModuleItemModel> services = new LinkedList<>();

        for (String name : names) {
            ServiceInfo service = Hexagon.serviceInfo(name);
            if (service != null) {
                services.add(new ModuleItemModel(service, ModuleItemModel.TYPE_SERVICE));
            }
        }

        return services;
    }

    private static class ModuleItemModel {
        static final int TYPE_MODULE = 1;
        static final int TYPE_CONTROLLER = 2;
        static final int TYPE_SERVICE = 3;

        String label;
        boolean active;
        int type;

        ModuleItemModel(ModuleItemInfo info, int type) {
            this.label = info.label();
            this.active = info.active();
            this.type = type;
        }
    }

    private static class ModuleModel extends ModuleItemModel {
        List<ModuleItemModel> controllers;
        List<ModuleItemModel> services;

        ModuleModel(ModuleInfo info) {
            super(info, TYPE_MODULE);
        }
    }
}
