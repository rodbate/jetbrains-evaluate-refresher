package com.github.rodbate.jetbrains.evaluate.refresher.eval.factory;

import com.github.rodbate.jetbrains.evaluate.refresher.eval.EvalRefresher;
import com.github.rodbate.jetbrains.evaluate.refresher.eval.WindowsEvalRefresher;
import com.intellij.openapi.util.SystemInfo;

/**
 * EvalRefresherFactory
 *
 * @author rodbate
 * @since 2021-03-05
 */
public class EvalRefresherFactory {

    public static EvalRefresher getEvalRefresher() {
        if (SystemInfo.isWindows) {
            return WindowsEvalRefresher.INSTANCE;
        } else {
            throw new IllegalStateException("No EvalRefresher found");
        }
    }
}
