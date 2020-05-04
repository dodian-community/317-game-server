package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.MAGIC_ON_PLAYER_OPCODE;

@Component
@Getter
@Opcodes(MAGIC_ON_PLAYER_OPCODE)
public class MagicOnPlayerPacket extends GamePacket {

    private int targetIndex;
    private int spellId;

    @Override
    public MagicOnPlayerPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.targetIndex = packet.readShortA();
        this.spellId = packet.readLEShort();
        return this;
    }
}
