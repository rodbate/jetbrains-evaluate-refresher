package com.github.rodbate.jetbrains.evaluate.refresher.eval.checker;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.github.rodbate.jetbrains.evaluate.refresher.eval.EvalRefreshExecutor;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.ExtensionNotApplicableException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.LicensingFacade;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.util.text.DateFormatUtil;
import org.jetbrains.annotations.NotNull;

public class EvaluationExpiredChecker implements StartupActivity.DumbAware {

    public EvaluationExpiredChecker() {
        if (ApplicationManager.getApplication()
            .isHeadlessEnvironment()) {
            throw ExtensionNotApplicableException.INSTANCE;
        }
    }

    @Override
    public void runActivity(@NotNull Project project) {
        AppExecutorUtil.getAppScheduledExecutorService()
            .schedule(new ExpiredCheckTask(), 10, TimeUnit.SECONDS);
    }


    private void checkExpiration() {
        LicensingFacade license = LicensingFacade.getInstance();
        if (null == license || !license.isEvaluationLicense()) {
            return;
        }
        Date expirationDate = license.expirationDate;
        long differenceInDays = DateFormatUtil.getDifferenceInDays(new Date(), expirationDate);
        if (differenceInDays > 2) {
            return;
        }
        ApplicationManager.getApplication().invokeLater(() -> {
            boolean yes = Messages.showYesNoDialog("Evaluation will expire at " + expirationDate, "Evaluation Expiration",
                "Refresh Now", "Not Now", Messages.getQuestionIcon()) == Messages.YES;
            if (yes) {
                EvalRefreshExecutor.execute();
            }
        });
    }

    private void log(String msg, NotificationType notificationType) {
        Notifications.Bus.notify(new Notification("EvaluationExpiredChecker", "EvaluationExpiredChecker",
            msg, notificationType));
    }


    private class ExpiredCheckTask implements Runnable {
        @Override
        public void run() {
            try {
                EvaluationExpiredChecker.this.checkExpiration();
            } catch (Exception e) {
                log("ExpiredCheckTask Error: " + e.getMessage(), NotificationType.ERROR);
                return;
            }
            AppExecutorUtil.getAppScheduledExecutorService()
                .schedule(this, 5, TimeUnit.HOURS);
        }
    }
}
