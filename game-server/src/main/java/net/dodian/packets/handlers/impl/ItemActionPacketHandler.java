package net.dodian.packets.handlers.impl;

import net.dodian.events.EventsProvider;
import net.dodian.events.impl.player.interact.item.PlayerItemFirstClickEvent;
import net.dodian.events.impl.player.interact.item.PlayerItemSecondClickEvent;
import net.dodian.events.impl.player.interact.item.PlayerItemThirdClickEvent;
import net.dodian.old.world.model.Item;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.item.ItemActionPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemActionPacketHandler implements PacketListener {

    private final EventsProvider eventsProvider;

    @Autowired
    public ItemActionPacketHandler(EventsProvider eventsProvider) {
        this.eventsProvider = eventsProvider;
    }

    @PacketHandler
    public void onFirstClick(ItemActionPacket packet) {
        if(!ItemActionPacket.Action.FIRST.equals(packet.getAction())) {
            return;
        }

        eventsProvider.executeListeners(new PlayerItemFirstClickEvent().create(packet.getPlayer(), new Item(packet.getItemId()).setSlot(packet.getSlot())));
    }

    @PacketHandler
    public void onSecondClick(ItemActionPacket packet) {
        if(!ItemActionPacket.Action.SECOND.equals(packet.getAction())) {
            return;
        }

        eventsProvider.executeListeners(new PlayerItemSecondClickEvent().create(packet.getPlayer(), new Item(packet.getItemId()).setSlot(packet.getSlot())));
    }

    @PacketHandler
    public void onThirdClick(ItemActionPacket packet) {
        if(!ItemActionPacket.Action.THIRD.equals(packet.getAction())) {
            return;
        }

        eventsProvider.executeListeners(new PlayerItemThirdClickEvent().create(packet.getPlayer(), new Item(packet.getItemId()).setSlot(packet.getSlot())));
    }
}
