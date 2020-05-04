package net.dodian.packets.impl.npc;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.*;

@Component
@Getter
@Opcodes({FIRST_NPC_CLICK_OPCODE, SECOND_NPC_CLICK_OPCODE,
        THIRD_NPC_CLICK_OPCODE, FOURTH_NPC_CLICK_OPCODE})
public class NpcOptionPacket extends GamePacket {

    private int npcIndex;

    @Override
    public NpcOptionPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.npcIndex = packet.readLEShort();
        return this;
    }
}
