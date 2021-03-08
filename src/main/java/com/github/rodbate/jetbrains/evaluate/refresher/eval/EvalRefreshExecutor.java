package com.github.rodbate.jetbrains.evaluate.refresher.eval;

import java.io.IOException;

import com.github.rodbate.jetbrains.evaluate.refresher.eval.factory.EvalRefresherFactory;
import com.intellij.idea.Main;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.Restarter;

public class EvalRefreshExecutor {

    public static void execute() {
        Exception exception = null;
        try {
            EvalRefresherFactory.getEvalRefresher()
                .refresh();
        } catch (Exception ex) {
            exception = ex;
        }
        if (exception != null) {
            Notifications.Bus.notify(new Notification("EvalRefreshExecutor", "EvalRefreshExecutor",
                String.format("Failed to refresh eval.%n%s", exception.getMessage()), NotificationType.ERROR));
        } else {
            restartIde();
        }
    }


    private static void restartIde() {
        String title = "Refresh Evaluation";
        String message = "Refresh Evaluation successfully.";
        String yes = "Restart Now";
        String no = "Restart Later";
        if (Messages.showYesNoDialog(message, title, yes, no, Messages.getQuestionIcon()) == Messages.YES) {
            if (Restarter.isSupported()) {
                try {
                    Restarter.scheduleRestart(false);
                }
                catch (IOException e) {
                    Main.showMessage("Restart failed", e);
                }
            }
            System.exit(0);
        }
    }
}
