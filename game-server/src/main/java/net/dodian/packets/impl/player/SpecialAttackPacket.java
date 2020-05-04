package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.SPECIAL_ATTACK_OPCODE;

@Component
@Getter
@Opcodes(SPECIAL_ATTACK_OPCODE)
public class SpecialAttackPacket extends GamePacket {

    private int specBar;

    @Override
    public SpecialAttackPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.specBar = packet.readInt();
        return this;
    }
}
