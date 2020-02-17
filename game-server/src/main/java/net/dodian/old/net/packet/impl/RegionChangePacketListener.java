package net.dodian.old.net.packet.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.collision.region.RegionClipping;
import net.dodian.old.world.entity.impl.npc.NpcAggression;
import net.dodian.old.world.entity.impl.object.ObjectHandler;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;


public class RegionChangePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		if(player.isAllowRegionChangePacket()) {
			RegionClipping.loadRegion(player.getPosition().getX(), player.getPosition().getY());
			ObjectHandler.onRegionChange(player);
			GroundItemManager.onRegionChange(player);
			player.getAggressionTolerance().start(NpcAggression.NPC_TOLERANCE_SECONDS); //Every 4 minutes, reset aggression for npcs in the region.
			player.setRegionChange(false);
			player.setAllowRegionChangePacket(false);
		}
	}
}
