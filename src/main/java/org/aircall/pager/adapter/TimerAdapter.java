package org.aircall.pager.adapter;

/**
 * Timer adapter interface.
 */
public interface TimerAdapter {

    /**
     * Sets acknowledgment timeout for the given monitored service.
     *
     * @param monitoredServiceId the monitored service id
     * @param timeout            the timeout period (in minutes)
     */
    void setAckTimeout(String monitoredServiceId, int timeout);
}
