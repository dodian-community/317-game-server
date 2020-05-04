package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.UPDATE_PLANE_OPCODE;

@Component
@Getter
@Opcodes(UPDATE_PLANE_OPCODE)
public class HeightCheckPacket extends GamePacket {

    private int plane;

    @Override
    public HeightCheckPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.plane = packet.readByte();
        return this;
    }
}
