package net.dodian.events.impl.player.interact.npc;

import net.dodian.events.GameEvent;
import net.dodian.events.impl.player.PlayerEvent;
import org.springframework.stereotype.Component;

@Component
public class PlayerNpcKillEvent extends PlayerEvent {
    public GameEvent create() {
        return null;
    }
}
