package net.dodian.packets.impl.player;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.FINALIZED_MAP_REGION_OPCODE;

@Component
@Opcodes(FINALIZED_MAP_REGION_OPCODE)
public class FinalizedMapRegionChangePacket extends GamePacket {

    @Override
    public FinalizedMapRegionChangePacket createFrom(Packet packet, Player player) {
        this.player = player;
        return this;
    }
}
