package com.honeycomb.hexagon.resolve;

import com.honeycomb.basement.condition.ICondition;
import com.honeycomb.hexagon.HexagonEngine;
import com.honeycomb.hexagon.core.ControllerInfo;
import com.honeycomb.hexagon.core.ModuleInfo;
import com.honeycomb.hexagon.core.ModuleItemInfo;
import com.honeycomb.hexagon.core.ServiceInfo;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class ModuleResolver {
    private HexagonEngine mEngine;

    private Stack<String> mResolving;

    public void setEngine(HexagonEngine engine) {
        mEngine = engine;
    }

    public void resolve() {
        if (mEngine == null) {
            throw new IllegalStateException("Engine not attached.");
        }

        if (mResolving != null) {
            return;
        }
        mResolving = new Stack<>();

        Set<String> modules = mEngine.getModuleRegistry().getRegisteredModuleNames();
        for (String module : modules) {
            resolveModule(module);
        }
    }

    private boolean resolveModule(String name) {
        ModuleInfo module = mEngine.getModuleRegistry().getModuleInfo(name);
        if (module == null) {
            // TODO: Warn if module not registered
            return false;
        }
        return resolveModule(module);
    }

    private boolean resolveModule(ModuleInfo module) {
        if (module.resolved()) {
            return true;
        }
        final String name = module.name();

        if (mResolving.contains(name)) {
            throw new IllegalStateException("Module " + module.label() + " is resolving"
                    + ", circular dependency may occurred: " + dumpResolving());
        }

        mResolving.push(name);
        try {
            resolve(module);
            return true;
        } catch (Exception e) {
            // TODO: Warn resolve exception
            disable(module);
            return false;
        } finally {
            mResolving.pop();
        }
    }

    private void resolveController(String name) {
        ControllerInfo controller = mEngine.getControllerRegistry().getControllerInfo(name);
        if (controller == null) {
            // TODO: Warn if controller not registered
            return;
        }
        resolveController(controller);
    }

    private void resolveController(ControllerInfo controller) {
        if (controller.resolved()) {
            return;
        }
        final String name = controller.name();

        if (mResolving.contains(name)) {
            throw new IllegalStateException("Module " + module.label() + " is resolving"
                    + ", circular dependency may occurred: " + dumpResolving());
        }

        mResolving.push(name);
        try {
            resolve(module);
        } catch (Exception e) {
            // TODO: Warn resolve exception
            disable(module);
        } finally {
            mResolving.pop();
        }
    }

    private void resolve(ModuleInfo module) throws Exception {
        resolveModuleItem(module);

        // Resolve controllers.
        for (String controller : module.controllers()) {
            resolveController(controller);
        }

        // Resolve services.
        for (String service : module.services()) {
            resolveService(service);
        }
    }

    private void resolveModuleItem(ModuleItemInfo moduleItem) throws Exception {
        // Resolve module dependencies.
        try {
            resolveDependencies(moduleItem.dependencies());
        } catch (Exception e) {
            throw new Exception("Failed to dependencies of module " + moduleItem.label(), e);
        }

        // Check prerequisites
        for (ICondition prerequisite : moduleItem.prerequisites()) {
            if (!prerequisite.isSatisfied()) {
                throw new Exception("Prerequisite not satisfied: "
                        + prerequisite.getClass().getSimpleName());
            }
        }
    }



    private boolean resolveService(String name) {
        ServiceInfo service = mEngine.getServiceRegistry().getServiceInfo(name);
        return resolve(service);
    }

    private boolean resolve(ServiceInfo service) {

    }

    private void resolveDependencies(Collection<String> dependencies) throws Exception {
        if (dependencies != null && !dependencies.isEmpty()) {
            for (String dependency : dependencies) {
                if (!resolveModule(dependency)) {
                    throw new Exception("Failed to resolve dependent module " + dependency);
                }
            }
        }
    }

    private void disable(ModuleInfo module) {
        // TODO
    }

    private void disable(ControllerInfo controller) {

    }

    private void disable(ServiceInfo service) {

    }

    private String dumpResolving() {
        StringBuilder resolving = new StringBuilder();
        Iterator<String> it = mResolving.iterator();
        while (it.hasNext()) {
            String module = it.next();
            resolving.append(module);
            if (it.hasNext()) {
                resolving.append("->");
            }
        }
        return resolving.toString();
    }
}
