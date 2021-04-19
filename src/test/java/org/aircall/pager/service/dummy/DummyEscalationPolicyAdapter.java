package org.aircall.pager.service.dummy;

import java.util.List;
import java.util.Map;

import org.aircall.pager.adapter.EscalationPolicyAdapter;
import org.aircall.pager.model.*;

public class DummyEscalationPolicyAdapter implements EscalationPolicyAdapter {

    private final Map<String, EscalationPolicy> policies;
    
    public DummyEscalationPolicyAdapter(Map<String, EscalationPolicy> policies) {
        this.policies = policies;
    }
    
    @Override
    public EscalationPolicy getEscalationPolicy(String monitoredServiceId) {
        return policies.get(monitoredServiceId);
    }

    @Override
    public List<Target> getLevelTargets(String monitoredServiceId, long level) {
        return policies.get(monitoredServiceId).getTargetsByLevel().get(level);
    }
}
