package net.dodian.packets.handlers.impl;

import net.dodian.Server;
import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.old.world.model.Item;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.item.PickupItemPacket;
import org.springframework.stereotype.Component;


@Component
public class PickupItemPacketHandler extends PacketListener {

    @PacketHandler
    public void onPickupItem(PickupItemPacket packet) {
        Player player = packet.getPlayer();

        if(packet.getPlayer() == null || packet.getPlayer().getHitpoints() <= 0) {
            return;
        }

        if(!player.getLastItemPickup().elapsed(300)) {
            return;
        }

        if(player.busy()) {
            return;
        }

        packet.getPlayer().setWalkToTask(new WalkToTask(packet.getPlayer(), packet.getPosition(), 0, () -> {
            GroundItemManager.pickupGroundItem(player, new Item(packet.getId()), packet.getPosition());
        }));
    }
}
