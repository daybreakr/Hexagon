package com.honeycomb.hexagon;

import com.honeycomb.hexagon.core.IService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        Set<String> names = mEngine.getServiceRegistry().getRegisteredServiceNames(category);
        if (names != null) {
            boolean result = true;
            for (String name : names) {
                result &= startService(name);
            }
            return result;
        }
        return false;
    }

    public boolean stopService(IService service) {
        final String name = service != null ? service.getClass().getName() : null;
        return name != null && stopService(name);
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

        Set<String> names = mEngine.getServiceRegistry().getRegisteredServiceNames(category);
        return stopServices(names);
    }

    public boolean stopAllServices() {
        if (mEngine == null) {
            return false;
        }

        Set<String> names = mEngine.getServiceRegistry().getRegisteredServiceNames();
        return stopServices(names);
    }

    private boolean stopServices(Set<String> names) {
        if (names != null) {
            boolean result = true;
            for (String name : names) {
                result &= stopService(name);
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
