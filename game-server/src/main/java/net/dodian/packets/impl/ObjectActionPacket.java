package net.dodian.packets.impl;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.*;

@Component
@Getter
@Opcodes({OBJECT_FIRST_CLICK_OPCODE, OBJECT_SECOND_CLICK_OPCODE,
        OBJECT_THIRD_CLICK_OPCODE, OBJECT_FOURTH_CLICK_OPCODE,
        OBJECT_FIFTH_CLICK_OPCODE})
public class ObjectActionPacket extends GamePacket {

    private int id;
    private Position position;

    @Override
    public ObjectActionPacket createFrom(Packet packet, Player player) {
        this.player = player;
        int x = packet.readLEShortA();
        this.id = packet.readUnsignedShort();
        int y = packet.readUnsignedShortA();
        this.position = new Position(x, y, player.getPosition().getZ());
        return this;
    }
}
