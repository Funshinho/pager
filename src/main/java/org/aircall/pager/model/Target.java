package org.aircall.pager.model;

/**
 * Target definition.
 */
public class Target {

    private NotificationType type;

    private String target;

    public Target(NotificationType type, String target) {
        this.type = type;
        this.target = target;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
