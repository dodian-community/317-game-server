package net.dodian.events.impl.player.interact.object;

import lombok.Getter;
import net.dodian.events.impl.player.PlayerEvent;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PlayerObjectEvent extends PlayerEvent {
    protected GameObject object;

    public PlayerObjectEvent create(Player player, GameObject object) {
        this.player = player;
        this.object = object;
        return this;
    }
}
