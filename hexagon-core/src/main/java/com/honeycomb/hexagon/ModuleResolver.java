package com.honeycomb.hexagon;

import com.honeycomb.hexagon.core.Condition;
import com.honeycomb.hexagon.core.ControllerInfo;
import com.honeycomb.hexagon.core.ModuleInfo;
import com.honeycomb.hexagon.core.ModuleItemInfo;
import com.honeycomb.hexagon.core.ServiceInfo;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleResolver extends HexagonComponent {
    private final AtomicBoolean mResolving = new AtomicBoolean(false);
    private final Stack<String> mResolvingModules = new Stack<>();

    public void resolve() {
        if (assembly() == null) {
            throw new IllegalStateException("Engine not attached.");
        }

        if (mResolving.compareAndSet(false, true)) {
            try {
                mResolvingModules.clear();

                Set<String> modules = assembly().moduleRegistry().getRegisteredModuleNames();

                for (String module : modules) {
                    resolveModule(module);
                }
            } finally {
                mResolving.set(false);
            }
        }
    }

    private boolean resolveModule(String name) {
        ModuleInfo module = assembly().moduleRegistry().getModuleInfo(name);
        if (module == null) {
            // TODO: Warn module not registered
            return false;
        }

        // Returns immediately if already resolved.
        if (module.resolved()) {
            return true;
        }

        // Check circular dependency.
        if (mResolvingModules.contains(name)) {
            throw new IllegalStateException("Module " + module.label() + " is resolving"
                    + ", circular dependency may occurred: " + dumpResolving());
        }

        // Mark module resolving, if this module appeared again, circular dependency occurred.
        mResolvingModules.push(name);
        boolean resolved = false;
        try {
            resolveModuleItem(module);

            // Resolve controllers.
            for (String controller : module.controllers()) {
                resolveController(controller);
            }

            // Resolve services.
            for (String service : module.services()) {
                resolveService(service);
            }

            resolved = true;
        } catch (Exception e) {
            // TODO: Warn failed to resolve module
        } finally {
            mResolvingModules.pop();
            moduleResolved(module, resolved);
        }

        return resolved;
    }

    private void resolveController(String name) {
        ControllerInfo controller = assembly().controllerRegistry().getControllerInfo(name);
        if (controller == null) {
            // TODO: Warn controller not registered.
            return;
        }

        // Returns immediately if already resolved.
        if (controller.resolved()) {
            return;
        }

        boolean resolved = false;
        try {
            resolveModuleItem(controller);
            resolved = true;
        } catch (Exception e) {
            // TODO: Warn failed to resolve controller.
        } finally {
            controllerResolved(name, resolved);
        }
    }

    private void resolveService(String name) {
        ServiceInfo service = assembly().serviceRegistry().getServiceInfo(name);
        if (service == null) {
            // TODO: Warn if controller not registered
            return;
        }

        // Returns immediately if already resolved.
        if (service.resolved()) {
            return;
        }

        boolean resolved = false;
        try {
            resolveModuleItem(service);
            resolved = true;
        } catch (Exception e) {
            // TODO: Warn failed to resolve controller.
        } finally {
            serviceResolved(name, resolved);
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
        for (Condition prerequisite : moduleItem.prerequisites()) {
            if (!prerequisite.isSatisfied()) {
                throw new Exception("Prerequisite not satisfied: "
                        + prerequisite.getClass().getSimpleName());
            }
        }
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

    private void moduleResolved(ModuleInfo module, boolean enabled) {
        assembly().moduleRegistry().resolved(module.name(), enabled);

        // Disable controllers and services in this module when the module was disabled.
        if (!enabled) {
            for (String controller : module.controllers()) {
                controllerResolved(controller, false);
            }

            for (String service : module.services()) {
                serviceResolved(service, false);
            }
        }
    }

    private void controllerResolved(String name, boolean enabled) {
        assembly().controllerRegistry().resolved(name, enabled);
    }

    private void serviceResolved(String name, boolean enabled) {
        assembly().serviceRegistry().resolved(name, enabled);
    }

    private String dumpResolving() {
        StringBuilder resolving = new StringBuilder();
        Iterator<String> it = mResolvingModules.iterator();
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
