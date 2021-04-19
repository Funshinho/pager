package org.aircall.pager.model;

/**
 * Email target definition.
 */
public class EmailTarget extends Target {

    public EmailTarget(String target) {
        super(NotificationType.EMAIL, target);
    }
}
