package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.EQUIP_ITEM_OPCODE;

@Getter
@Component
@Opcodes(EQUIP_ITEM_OPCODE)
public class EquipItemPacket extends GamePacket {
    private int id;
    private int slot;
    private int interfaceId;

    @Override
    public EquipItemPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.id = packet.readShort();
        this.slot = packet.readShortA();
        this.interfaceId = packet.readShortA();
        return this;
    }
}
