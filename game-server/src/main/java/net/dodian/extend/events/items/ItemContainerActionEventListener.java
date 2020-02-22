package net.dodian.extend.events.items;

import net.dodian.extend.events.EventListener;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public interface ItemContainerActionEventListener extends EventListener {
    void onFirstAction(Player player, int interfaceId, int slot, int itemId);
    void onSecondAction(Player player, int interfaceId, int slot, int itemId);
    void onThirdAction(Player player, int interfaceId, int slot, int itemId);
    void onFourthAction(Player player, int interfaceId, int slot, int itemId);
    void onFifthAction(Player player, int interfaceId, int slot, int itemId);
    default void onSixthAction(Player player, int interfaceId, int slot, int itemId) {}
}
