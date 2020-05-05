package net.dodian.packets.impl.object;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.packets.GamePacket;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ObjectActionPacket extends GamePacket {

    private int id;
    private Position position;

    @Override
    public ObjectActionPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.id = packet.readUnsignedShort();
        int y = packet.readLEShortA();
        int x = packet.readUnsignedShortA();
        this.position = new Position(x, y, player.getPosition().getZ());
        return this;
    }
}
