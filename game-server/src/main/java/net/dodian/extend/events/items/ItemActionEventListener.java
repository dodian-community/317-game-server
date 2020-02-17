package net.dodian.extend.events.items;

import net.dodian.extend.events.EventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public interface ItemActionEventListener extends EventListener {
    void onFirstClick(Player player, Packet packet);
    void onSecondClick(Player player, Packet packet);
    void onThirdClick(Player player, Packet packet);
}
