package net.dodian.extend.plugins.dev.dummydata;

import net.dodian.extend.events.system.ServerEventListener;
import net.dodian.managers.DefinitionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DummyDefinitions implements ServerEventListener {

    private final DefinitionsManager definitionsManager;

    @Autowired
    public DummyDefinitions(DefinitionsManager definitionsManager) {
        this.definitionsManager = definitionsManager;
    }

    @Override
    public void onStartedUp() {
        definitionsManager.loadItemDefinitions();
    }

    @Override
    public void onStartup() {

    }

    @Override
    public void onShutdown() {

    }
}
