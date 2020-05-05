package net.dodian.packets.impl.item.use;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.ITEM_ON_ITEM_OPCODE;

@Component
@Getter
@Opcodes(ITEM_ON_ITEM_OPCODE)
public class ItemOnItemPacket extends GamePacket {

    private int usedWithSlot;
    private int itemUsedSlot;

    @Override
    public ItemOnItemPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.usedWithSlot = packet.readUnsignedShort();
        this.itemUsedSlot = packet.readUnsignedShortA();
        return this;
    }
}
