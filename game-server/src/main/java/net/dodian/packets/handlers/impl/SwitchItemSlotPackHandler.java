package net.dodian.packets.handlers.impl;

import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.item.SwitchItemSlotPacket;
import org.springframework.stereotype.Component;

@Component
public class SwitchItemSlotPackHandler extends PacketListener {
    @PacketHandler
    public void onSwitchItemSlot(SwitchItemSlotPacket packet) {
        Player player = packet.getPlayer();

        if(player == null || player.getHitpoints() <= 0) {
            return;
        }

        player.getPacketSender().sendInterfaceRemoval();

        if(packet.getFromSlot() >= 0 && packet.getFromSlot() < player.getInventory().capacity() && packet.getToSlot() >= 0 && packet.getToSlot() < player.getInventory().capacity()  && packet.getToSlot() != packet.getFromSlot()) {
            player.getInventory().swap(packet.getFromSlot(), packet.getToSlot()).refreshItems();
        }
    }
}
