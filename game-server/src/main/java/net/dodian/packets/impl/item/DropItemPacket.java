package net.dodian.packets.impl.item;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.DROP_ITEM_OPCODE;

@Component
@Getter
@Opcodes(DROP_ITEM_OPCODE)
public class DropItemPacket extends GamePacket {

    private int id;
    private int interfaceId;
    private int itemSlot;

    @Override
    public DropItemPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.id = packet.readUnsignedShortA();
        this.interfaceId = packet.readUnsignedShort();
        this.itemSlot = packet.readUnsignedShortA();
        return this;
    }
}
