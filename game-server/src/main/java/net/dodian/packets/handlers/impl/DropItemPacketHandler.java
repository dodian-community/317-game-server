package net.dodian.packets.handlers.impl;

import net.dodian.Server;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.old.world.model.GroundItem;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.container.impl.Inventory;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.item.DropItemPacket;
import org.springframework.stereotype.Component;

@Component
public class DropItemPacketHandler extends PacketListener {


    @PacketHandler
    public void onDropItem(DropItemPacket packet) {
        Player player = packet.getPlayer();

        if(packet.getPlayer() == null || packet.getPlayer().getHitpoints() <= 0) {
            return;
        }

        if (packet.getInterfaceId() != Inventory.INTERFACE_ID) {
            return;
        }

        if (player.busy()) {
            player.getPacketSender().sendMessage("You cannot do this right now.");
            return;
        }

        if(packet.getItemSlot() < 0 || packet.getItemSlot() > packet.getPlayer().getInventory().capacity()) {
            return;
        }

        player.getPacketSender().sendInterfaceRemoval();

        Item item = packet.getPlayer().getInventory().getItems()[packet.getItemSlot()];
        if (item == null) {
            return;
        }
        if(item.getId() != packet.getId()) {
            return;
        }

        if(item.getDefinition().isDropable()) {
            GroundItemManager.spawnGroundItem(player, new GroundItem(item, player.getPosition(), player.getUsername(), false, 150, true, 120));
            player.getInventory().setItem(packet.getItemSlot(), new Item(-1, 0)).refreshItems();
        }
    }
}
