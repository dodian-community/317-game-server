package net.dodian.packets.impl.item.use;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.ITEM_ON_PLAYER_OPCODE;

@Component
@Getter
@Opcodes(ITEM_ON_PLAYER_OPCODE)
public class ItemOnPlayerPacket extends GamePacket {

    private int interfaceId;
    private int targetIndex;
    private int itemId;
    private int slot;

    @Override
    public ItemOnPlayerPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.interfaceId = packet.readUnsignedShortA();
        this.targetIndex = packet.readUnsignedShort();
        this.itemId = packet.readUnsignedShort();
        this.slot = packet.readLEShort();
        return this;
    }
}
