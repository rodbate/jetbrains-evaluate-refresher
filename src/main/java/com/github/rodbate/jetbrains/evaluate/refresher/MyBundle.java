package com.github.rodbate.jetbrains.evaluate.refresher;

import java.util.function.Supplier;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class MyBundle extends AbstractBundle {
    private static final String MY_BUNDLE = "messages.MyBundle";

    private static final MyBundle INSTANCE = new MyBundle();

    private MyBundle() {
        super(MY_BUNDLE);
    }

    public static String message(@NotNull @PropertyKey(resourceBundle = MY_BUNDLE) String key,
                                 Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

    @NotNull
    public static Supplier<String> messagePointer(@NotNull @PropertyKey(resourceBundle = MY_BUNDLE) String key,
                                                  Object @NotNull ... params) {
        return INSTANCE.getLazyMessage(key, params);
    }
}
