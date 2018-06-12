package com.honeycomb.hexagon.core.service;

import com.honeycomb.hexagon.HexagonEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager {
    private final Map<String, ServiceRecord> mRecords = new HashMap<>();

    private HexagonEngine mEngine;

    public void setEngine(HexagonEngine engine) {
        mEngine = engine;
    }

    public boolean startService(Class<? extends IService> serviceClass) {
        final String name = serviceClass != null ? serviceClass.getName() : null;
        return name != null && startService(name);
    }

    public boolean startService(String name) {
        if (mEngine == null) {
            return false;
        }

        IService service = mEngine.getServiceRegistry().getService(name);
        return service != null && startService(name, service);
    }

    public boolean startServices(String category) {
        if (mEngine == null) {
            return false;
        }

        List<ServiceInfo> services = mEngine.getServiceRegistry().getRegisteredServices(category);
        if (services != null) {
            boolean result = true;
            for (ServiceInfo service : services) {
                result &= startService(service.name);
            }
            return result;
        }
        return false;
    }

    public boolean stopService(Class<? extends IService> serviceClass) {
        final String name = serviceClass != null ? serviceClass.getName() : null;
        return name != null && stopService(name);
    }

    public boolean stopService(String name) {
        if (mEngine == null) {
            return false;
        }

        IService service = mEngine.getServiceRegistry().getService(name);
        return service != null && stopService(name, service);
    }

    public boolean stopServices(String category) {
        if (mEngine == null) {
            return false;
        }

        List<ServiceInfo> services = mEngine.getServiceRegistry().getRegisteredServices(category);
        return stopServices(services);
    }

    public boolean stopAllServices() {
        if (mEngine == null) {
            return false;
        }

        List<ServiceInfo> services = mEngine.getServiceRegistry().getRegisteredServices();
        return stopServices(services);
    }

    private boolean stopServices(List<ServiceInfo> services) {
        if (services != null) {
            boolean result = true;
            for (ServiceInfo service : services) {
                result &= stopService(service.name);
            }
            return result;
        }
        return false;
    }

    private boolean startService(String name, IService service) {
        // Preparing service record.
        ServiceRecord record;
        synchronized (mRecords) {
            record = mRecords.get(name);
            if (record == null) {
                record = newRecord(name);
            } else if (record.started) {
                // Service already started.
                return true;
            }
        }

        // Start the service.
        service.start();

        final boolean started = service.isStarted();
        record.started = started;

        return started;
    }

    private boolean stopService(String name, IService service) {
        // Preparing service record.
        ServiceRecord record = mRecords.get(name);
        if (record == null || !record.started) {
            // Service not started yet.
            return true;
        }

        // Stop the service
        service.stop();

        final boolean started = service.isStarted();
        record.started = started;

        return !started;
    }

    private ServiceRecord newRecord(String name) {
        ServiceRecord record = new ServiceRecord();
        record.name = name;
        record.started = false;
        mRecords.put(name, record);
        return record;
    }

    private static class ServiceRecord {
        String name;
        boolean started;
    }
}
