package net.dodian.game.events.impl.player.interact.object;

import lombok.Getter;
import net.dodian.game.events.impl.player.PlayerEvent;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PlayerItemUsageEvent extends PlayerEvent {
    protected Item item;
    protected GameObject object;

    public PlayerItemUsageEvent create(Player player, Item item, GameObject object) {
        this.player = player;
        this.item = item;
        this.object = object;
        return this;
    }
}
