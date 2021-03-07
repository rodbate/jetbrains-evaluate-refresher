package com.github.rodbate.jetbrains.evaluate.refresher.eval.refresher;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.io.FileUtil;
import org.jdom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author rodbate
 * @since 2021/3/7
 */
public abstract class AbstractEvalRefresher implements EvalRefresher {

    @Override
    public void refresh() throws Exception {
        Path configDir = PathManager.getConfigDir();
        File evalDir = configDir.resolve("eval").toFile();
        FileUtil.delete(evalDir);
        deleteOtherConfigEvalAttributes();
        doRefreshPlatform();
    }

    private void deleteOtherConfigEvalAttributes() throws Exception {
        Path otherConfigFile = PathManager.getConfigDir().resolve("options").resolve("other.xml");

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

    protected void doRefreshPlatform() throws Exception {
    }
}
