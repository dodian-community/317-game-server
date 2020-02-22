package net.dodian.old.net.packet.impl;

import net.dodian.extend.events.items.ItemActionEventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemActionPacketListener implements PacketListener {

    private final List<ItemActionEventListener> itemActionEventListeners;

    @Autowired
    public ItemActionPacketListener(List<ItemActionEventListener> itemActionEventListeners) {
        this.itemActionEventListeners = itemActionEventListeners;
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player == null || player.getHitpoints() <= 0) {
            return;
        }

        int itemId = packet.readShort();
        int slot = packet.readShort();

        if (slot < 0 || slot > player.getInventory().capacity()) {
            return;
        }

        if (player.getInventory().getItems()[slot].getId() != itemId) {
            return;
        }

        switch (packet.getOpcode()) {
            case PacketConstants.FIRST_ITEM_ACTION_OPCODE:
                itemActionEventListeners.forEach(event -> event.onFirstClick(player, packet));
                break;
            case PacketConstants.SECOND_ITEM_ACTION_OPCODE:
                itemActionEventListeners.forEach(event -> event.onSecondClick(player, packet));
                break;
            case PacketConstants.THIRD_ITEM_ACTION_OPCODE:
                itemActionEventListeners.forEach(event -> event.onThirdClick(player, packet));
                break;
        }
    }
}