package org.aircall.pager.model;

/**
 * Alert definition.
 */
public class Alert {

    private String monitoredServiceId;

    private String message;

    private long level;

    private boolean acknowledged;

    public Alert(String monitoredServiceId, String message) {
        this(monitoredServiceId, message, 0);
    }
    
    public Alert(String monitoredServiceId, String message, long level) {
        this.monitoredServiceId = monitoredServiceId;
        this.message = message;
        this.level = level;
    }

    public String getMonitoredServiceId() {
        return monitoredServiceId;
    }

    public String getMessage() {
        return message;
    }

    public long getLevel() {
        return level;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setMonitoredServiceId(String monitoredServiceId) {
        this.monitoredServiceId = monitoredServiceId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
}
