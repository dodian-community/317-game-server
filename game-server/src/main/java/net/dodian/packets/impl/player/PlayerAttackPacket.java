package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.ATTACK_PLAYER_OPCODE;

@Component
@Getter
@Opcodes(ATTACK_PLAYER_OPCODE)
public class PlayerAttackPacket extends GamePacket {

    private int id;

    @Override
    public PlayerAttackPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.id = packet.readLEShort();
        return this;
    }
}
