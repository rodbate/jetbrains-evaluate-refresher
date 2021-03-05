package com.github.rodbate.jetbrains.evaluate.refresher.eval;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;

import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.io.FileUtil;
import org.jdom.Element;

/**
 * Windows Evaluate Refresher
 *
 * @author rodbate
 * @since 2021-03-05
 */
public class WindowsEvalRefresher implements EvalRefresher {
    public static final WindowsEvalRefresher INSTANCE = new WindowsEvalRefresher();

    @Override
    public void refresh() throws Exception {
        Path configDir = PathManager.getConfigDir();
        File evalDir = configDir.resolve("eval").toFile();
        FileUtil.delete(evalDir);
        deleteOtherConfigEvalAttributes();
        deleteWinRegistry();
    }

    private void deleteOtherConfigEvalAttributes() throws Exception {
        Path otherConfigFile = PathManager.getConfigDir()
            .resolve("options")
            .resolve("other.xml");

        Element root = JDOMUtil.load(new FileInputStream(otherConfigFile.toFile()));
        for (Element child : new ArrayList<>(root.getChildren("component"))) {
            if (child.getAttributeValue("name").equals("PropertiesComponent")) {
                for (Element c : new ArrayList<>(child.getChildren("property"))) {
                    if (c.getAttributeValue("name").startsWith("evlsprt")) {
                        c.detach();
                    }
                }
            }
        }
        JDOMUtil.write(root, otherConfigFile);
    }

    private void deleteWinRegistry() throws IOException, InterruptedException {
        String productName = ApplicationNamesInfo.getInstance()
            .getProductName()
            .toLowerCase();

        Process process = new ProcessBuilder()
            .command(
                "reg",
                "delete",
                String.format("\"HKEY_CURRENT_USER\\Software\\JavaSoft\\Prefs\\jetbrains\\%s\"", productName),
                "/f")
            .redirectErrorStream(true)
            .start();

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            return;
        }

        String errorMsg = FileUtil.loadTextAndClose(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()));
        throw new RuntimeException("Delete windows registry error: " + errorMsg);
    }
}
