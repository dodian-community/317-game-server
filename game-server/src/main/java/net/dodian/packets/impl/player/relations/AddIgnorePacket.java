package net.dodian.packets.impl.player.relations;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.ADD_IGNORE_OPCODE;

@Component
@Getter
@Opcodes(ADD_IGNORE_OPCODE)
public class AddIgnorePacket extends GamePacket {

    private long username;

    @Override
    public AddIgnorePacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.username = packet.readLong();
        return this;
    }
}
