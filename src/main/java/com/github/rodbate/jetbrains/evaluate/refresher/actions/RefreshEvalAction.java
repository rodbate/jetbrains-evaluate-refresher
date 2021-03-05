package com.github.rodbate.jetbrains.evaluate.refresher.actions;

import com.github.rodbate.jetbrains.evaluate.refresher.eval.EvalRefresher;
import com.github.rodbate.jetbrains.evaluate.refresher.eval.factory.EvalRefresherFactory;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Refresh eval action
 *
 * @author rodbate
 * @since 2021-03-05
 */
public class RefreshEvalAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        EvalRefresher evalRefresher = EvalRefresherFactory.getEvalRefresher();
        Exception exception = null;
        try {
            evalRefresher.refresh();
        } catch (Exception ex) {
            exception = ex;
        }
        if (exception != null) {
            Notifications.Bus.notify(new Notification("RefreshEvalAction", "RefreshEvalAction",
                String.format("failed to refresh eval.%n%s", exception.getMessage()), NotificationType.ERROR));
        } else {
            Notifications.Bus.notify(new Notification("RefreshEvalAction", "RefreshEvalAction",
                "refresh eval successfully!", NotificationType.INFORMATION));
        }
    }
}
