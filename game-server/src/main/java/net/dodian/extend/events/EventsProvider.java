package net.dodian.extend.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EventsProvider {

    private final List<EventListener> eventListeners;

    @Autowired
    public EventsProvider(List<EventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public void registerEvents() {


        eventListeners.forEach(listener -> {
            Arrays.asList(listener.getClass().getSuperclass().getMethods()).forEach(method -> {
                if(method.isAnnotationPresent(EventHandler.class)) {
                    Arrays.asList(method.getParameterTypes()).forEach(parameterType -> {
                        //parameterType.
                    });
                }
            });
        });
    }
}
