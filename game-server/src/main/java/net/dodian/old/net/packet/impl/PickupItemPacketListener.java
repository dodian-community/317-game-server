package net.dodian.old.net.packet.impl;

import net.dodian.Server;
import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.old.world.model.GroundItem;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Position;

/**
 * This packet listener is used to pick up ground items
 * that exist in the world.
 *
 * @author relex lawl
 */

public class PickupItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(final Player player, Packet packet) {
        final int y = packet.readLEShort();
        final int itemId = packet.readShort();
        final int x = packet.readLEShort();
        final Position position = new Position(x, y, player.getPosition().getZ());

        if (player == null || player.getHitpoints() <= 0) {
            return;
        }

        if (!player.getLastItemPickup().elapsed(500))
            return;
        if (player.busy())
            return;
        player.setWalkToTask(new WalkToTask(player, position, 1, new WalkToTask.FinalizedMovementTask() {
            @Override
            public void execute() {
                if (Math.abs(player.getPosition().getX() - x) > 25 || Math.abs(player.getPosition().getY() - y) > 25) {
                    player.getMovementQueue().reset();
                    return;
                }
                boolean canPickup = player.getInventory().getFreeSlots() > 0 || (player.getInventory().getFreeSlots() == 0 && Server.getDefinitionsHandler().getItemDefinitionById(itemId).isStackable() && player.getInventory().contains(itemId));
                if (!canPickup) {
                    player.getInventory().full();
                    return;
                }
                GroundItem gItem = GroundItemManager.getGroundItem(player, new Item(itemId), position);
                if (gItem != null) {
                    if (player.getInventory().getAmount(gItem.getItem().getId()) + gItem.getItem().getAmount() > Integer.MAX_VALUE || player.getInventory().getAmount(gItem.getItem().getId()) + gItem.getItem().getAmount() <= 0) {
                        player.getPacketSender().sendMessage("You cannot hold that amount of this item. Clear your inventory!");
                        return;
                    }
                    GroundItemManager.pickupGroundItem(player, new Item(itemId), new Position(x, y, player.getPosition().getZ()));
                }
            }
        }));
    }
}
