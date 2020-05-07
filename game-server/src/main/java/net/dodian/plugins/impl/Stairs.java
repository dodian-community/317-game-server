package net.dodian.plugins.impl;

import net.dodian.events.EventHandler;
import net.dodian.events.EventListener;
import net.dodian.events.impl.player.interact.object.PlayerObjectFirstClickEvent;
import net.dodian.events.impl.player.interact.object.PlayerObjectSecondClickEvent;
import net.dodian.old.world.collision.region.RegionClipping;
import org.springframework.stereotype.Component;

@Component
public class Stairs implements EventListener {

    @EventHandler
    public void onFirstClick(PlayerObjectFirstClickEvent event) {
        RegionClipping.removeObject(event.getObject());
    }

    @EventHandler
    public void onSecondClick(PlayerObjectSecondClickEvent event) {
        RegionClipping.removeObject(event.getObject());
    }
}
