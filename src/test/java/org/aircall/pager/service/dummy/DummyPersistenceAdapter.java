package org.aircall.pager.service.dummy;

import java.util.HashMap;
import java.util.Map;

import org.aircall.pager.adapter.PersistenceAdapter;
import org.aircall.pager.model.Alert;
import org.aircall.pager.model.MonitoredService;

public class DummyPersistenceAdapter implements PersistenceAdapter {

    private final Map<String, MonitoredService> monitoredServices;

    private final Map<String, Alert> alerts;

    public DummyPersistenceAdapter(Map<String, MonitoredService> monitoredServices) {
        this.monitoredServices = monitoredServices;
        alerts = new HashMap<>();
    }

    @Override
    public MonitoredService findMonitoredServiceById(String monitoredServiceId) {
        return monitoredServices.get(monitoredServiceId);
    }

    @Override
    public void updateMonitoredService(MonitoredService monitoredService) {
        monitoredServices.put(monitoredService.getId(), monitoredService);
    }

    @Override
    public void saveAlert(Alert alert) {
        alerts.put(alert.getMonitoredServiceId(), alert);
    }

}
