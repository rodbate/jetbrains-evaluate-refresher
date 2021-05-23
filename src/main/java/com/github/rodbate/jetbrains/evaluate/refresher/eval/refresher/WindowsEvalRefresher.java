package com.github.rodbate.jetbrains.evaluate.refresher.eval.refresher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.util.io.FileUtil;

/**
 * Windows Evaluate Refresher
 *
 * @author rodbate
 * @since 2021-03-05
 */
public class WindowsEvalRefresher extends AbstractEvalRefresher {
    public static final WindowsEvalRefresher INSTANCE = new WindowsEvalRefresher();

    @Override
    protected void doRefreshPlatform() throws Exception {
        deleteWinRegistry();
    }

    private void deleteWinRegistry() throws IOException, InterruptedException {
        execCommands(
            "cmd.exe",
            "/c",
            "reg",
            "delete",
            String.format("\"HKEY_CURRENT_USER\\Software\\JavaSoft\\Prefs\\jetbrains\\%s\"", getProductName()),
            "/f"
        );
    }
}
