package com.github.rodbate.jetbrains.evaluate.refresher.eval.refresher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.io.FileUtil;
import org.jdom.Element;

/**
 * @author rodbate
 * @since 2021/3/7
 */
public abstract class AbstractEvalRefresher implements EvalRefresher {

    @Override
    public void refresh() throws Exception {
        Path configDir = getConfigDir();
        File evalDir = configDir.resolve("eval").toFile();
        FileUtil.delete(evalDir);
        deleteOtherConfigEvalAttributes();
        doRefreshPlatform();
    }

    private void deleteOtherConfigEvalAttributes() throws Exception {
        Path otherConfigFile = getConfigDir().resolve("options").resolve("other.xml");

        Element root = JDOMUtil.load(new FileInputStream(otherConfigFile.toFile()));
        for (Element child : new ArrayList<>(root.getChildren("component"))) {
            if (child.getAttributeValue("name").equals("PropertiesComponent")) {
                for (Element c : new ArrayList<>(child.getChildren("property"))) {
                    if (c.getAttributeValue("name")
                        .startsWith("evlsprt")) {
                        c.detach();
                    }
                }
            }
        }
        JDOMUtil.write(root, otherConfigFile);
    }

    private Path getConfigDir() {
        return Paths.get(PathManager.getConfigPath());
    }

    protected String getProductName() {
        return ApplicationNamesInfo.getInstance()
            .getProductName()
            .toLowerCase();
    }

    protected void execCommands(String... commands) throws IOException, InterruptedException {
        Process process = new ProcessBuilder()
            .command(commands)
            .redirectErrorStream(true)
            .start();

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            return;
        }

        String errorMsg = FileUtil.loadTextAndClose(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()));
        throw new RuntimeException(String.format("Failed to exec commands[%s], errorMsg: %s",
            Arrays.toString(commands), errorMsg));
    }

    protected void doRefreshPlatform() throws Exception {
    }
}
