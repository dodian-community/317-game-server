package net.dodian.packets.impl.npc;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NpcOptionPacket extends GamePacket {

    private int npcIndex;

    @Override
    public NpcOptionPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.npcIndex = packet.readLEShort();
        return this;
    }
}
