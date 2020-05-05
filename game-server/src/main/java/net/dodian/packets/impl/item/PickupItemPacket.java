package net.dodian.packets.impl.item;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.PICKUP_ITEM_OPCODE;

@Component
@Getter
@Opcodes(PICKUP_ITEM_OPCODE)
public class PickupItemPacket extends GamePacket {

    private int id;
    private Position position;

    @Override
    public PickupItemPacket createFrom(Packet packet, Player player) {
        this.player = player;
        int y = packet.readLEShort();
        this.id = packet.readShortA();
        int x = packet.readLEShort();
        this.position = new Position(x, y, player.getPosition().getZ());
        return this;
    }
}
