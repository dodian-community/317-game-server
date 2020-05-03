package net.dodian.packets.listeners;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public interface PacketListenerBase {
    PacketListenerBase create(Packet packet, Player player);
    void handle();
}
