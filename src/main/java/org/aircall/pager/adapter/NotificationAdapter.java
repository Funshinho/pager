package org.aircall.pager.adapter;

import org.aircall.pager.model.Target;

/**
 * Notification adapter interface.
 *
 * @param <T> the target type
 */
public interface NotificationAdapter<T extends Target> {

    /**
     * Notifies the given target.
     *
     * @param target the target.
     */
    void notify(T target);
}
