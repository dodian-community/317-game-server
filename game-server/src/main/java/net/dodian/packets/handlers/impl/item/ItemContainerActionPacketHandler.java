package net.dodian.packets.handlers.impl.item;

import net.dodian.events.impl.player.interact.item.container.*;
import net.dodian.old.world.model.Item;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.item.actions.container.*;
import org.springframework.stereotype.Component;

@Component
public class ItemContainerActionPacketHandler extends PacketListener {

    @PacketHandler
    public void onFirst(ItemContainerFirstActionPacket packet) {
        eventsProvider.executeListeners(new PlayerItemContainerFirstClickEvent().create(packet.getPlayer(), new Item(packet.getId()), packet.getInterfaceId(), packet.getSlot()));
    }

    @PacketHandler
    public void onSecond(ItemContainerSecondActionPacket packet) {
        eventsProvider.executeListeners(new PlayerItemContainerSecondClickEvent().create(packet.getPlayer(), new Item(packet.getId()), packet.getInterfaceId(), packet.getSlot()));
    }

    @PacketHandler
    public void onThird(ItemContainerThirdActionPacket packet) {
        eventsProvider.executeListeners(new PlayerItemContainerThirdClickEvent().create(packet.getPlayer(), new Item(packet.getId()), packet.getInterfaceId(), packet.getSlot()));
    }

    @PacketHandler
    public void onFourth(ItemContainerFourthActionPacket packet) {
        eventsProvider.executeListeners(new PlayerItemContainerFourthClickEvent().create(packet.getPlayer(), new Item(packet.getId()), packet.getInterfaceId(), packet.getSlot()));
    }

    @PacketHandler
    public void onFifth(ItemContainerFifthActionPacket packet) {
        eventsProvider.executeListeners(new PlayerItemContainerFifthClickEvent().create(packet.getPlayer(), new Item(packet.getId()), packet.getInterfaceId(), packet.getSlot()));
    }

    @PacketHandler
    public void onSixth(ItemContainerSixthClickPacket packet) {
        eventsProvider.executeListeners(new PlayerItemContainerSixthClickEvent().create(packet.getPlayer(), new Item(packet.getId()), packet.getInterfaceId(), packet.getSlot()));
    }
}
