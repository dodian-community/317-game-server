package net.dodian.plugins.impl.dummydata;

import net.dodian.events.EventHandler;
import net.dodian.events.EventListener;
import net.dodian.events.impl.server.ServerStartedUpEvent;
import net.dodian.events.impl.server.ServerStartingUpEvent;
import net.dodian.managers.DefinitionsManager;
import net.dodian.tools.JsonToSQLDefinitions;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DummyDefinitionsPlugin implements EventListener {


    private final DefinitionsManager definitionsManager;
    private final JsonToSQLDefinitions jsonToSQLDefinitions;

    public DummyDefinitionsPlugin(DefinitionsManager definitionsManager, JsonToSQLDefinitions jsonToSQLDefinitions) {
        this.definitionsManager = definitionsManager;
        this.jsonToSQLDefinitions = jsonToSQLDefinitions;
    }

    @EventHandler
    public void onServerStarting(ServerStartingUpEvent event) {
        jsonToSQLDefinitions.migrate();
    }

    @EventHandler
    public void onServerStarted(ServerStartedUpEvent event) {
        definitionsManager.loadItemDefinitions();
    }
}
