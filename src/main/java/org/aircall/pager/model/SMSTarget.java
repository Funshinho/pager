package org.aircall.pager.model;

/**
 * SMS target definition.
 */
public class SMSTarget extends Target {

    public SMSTarget(String target) {
        super(NotificationType.SMS, target);
    }
}
