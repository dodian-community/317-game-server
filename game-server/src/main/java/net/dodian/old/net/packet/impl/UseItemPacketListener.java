package net.dodian.old.net.packet.impl;

import net.dodian.extend.events.items.ItemUseEventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.World;
import net.dodian.old.world.collision.region.RegionClipping;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UseItemPacketListener implements PacketListener {

    private final List<ItemUseEventListener> itemUseEventListeners;

    @Autowired
    public UseItemPacketListener(List<ItemUseEventListener> itemUseEventListeners) {
        this.itemUseEventListeners = itemUseEventListeners;
    }

    private void itemOnItem(Player player, Packet packet) {
        int usedWithSlot = packet.readUnsignedShort();
        int itemUsedSlot = packet.readUnsignedShortA();

        if (usedWithSlot < 0 || itemUsedSlot < 0
                || itemUsedSlot > player.getInventory().capacity()
                || usedWithSlot > player.getInventory().capacity()) {
            return;
        }

        Item used = player.getInventory().getItems()[itemUsedSlot];
        Item usedWith = player.getInventory().getItems()[usedWithSlot];

        itemUseEventListeners.forEach(event -> event.itemOnItem(player, used, usedWith));
    }

    private void itemOnNpc(final Player player, Packet packet) {
        final int id = packet.readShortA();
        final int index = packet.readShortA();
        final int slot = packet.readLEShort();

        if (index < 0 || index > World.getNpcs().capacity()) {
            return;
        }

        if (slot < 0 || slot > player.getInventory().getItems().length) {
            return;
        }

        NPC npc = World.getNpcs().get(index);
        if (npc == null) {
            return;
        }

        if (player.getInventory().getItems()[slot].getId() != id) {
            return;
        }

        Item item = player.getInventory().getItems()[slot];

        itemUseEventListeners.forEach(event -> event.itemOnNpc(player, item, npc));
    }

    private void itemOnObject(Player player, Packet packet) {
        int interfaceType = packet.readShort();
        final int objectId = packet.readShort();
        final int objectY = packet.readLEShortA();
        final int itemSlot = packet.readLEShort();
        final int objectX = packet.readLEShortA();
        final int itemId = packet.readShort();

        if (itemSlot < 0 || itemSlot > player.getInventory().capacity()) {
            return;
        }

        final Item item = player.getInventory().getItems()[itemSlot];
        if (item == null) {
            return;
        }

        final GameObject gameObject = new GameObject(objectId, new Position(objectX, objectY, player.getPosition().getZ()));
        if (!RegionClipping.objectExists(gameObject)) {
            //	player.getPacketSender().sendMessage("An error occured. Error code: "+id).sendMessage("Please report the error to a staff member.");
            return;
        }

        itemUseEventListeners.forEach(event -> event.itemOnObject(player, item, gameObject));
    }

    private void itemOnPlayer(Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShortA();
        int targetIndex = packet.readUnsignedShort();
        int itemId = packet.readUnsignedShort();
        int slot = packet.readLEShort();

        if (slot < 0 || slot > player.getInventory().capacity() || targetIndex > World.getPlayers().capacity()) {
            return;
        }

        Player target = World.getPlayers().get(targetIndex);
        if (target == null) {
            return;
        }

        if (player.getInventory().getItems()[slot].getId() != itemId) {
            return;
        }
        Item item = player.getInventory().getItems()[slot];

        itemUseEventListeners.forEach(event -> event.itemOnPlayer(player, item, target));
    }


    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHitpoints() <= 0) {
            return;
        }

        switch (packet.getOpcode()) {
            case PacketConstants.ITEM_ON_ITEM:
                itemOnItem(player, packet);
                break;
            case PacketConstants.ITEM_ON_OBJECT:
                itemOnObject(player, packet);
                break;
            case PacketConstants.ITEM_ON_GROUND_ITEM:
                // TODO
                break;
            case PacketConstants.ITEM_ON_NPC:
                itemOnNpc(player, packet);
                break;
            case PacketConstants.ITEM_ON_PLAYER:
                itemOnPlayer(player, packet);
                break;
        }
    }
}