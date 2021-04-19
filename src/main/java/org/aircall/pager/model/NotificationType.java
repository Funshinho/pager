package org.aircall.pager.model;

/**
 * Notification types.
 */
public enum NotificationType {

    EMAIL("email"),
    SMS("sms");
    
    private String type;
    
    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
