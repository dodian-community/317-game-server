package net.dodian.packets.handlers.impl;

import net.dodian.events.EventsProvider;
import net.dodian.events.impl.player.interact.item.PlayerItemFirstClickEvent;
import net.dodian.events.impl.player.interact.item.PlayerItemSecondClickEvent;
import net.dodian.events.impl.player.interact.item.PlayerItemThirdClickEvent;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.item.ItemActionPacket;
import org.springframework.stereotype.Component;

@Component
public class ItemActionPacketHandler implements PacketListener {

    private final EventsProvider eventsProvider;

    public ItemActionPacketHandler(EventsProvider eventsProvider) {
        this.eventsProvider = eventsProvider;
    }

    @PacketHandler
    public void onFirstClick(ItemActionPacket packet) {
        if(!ItemActionPacket.Action.FIRST.equals(packet.getAction())) {
            return;
        }

        eventsProvider.executeListeners(PlayerItemFirstClickEvent.class, packet);
    }

    @PacketHandler
    public void onSecondClick(ItemActionPacket packet) {
        if(!ItemActionPacket.Action.SECOND.equals(packet.getAction())) {
            return;
        }

        eventsProvider.executeListeners(PlayerItemSecondClickEvent.class, packet);
    }

    @PacketHandler
    public void onThirdClick(ItemActionPacket packet) {
        if(!ItemActionPacket.Action.THIRD.equals(packet.getAction())) {
            return;
        }

        eventsProvider.executeListeners(PlayerItemThirdClickEvent.class, packet);
    }
}
