package net.dodian.extend.events.system;

import net.dodian.extend.events.EventListener;
import org.springframework.stereotype.Component;

@Component
public interface ServerEventListener extends EventListener {
    void onStartup();
    void onShutdown();
    void onStartedUp();
}
