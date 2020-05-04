package net.dodian.packets.impl.npc;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.MAGIC_NPC_OPCODE;

@Component
@Getter
@Opcodes(MAGIC_NPC_OPCODE)
public class MagicAttackNpcPacket extends GamePacket {
    private int npcIndex;
    private int spellId;

    @Override
    public MagicAttackNpcPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.npcIndex = packet.readLEShortA();
        this.spellId = packet.readShortA();
        return this;
    }
}
