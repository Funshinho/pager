package org.aircall.pager.service;

import org.aircall.pager.model.Alert;

/**
 * Pager service interface.
 */
public interface PagerService {

    /**
     * Handler when an alert is received.
     *
     * @param alert the alert
     */
    void onAlertReceived(Alert alert);

    /**
     * Handler when an alert is acknowledged.
     *
     * @param alert the alert
     */
    void onAlertAckReceived(Alert alert);

    /**
     * Handler when a timeout occurred for acknowledging an alert.
     *
     * @param alert the alert
     */
    void onAlertAckTimeout(Alert alert);

    /**
     * Set monitored service state to healthy.
     *
     * @param monitoredServiceId the monitored service id
     */
    void setHealthyState(String monitoredServiceId);
}
