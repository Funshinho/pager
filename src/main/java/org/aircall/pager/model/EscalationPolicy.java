package org.aircall.pager.model;

import java.util.List;
import java.util.Map;

/**
 * Escalation policy definition.
 */
public class EscalationPolicy {

    private String monitoredServiceId;

    private Map<Long, List<Target>> targetsByLevel;

    public EscalationPolicy(String monitoredServiceId, Map<Long, List<Target>> targetsByLevel) {
        this.monitoredServiceId = monitoredServiceId;
        this.targetsByLevel = targetsByLevel;
    }

    public String getMonitoredServiceId() {
        return monitoredServiceId;
    }

    public Map<Long, List<Target>> getTargetsByLevel() {
        return targetsByLevel;
    }

    public void setMonitoredServiceId(String monitoredServiceId) {
        this.monitoredServiceId = monitoredServiceId;
    }

    public void setTargetsByLevel(Map<Long, List<Target>> targetsByLevel) {
        this.targetsByLevel = targetsByLevel;
    }
}
