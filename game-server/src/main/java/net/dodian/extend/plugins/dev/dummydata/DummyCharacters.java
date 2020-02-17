package net.dodian.extend.plugins.dev.dummydata;

import net.dodian.extend.events.system.ServerEventListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DummyCharacters implements ServerEventListener {

    @Override
    public void onStartup() {

    }

    @Override
    public void onShutdown() {

    }

    @Override
    public void onStartedUp() {

    }
}
