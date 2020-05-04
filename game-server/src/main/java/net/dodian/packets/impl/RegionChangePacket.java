package net.dodian.packets.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.CHANGE_MAP_REGION_OPCODE;

@Component
@Opcodes(CHANGE_MAP_REGION_OPCODE)
public class RegionChangePacket extends GamePacket {

    @Override
    public RegionChangePacket createFrom(Packet packet, Player player) {
        this.player = player;
        return this;
    }
}
