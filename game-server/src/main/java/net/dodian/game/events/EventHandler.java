package net.dodian.game.events;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    int value() default 0;
}
