package net.dodian.game.events.impl.player.interact;

import lombok.Getter;
import net.dodian.game.events.impl.player.PlayerEvent;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PlayerButtonClickEvent extends PlayerEvent {
    private int button;

    public PlayerButtonClickEvent create(Player player, int button) {
        this.player = player;
        this.button = button;
        return this;
    }
}
