package net.dodian.extend.plugins.dev.dummydata;

import net.dodian.extend.events.system.ServerEventListener;
import net.dodian.managers.DefinitionsManager;
import net.dodian.tools.JsonToSQLDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DummyDefinitions implements ServerEventListener {

    private final DefinitionsManager definitionsManager;
    private final JsonToSQLDefinitions jsonToSQLDefinitions;

    @Autowired
    public DummyDefinitions(DefinitionsManager definitionsManager, JsonToSQLDefinitions jsonToSQLDefinitions) {
        this.definitionsManager = definitionsManager;
        this.jsonToSQLDefinitions = jsonToSQLDefinitions;
    }

    @Override
    public void onStartedUp() {
        definitionsManager.loadItemDefinitions();
    }

    @Override
    public void onStartup() {
        jsonToSQLDefinitions.migrate();
    }

    @Override
    public void onShutdown() {

    }
}
