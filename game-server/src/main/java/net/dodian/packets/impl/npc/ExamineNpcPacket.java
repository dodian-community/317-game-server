package net.dodian.packets.impl.npc;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.EXAMINE_NPC_OPCODE;

@Component
@Getter
@Opcodes(EXAMINE_NPC_OPCODE)
public class ExamineNpcPacket extends GamePacket {

    private short id;

    @Override
    public ExamineNpcPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.id = packet.readShort();
        return this;
    }
}
