package net.dodian.game.events.impl.player.interact.npc;

import net.dodian.game.events.GameEvent;
import net.dodian.game.events.impl.player.PlayerEvent;
import org.springframework.stereotype.Component;

@Component
public class PlayerNpcKillEvent extends PlayerEvent {
    public GameEvent create() {
        return null;
    }
}
