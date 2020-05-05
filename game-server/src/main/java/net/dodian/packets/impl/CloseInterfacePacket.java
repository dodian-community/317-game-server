package net.dodian.packets.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.CLOSE_INTERFACE_OPCODE;

@Component
@Opcodes(CLOSE_INTERFACE_OPCODE)
public class CloseInterfacePacket extends GamePacket {

    @Override
    public CloseInterfacePacket createFrom(Packet packet, Player player) {
        this.player = player;
        return this;
    }
}
