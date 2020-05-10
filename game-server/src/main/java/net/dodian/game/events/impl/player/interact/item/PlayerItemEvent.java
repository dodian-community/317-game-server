package net.dodian.game.events.impl.player.interact.item;

import lombok.Getter;
import net.dodian.game.events.impl.player.PlayerEvent;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import org.springframework.stereotype.Component;

@Component
@Getter
public abstract class PlayerItemEvent extends PlayerEvent {
    protected Item item;

    public PlayerItemEvent create(Player player, Item item) {
        this.player = player;
        this.item = item;
        return this;
    }
}
