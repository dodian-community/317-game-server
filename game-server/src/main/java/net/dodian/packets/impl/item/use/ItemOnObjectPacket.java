package net.dodian.packets.impl.item.use;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.ITEM_ON_OBJECT_OPCODE;

@Component
@Getter
@Opcodes(ITEM_ON_OBJECT_OPCODE)
public class ItemOnObjectPacket extends GamePacket {

    private int interfaceType;
    private GameObject gameObject;
    private int itemSlot;
    private int itemId;

    @Override
    public ItemOnObjectPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.interfaceType = packet.readShort();
        int objectId = packet.readShort();
        int objectY = packet.readLEShortA();
        this.itemSlot = packet.readLEShort();
        int objectX = packet.readLEShortA();
        this.itemId = packet.readShort();
        this.gameObject = new GameObject(objectId, new Position(objectX, objectY, player.getPosition().getZ()));
        return this;
    }
}
