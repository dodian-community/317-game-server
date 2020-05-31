package net.dodian.game.events.impl.player.interact.item;

import lombok.Getter;
import net.dodian.game.events.impl.player.PlayerEvent;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import org.springframework.stereotype.Component;

@Component
@Getter
public abstract class PlayerItemOnItemEvent extends PlayerEvent {
    protected Item used;
    protected Item usedOn;

    public PlayerItemOnItemEvent create(Player player, int usedSlot, int usedOnSlot) {
        this.player = player;
        this.used = player.getInventory().get(usedSlot);
        this.usedOn = player.getInventory().get(usedOnSlot);
        return this;
    }
}
