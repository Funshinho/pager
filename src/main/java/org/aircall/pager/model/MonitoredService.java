package org.aircall.pager.model;

/**
 * Monitored service definition.
 */
public class MonitoredService {

    private String id;
    
    private boolean healthy;
    
    public MonitoredService(String id, boolean healthy) {
        this.id = id;
        this.healthy = healthy;
    }

    public String getId() {
        return id;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public void setId(String id) {
        this.id = id;
    }
}
