package com.github.rodbate.jetbrains.evaluate.refresher.actions;

import com.github.rodbate.jetbrains.evaluate.refresher.eval.EvalRefreshExecutor;
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
        EvalRefreshExecutor.execute();
    }
}
