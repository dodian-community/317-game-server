package net.dodian.packets.handlers.impl;

import net.dodian.events.impl.player.interact.item.PlayerItemFirstClickEvent;
import net.dodian.events.impl.player.interact.item.PlayerItemSecondClickEvent;
import net.dodian.events.impl.player.interact.item.PlayerItemThirdClickEvent;
import net.dodian.old.world.model.Item;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.item.actions.ItemActionPacket;
import net.dodian.packets.impl.item.actions.ItemFirstActionPacket;
import net.dodian.packets.impl.item.actions.ItemSecondActionPacket;
import net.dodian.packets.impl.item.actions.ItemThirdActionPacket;
import org.springframework.stereotype.Component;

@Component
public class ItemActionPacketHandler extends PacketListener {

    public boolean checkItem(ItemActionPacket packet) {
        if(packet.getSlot() < 0 || packet.getSlot() > packet.getPlayer().getInventory().capacity()) {
            return false;
        }

        if(packet.getPlayer().getInventory().getItems()[packet.getSlot()].getId() != packet.getItemId()) {
            return false;
        }

        return true;
    }

    @PacketHandler
    public void onFirstClick(ItemFirstActionPacket packet) {
        if (!checkItem(packet)) {
            return;
        }

        eventsProvider.executeListeners(new PlayerItemFirstClickEvent().create(packet.getPlayer(), new Item(packet.getItemId()).setSlot(packet.getSlot())));
    }

    @PacketHandler
    public void onSecondClick(ItemSecondActionPacket packet) {
        if (!checkItem(packet)) {
            return;
        }

        eventsProvider.executeListeners(new PlayerItemSecondClickEvent().create(packet.getPlayer(), new Item(packet.getItemId()).setSlot(packet.getSlot())));
    }

    @PacketHandler
    public void onThirdClick(ItemThirdActionPacket packet) {
        if (!checkItem(packet)) {
            return;
        }

        eventsProvider.executeListeners(new PlayerItemThirdClickEvent().create(packet.getPlayer(), new Item(packet.getItemId()).setSlot(packet.getSlot())));
    }
}
