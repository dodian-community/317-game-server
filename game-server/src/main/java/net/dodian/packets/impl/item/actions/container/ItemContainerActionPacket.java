package net.dodian.packets.impl.item.actions.container;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ItemContainerActionPacket extends GamePacket {

    private int interfaceId;
    private int slot;
    private int id;

    @Override
    public ItemContainerActionPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.interfaceId = packet.readInt();
        this.slot = packet.readShortA();
        this.id = packet.readShortA();
        return this;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "] interfaceId: " + interfaceId + " | slot: " + slot + " | id: "  + id;
    }
}
