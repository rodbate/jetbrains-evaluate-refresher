package com.github.rodbate.jetbrains.evaluate.refresher.eval.refresher;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Windows Evaluate Refresher
 *
 * @author rodbate
 * @since 2021/3/7
 */
public class MacEvalRefresher extends AbstractEvalRefresher {
    public static final MacEvalRefresher INSTANCE = new MacEvalRefresher();
    private static final String USER_HOME_DIR = System.getProperty("user.home");

    @Override
    protected void doRefreshPlatform() throws Exception {
        deleteUserPreferenceFiles();
        clearPreferencesCache();
    }

    private void deleteUserPreferenceFiles() throws IOException {
        // ~/Library/Preferences/jetbrains.clion.xyz.plist
        // ~/Library/Preferences/com.apple.java.util.prefs.plist
        // ~/Library/Preferences/com.jetbrains.CLion.plist
        final Path prefsDir = Paths.get(USER_HOME_DIR, "Library/Preferences");
        final Set<Path> toDeletePrefFiles = new HashSet<>();
        Files.walkFileTree(prefsDir, EnumSet.noneOf(FileVisitOption.class), 1, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                final String filename = file.getFileName().toString();
                if ("com.apple.java.util.prefs.plist".equals(filename)) {
                    toDeletePrefFiles.add(file);
                }
                if (filename.startsWith("jetbrains." + getProductName()) && filename.endsWith(".plist")) {
                    toDeletePrefFiles.add(file);
                }
                if (("com.jetbrains." + getProductName() + ".plist").equalsIgnoreCase(filename)) {
                    toDeletePrefFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                throw exc;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.TERMINATE;
            }
        });

        toDeletePrefFiles.forEach(f -> {
            try {
                Files.deleteIfExists(f);
            } catch (IOException e) {
                Notifications.Bus.notify(new Notification("MacEvalRefresher", "MacEvalRefresher",
                    "Failed to delete pref file: " + f + ", error: " + e.getMessage(), NotificationType.ERROR));
            }
        });
    }

    private void clearPreferencesCache() throws IOException, InterruptedException {
        execCommands("bash", "-c", "killall cfprefsd");
    }
}
