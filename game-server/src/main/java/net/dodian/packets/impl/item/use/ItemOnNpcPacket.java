package net.dodian.packets.impl.item.use;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.ITEM_ON_NPC_OPCODE;

@Component
@Getter
@Opcodes(ITEM_ON_NPC_OPCODE)
public class ItemOnNpcPacket extends GamePacket {

    private int id;
    private int index;
    private int slot;

    @Override
    public ItemOnNpcPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.id = packet.readShortA();
        this.index = packet.readShortA();
        this.slot = packet.readLEShort();
        return this;
    }
}
