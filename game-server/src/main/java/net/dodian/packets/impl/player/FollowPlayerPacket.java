package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.FOLLOW_PLAYER_OPCODE;

@Component
@Getter
@Opcodes(FOLLOW_PLAYER_OPCODE)
public class FollowPlayerPacket extends GamePacket {

    private int targetIndex;

    @Override
    public FollowPlayerPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.targetIndex = packet.readLEShort();
        return this;
    }
}
