package org.aircall.pager.service.impl;

import java.util.List;

import org.aircall.pager.adapter.*;
import org.aircall.pager.model.*;
import org.aircall.pager.service.PagerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pager service implementation.
 */
public class PagerServiceImpl implements PagerService {

    public static final int ACK_TIMEOUT = 15; // timeout (in minutes)

    private static final Logger logger = LoggerFactory.getLogger(PagerServiceImpl.class);

    private final PersistenceAdapter persistenceAdapter;

    private final EscalationPolicyAdapter escalationPolicyAdapter;

    private final TimerAdapter timerAdapter;

    private final EmailAdapter emailAdapter;

    private final SMSAdapter smsAdapter;

    public PagerServiceImpl(PersistenceAdapter persistenceAdapter, EscalationPolicyAdapter escalationPolicyAdapter,
            TimerAdapter timerAdapter, EmailAdapter emailAdapter, SMSAdapter smsAdapter) {
        this.persistenceAdapter = persistenceAdapter;
        this.escalationPolicyAdapter = escalationPolicyAdapter;
        this.timerAdapter = timerAdapter;
        this.emailAdapter = emailAdapter;
        this.smsAdapter = smsAdapter;
    }

    @Override
    public void onAlertReceived(Alert alert) {
        String serviceId = alert.getMonitoredServiceId();

        MonitoredService service = persistenceAdapter.findMonitoredServiceById(serviceId);
        // we currently handle only one alert at a time
        if (!service.isHealthy()) {
            logger.info("Alert " + alert.getMessage() + " is ignored");
            return;
        }
        
        service.setHealthy(false);
        persistenceAdapter.updateMonitoredService(service);
        persistenceAdapter.saveAlert(alert);
        setTimeoutAndNotifyTargets(service, alert.getLevel());
    }

    @Override
    public void onAlertAckReceived(Alert alert) {
        MonitoredService service = persistenceAdapter.findMonitoredServiceById(alert.getMonitoredServiceId());
        if (!service.isHealthy() && alert.isAcknowledged()) {
            persistenceAdapter.saveAlert(alert);
        }
    }

    @Override
    public void onAlertAckTimeout(Alert alert) {
        MonitoredService service = persistenceAdapter.findMonitoredServiceById(alert.getMonitoredServiceId());
        if (!service.isHealthy() && !alert.isAcknowledged()) {
            long level = alert.getLevel();
            alert.setLevel(++level);
            setTimeoutAndNotifyTargets(service, level);
        }
    }

    @Override
    public void setHealthyState(String monitoredServiceId) {
        MonitoredService service = persistenceAdapter.findMonitoredServiceById(monitoredServiceId);
        if (!service.isHealthy()) {
            service.setHealthy(true);
            persistenceAdapter.updateMonitoredService(service);
        }
    }

    private void setTimeoutAndNotifyTargets(MonitoredService service, long escalationLevel) {
        EscalationPolicy policy = escalationPolicyAdapter.getEscalationPolicy(service.getId());
        List<Target> targets = policy.getTargetsByLevel().get(escalationLevel);
        if (targets == null) {
            logger.error("Unknown level " + escalationLevel);
            return;
        }
        targets.forEach(target -> {
            switch (target.getType()) {
                case SMS:
                    smsAdapter.notify((SMSTarget) target);
                    logger.info("SMS sent to " + target.getTarget());
                    break;
                case EMAIL:
                    emailAdapter.notify((EmailTarget) target);
                    logger.info("Email sent to " + target.getTarget());
                    break;
                default:
                    logger.error("Unknown notification type");
                    break;
            }
        });

        // Set acknowledgment timeout
        timerAdapter.setAckTimeout(service.getId(), ACK_TIMEOUT);
        logger.info("Alert to acknowledge sent for service " + service.getId() + ", timeout = " + ACK_TIMEOUT + " " +
                "minutes");
    }
}
