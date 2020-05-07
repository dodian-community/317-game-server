package net.dodian.events.impl.player.interact.item.container;

import lombok.Getter;
import net.dodian.events.impl.player.interact.item.PlayerItemEvent;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import org.springframework.stereotype.Component;

@Component
@Getter
public abstract class PlayerItemContainerEvent extends PlayerItemEvent {

    private int interfaceId;
    private int slot;

    public PlayerItemContainerEvent create(Player player, Item item, int interfaceId, int slot) {
        this.player = player;
        this.item = item;
        this.interfaceId = interfaceId;
        this.slot = slot;
        return this;
    }
}
