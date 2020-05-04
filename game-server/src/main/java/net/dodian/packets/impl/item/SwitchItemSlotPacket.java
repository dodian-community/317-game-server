package net.dodian.packets.impl.item;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.SWITCH_ITEM_SLOT_OPCODE;

@Component
@Getter
@Opcodes(SWITCH_ITEM_SLOT_OPCODE)
public class SwitchItemSlotPacket extends GamePacket {

    private int interfaceId;
    private int fromSlot;
    private int toSlot;

    @Override
    public SwitchItemSlotPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.interfaceId = packet.readInt();
        packet.readByteC();
        this.fromSlot = packet.readLEShortA();
        this.toSlot = packet.readLEShort();
        return this;
    }
}
