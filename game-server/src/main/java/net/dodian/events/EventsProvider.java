package net.dodian.events;

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

    private final List<GameEvent> gameEvents;
    private final List<EventListener> eventListeners;

    @Autowired
    public EventsProvider(List<GameEvent> gameEvents, List<EventListener> eventListeners) {
        this.gameEvents = gameEvents;
        this.eventListeners = eventListeners;
    }

    public <T extends GameEvent> void executeListeners(Class<T> eventType, Object... eventArguments) {
        this.executeListeners(eventType, Void.class, eventArguments);
    }

    public <R, T extends GameEvent> Optional<R> executeListeners(Class<T> eventType, Class<R> returnType, Object... eventArguments) {
        Optional<? extends GameEvent> optionalGameEvent = gameEvents.stream()
                .filter(event -> event.getClass().equals(eventType))
                .findFirst();

        if(optionalGameEvent.isEmpty()) {
            Server.getLogger().log(Level.INFO, "Couldn't find the event: " + eventType.getSimpleName());
            return Optional.empty();
        }

        GameEvent gameEvent = optionalGameEvent.get();
        Optional<Method> optionalCreateMethod = Arrays.stream(gameEvent.getClass().getMethods())
                .filter(method -> method.getName().equalsIgnoreCase("create"))
                .findFirst();

        T createdEvent = null;
        if(optionalCreateMethod.isPresent()) {
            try {
                createdEvent = eventType.cast(optionalCreateMethod.get().invoke(gameEvent, eventArguments));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                createdEvent = eventType.cast(gameEvent.getClass().getConstructor().newInstance());
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(createdEvent == null) {
            return Optional.empty();
        }

        for(EventListener listener : eventListeners) {
            Optional<Method> optionalMethod = Arrays.stream(listener.getClass().getMethods())
                    .filter(m -> m.isAnnotationPresent(EventHandler.class))
                    .filter(m -> m.getParameterTypes()[0].equals(eventType))
                    .findFirst();

            if(optionalMethod.isPresent() && !createdEvent.isCancelled()) {
                try {
                    if(!returnType.equals(Void.class) && optionalMethod.get().getReturnType().equals(returnType)) {
                        return Optional.of(returnType.cast(optionalMethod.get().invoke(listener, createdEvent)));
                    } else {
                        optionalMethod.get().invoke(listener, createdEvent);
                        if(!returnType.equals(Void.class)) {
                            Server.getLogger().log(Level.INFO, "Invoked " + optionalMethod.get().getName() + " with event: " + createdEvent.getClass().getSimpleName() + " without returning.");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        createdEvent.setCancelled(false);

        return Optional.empty();
    }
}
