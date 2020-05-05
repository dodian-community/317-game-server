package net.dodian.packets.impl.npc;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.ATTACK_NPC_OPCODE;

@Component
@Getter
@Opcodes(ATTACK_NPC_OPCODE)
public class MeleeAttackNpcPacket extends GamePacket {
    public int npcIndex;

    @Override
    public MeleeAttackNpcPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.npcIndex = packet.readShortA();
        return this;
    }
}
