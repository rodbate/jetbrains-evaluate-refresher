package com.github.rodbate.jetbrains.evaluate.refresher.actions;

import com.github.rodbate.jetbrains.evaluate.refresher.eval.factory.EvalRefresherFactory;
import com.intellij.idea.Main;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.Restarter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Refresh eval action
 *
 * @author rodbate
 * @since 2021-03-05
 */
public class RefreshEvalAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Exception exception = null;
        try {
            EvalRefresherFactory.getEvalRefresher().refresh();
        } catch (Exception ex) {
            exception = ex;
        }
        if (exception != null) {
            Notifications.Bus.notify(new Notification("RefreshEvalAction", "RefreshEvalAction",
                String.format("Failed to refresh eval.%n%s", exception.getMessage()), NotificationType.ERROR));
        } else {
            restartIde();
        }
    }


    private void restartIde() {
        if (Restarter.isSupported()) {
            try {
                Restarter.scheduleRestart(false);
            }
            catch (IOException e) {
                Main.showMessage("Restart failed", e);
            }
            System.exit(0);
        }
        else {
            String title = "Refresh Evaluation";
            String message = "Refresh Evaluation successfully.";
            String yes = "Restart Now";
            String no = "Restart Later";
            if (Messages.showYesNoDialog(message, title, yes, no, Messages.getQuestionIcon()) == Messages.YES) {
                System.exit(0);
            }
        }
    }
}
