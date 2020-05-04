package net.dodian.packets.impl.player;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.PLAYER_INACTIVE_OPCODE;

@Component
@Opcodes(PLAYER_INACTIVE_OPCODE)
public class PlayerInactivePacket extends GamePacket {

    @Override
    public PlayerInactivePacket createFrom(Packet packet, Player player) {
        this.player = player;
        return this;
    }
}
