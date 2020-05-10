package net.dodian.game.events.impl.player.session;

import lombok.Getter;
import net.dodian.game.events.impl.player.PlayerEvent;
import net.dodian.old.net.PlayerSession;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PlayerConnectEvent extends PlayerEvent {
    protected Object message;
    protected PlayerSession playerSession;

    public PlayerConnectEvent create(PlayerSession playerSession, Object message) {
        this.playerSession = playerSession;
        this.message = message;
        return this;
    }
}
