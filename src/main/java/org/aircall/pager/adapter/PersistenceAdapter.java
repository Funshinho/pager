package org.aircall.pager.adapter;

import org.aircall.pager.model.Alert;
import org.aircall.pager.model.MonitoredService;

/**
 * Persistence adapter interface.
 */
public interface PersistenceAdapter {

    /**
     * Finds a monitored service by its id.
     *
     * @param monitoredServiceId the monitored service id
     * @return the monitored service
     */
    MonitoredService findMonitoredServiceById(String monitoredServiceId);

    /**
     * Updates a monitored service.
     *
     * @param monitoredService the monitored service
     */
    void updateMonitoredService(MonitoredService monitoredService);

    /**
     * Saves an alert.
     *
     * @param alert the alert
     */
    void saveAlert(Alert alert);
}
