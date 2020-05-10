package net.dodian.game.events;

import net.dodian.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

@Component
public class EventsProvider {

    private final List<EventListener> eventListeners;

    @Autowired
    public EventsProvider(List<EventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public void executeListeners(GameEvent event) {
        this.executeListeners(event, Void.class);
    }

    public <R> Optional<R> executeListeners(GameEvent event, Class<R> returnType) {
        for (EventListener listener : eventListeners) {
            Optional<Method> optionalEventHandler = Arrays.stream(listener.getClass().getMethods())
                    .filter(m -> m.isAnnotationPresent(EventHandler.class))
                    .filter(m -> m.getParameterTypes()[0].equals(event.getClass()))
                    .findFirst();

            if (optionalEventHandler.isPresent() && !event.isCancelled()) {
                try {
                    if (!returnType.equals(Void.class) && optionalEventHandler.get().getReturnType().equals(returnType)) {
                        return Optional.of(returnType.cast(optionalEventHandler.get().invoke(listener, event)));
                    } else {
                        optionalEventHandler.get().invoke(listener, event);
                        if (!returnType.equals(Void.class)) {
                            Server.getLogger().log(Level.INFO, "Invoked " + optionalEventHandler.get().getName()
                                    + " with event: " + event.getClass().getSimpleName() + " without returning.");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        event.setCancelled(false);

        return Optional.empty();
    }
}
