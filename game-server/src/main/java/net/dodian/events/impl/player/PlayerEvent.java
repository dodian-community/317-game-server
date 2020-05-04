package net.dodian.events.impl.player;

import lombok.Getter;
import lombok.Setter;
import net.dodian.events.GameEvent;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public abstract class PlayerEvent extends GameEvent {
    public Player player;

    public GameEvent create(Player player) {
        this.player = player;
        return this;
    }
}
