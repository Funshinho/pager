package org.aircall.pager.adapter;

import java.util.List;

import org.aircall.pager.model.*;

/**
 * Escalation policy adapter interface.
 */
public interface EscalationPolicyAdapter {

    /**
     * Finds an escalation policy from given monitored service id.
     *
     * @param monitoredServiceId the monitored service id
     * @return the escalation policy
     */
    EscalationPolicy getEscalationPolicy(String monitoredServiceId);

    /**
     * Gets targets of given level.
     *
     * @param monitoredServiceId the monitored service id
     * @param level              the escalation level
     * @return the list of targets
     */
    List<Target> getLevelTargets(String monitoredServiceId, long level);
}
