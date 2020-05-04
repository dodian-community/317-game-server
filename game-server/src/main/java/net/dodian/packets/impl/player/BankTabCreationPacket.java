package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.BANK_TAB_CREATION_OPCODE;

@Component
@Getter
@Opcodes(BANK_TAB_CREATION_OPCODE)
public class BankTabCreationPacket extends GamePacket {

    private int interfaceId;
    private short fromSlot;
    private short toTab;

    @Override
    public BankTabCreationPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.interfaceId = packet.readInt();
        this.fromSlot = packet.readShort();
        this.toTab = packet.readLEShort();
        return this;
    }
}
