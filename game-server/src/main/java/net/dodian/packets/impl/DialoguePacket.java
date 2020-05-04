package net.dodian.packets.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.DIALOGUE_OPCODE;

@Component
@Opcodes(DIALOGUE_OPCODE)
public class DialoguePacket extends GamePacket {

    @Override
    public DialoguePacket createFrom(Packet packet, Player player) {
        this.player = player;
        return this;
    }
}
