package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.*;

@Component
@Getter
@Opcodes({PLAYER_OPTION_1_OPCODE, PLAYER_OPTION_2_OPCODE,
        PLAYER_OPTION_3_OPCODE})
public class PlayerOptionPacket  extends GamePacket {

    private int id;

    @Override
    public PlayerOptionPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.id = packet.readShort() & 0xFFFF;
        return this;
    }
}
