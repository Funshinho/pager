package org.aircall.pager.service;

import static org.aircall.pager.service.impl.PagerServiceImpl.ACK_TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.*;

import org.aircall.pager.adapter.*;
import org.aircall.pager.model.*;
import org.aircall.pager.service.dummy.*;
import org.aircall.pager.service.impl.PagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PagerServiceTest {

    private PersistenceAdapter persistenceAdapter;

    private EscalationPolicyAdapter escalationPolicyAdapter;

    private TimerAdapter timerAdapter;

    private EmailAdapter emailAdapter;

    private SMSAdapter smsAdapter;

    private PagerService pagerService;

    private EmailTarget target1;
    private EmailTarget target2;
    private SMSTarget target3;

    private MonitoredService ms1;
    private MonitoredService ms2;

    /**
     * Initialize dummy databases
     */
    public PagerServiceTest() {
        target1 = new EmailTarget("test1@test.com");
        target2 = new EmailTarget("test2@test.com");
        target3 = new SMSTarget("061122334455");

        ms1 = new MonitoredService("MS1", true);
        ms2 = new MonitoredService("MS2", false);
        Map<String, MonitoredService> monitoredServices = new HashMap<>();
        monitoredServices.put("MS1", ms1);
        monitoredServices.put("MS2", ms2);

        Map<String, EscalationPolicy> policies = new HashMap<>();
        policies.put("MS1", new EscalationPolicy("MS1", Map.of(0L, List.of(target1), 1L, List.of(target2, target3))));
        policies.put("MS2", new EscalationPolicy("MS2", Map.of(0L, List.of(target1), 1L, List.of(target2, target3))));

        persistenceAdapter = new DummyPersistenceAdapter(monitoredServices);
        escalationPolicyAdapter = new DummyEscalationPolicyAdapter(policies);
    }

    @BeforeEach
    public void setup() {
        
        emailAdapter = mock(EmailAdapter.class);
        smsAdapter = mock(SMSAdapter.class);
        timerAdapter = mock(TimerAdapter.class);

        pagerService = new PagerServiceImpl(persistenceAdapter, escalationPolicyAdapter, timerAdapter, emailAdapter,
                smsAdapter);
    }

    @Test
    public void testAlertReceivedOnHealthyMonitoredService() {
        // Given a Monitored Service in a Healthy State,
        // when the Pager receives an Alert related to this Monitored Service
        Alert alert = new Alert("MS1", "MS1 is down !");
        pagerService.onAlertReceived(alert);

        // then the Monitored Service becomes Unhealthy
        MonitoredService service = persistenceAdapter.findMonitoredServiceById("MS1");
        assertFalse(service.isHealthy());

        // the Pager notifies all targets of the first level of the escalation policy
        verify(emailAdapter).notify(target1);

        // and sets a 15-minutes acknowledgement delay
        verify(timerAdapter).setAckTimeout(ms1.getId(), ACK_TIMEOUT);
    }

    @Test
    public void testAcknowledgementTimeout() {
        // Given a Monitored Service in an Unhealthy State,
        // the corresponding Alert is not Acknowledged and the last level has not been notified
        Alert alert = new Alert("MS2", "MS2 is down !", 0);
        persistenceAdapter.saveAlert(alert);

        // when the Pager receives the Acknowledgement Timeout
        pagerService.onAlertAckTimeout(alert);

        // then the Pager notifies all targets of the next level of the escalation policy
        verify(emailAdapter).notify(target2);
        verify(smsAdapter).notify(target3);

        // and sets a 15-minutes acknowledgement delay
        verify(timerAdapter).setAckTimeout(ms2.getId(), ACK_TIMEOUT);
    }

    @Test
    public void testAcknowledgementReceived() {
        // Given a Monitored Service in an Unhealthy State
        Alert alert = new Alert("MS2", "MS2 is down !", 1);
        persistenceAdapter.saveAlert(alert);

        // when the Pager receives the Acknowledgement
        alert.setAcknowledged(true);
        pagerService.onAlertAckReceived(alert);

        // and later receives the Acknowledgement Timeout
        pagerService.onAlertAckTimeout(alert);

        // then the Pager doesn't notify any Target and doesn't set an acknowledgement delay
        verify(emailAdapter, never()).notify(target1);
        verify(timerAdapter, never()).setAckTimeout(ms2.getId(), ACK_TIMEOUT);
    }

    @Test
    public void testAlertReceivedOnUnealthyMonitoredService() {
        // Given a Monitored Service in an Unhealthy State,
        // when the Pager receives an Alert related to this Monitored Service
        Alert alert = new Alert("MS2", "MS2 is down !");
        pagerService.onAlertReceived(alert);

        // then the Pager doesn't notify any Target and doesn't set an acknowledgement delay
        verify(emailAdapter, never()).notify(target1);
        verify(timerAdapter, never()).setAckTimeout(ms2.getId(), ACK_TIMEOUT);
    }

    @Test
    public void testHealthyEventReceivedOnUnhealthyMonitoredService() {
        // Given a Monitored Service in an Unhealthy State,
        // when the Pager receives a Healthy event related to this Monitored Service
        Alert alert = new Alert("MS2", "MS2 is down !");
        persistenceAdapter.saveAlert(alert);
        pagerService.setHealthyState(ms2.getId());

        // and later receives the Acknowledgement Timeout
        pagerService.onAlertAckTimeout(alert);

        // then the Monitored Service becomes Healthy
        MonitoredService ms = persistenceAdapter.findMonitoredServiceById(ms2.getId());
        assertTrue(ms.isHealthy());

        // the Pager doesn’t notify any Target and doesn’t set an acknowledgement delay
        verify(emailAdapter, never()).notify(target1);
        verify(timerAdapter, never()).setAckTimeout(ms2.getId(), ACK_TIMEOUT);
    }

}
