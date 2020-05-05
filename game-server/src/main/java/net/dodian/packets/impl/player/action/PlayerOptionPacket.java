package net.dodian.packets.impl.player.action;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PlayerOptionPacket extends GamePacket {

    private int id;

    @Override
    public PlayerOptionPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.id = packet.readShort() & 0xFFFF;
        return this;
    }
}
