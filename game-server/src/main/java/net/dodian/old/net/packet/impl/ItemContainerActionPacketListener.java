package net.dodian.old.net.packet.impl;

import net.dodian.extend.events.items.ItemContainerActionEventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ItemContainerActionPacketListener implements PacketListener {

    private final List<ItemContainerActionEventListener> itemContainerActionEventListeners;

    @Autowired
    public ItemContainerActionPacketListener(Optional<List<ItemContainerActionEventListener>> itemContainerActionEventListeners) {
        this.itemContainerActionEventListeners = itemContainerActionEventListeners.orElse(new ArrayList<>());
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        int interfaceId = packet.readInt();
        int slot = packet.readShortA();
        int id = packet.readShortA();

        if (player == null || player.getHitpoints() <= 0) {
            return;
        }

        switch (packet.getOpcode()) {
            case PacketConstants.FIRST_ITEM_CONTAINER_ACTION_OPCODE:
                itemContainerActionEventListeners.forEach(eventListener -> eventListener
                        .onFirstAction(player, interfaceId, slot, id));
                break;
            case PacketConstants.SECOND_ITEM_CONTAINER_ACTION_OPCODE:
                itemContainerActionEventListeners.forEach(eventListener -> eventListener
                        .onSecondAction(player, interfaceId, slot, id));
                break;
            case PacketConstants.THIRD_ITEM_CONTAINER_ACTION_OPCODE:
                itemContainerActionEventListeners.forEach(eventListener -> eventListener
                        .onThirdAction(player, interfaceId, slot, id));
                break;
            case PacketConstants.FOURTH_ITEM_CONTAINER_ACTION_OPCODE:
                itemContainerActionEventListeners.forEach(eventListener -> eventListener
                        .onFourthAction(player, interfaceId, slot, id));
                break;
            case PacketConstants.FIFTH_ITEM_CONTAINER_ACTION_OPCODE:
                itemContainerActionEventListeners.forEach(eventListener -> eventListener
                        .onFifthAction(player, interfaceId, slot, id));
                break;
            case PacketConstants.SIXTH_ITEM_CONTAINER_ACTION_OPCODE:
                itemContainerActionEventListeners.forEach(eventListener -> eventListener
                        .onSixthAction(player, interfaceId, slot, id));
                break;
        }
    }
}
