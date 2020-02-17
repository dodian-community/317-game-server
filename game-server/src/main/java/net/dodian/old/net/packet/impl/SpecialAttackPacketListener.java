package net.dodian.old.net.packet.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.combat.CombatSpecial;
import net.dodian.old.world.entity.impl.player.Player;

/**
 * This packet listener handles the action when pressing
 * a special attack bar.
 * @author Professor Oak
 */

public class SpecialAttackPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		@SuppressWarnings("unused")
		int specialBarButton = packet.readInt();
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		CombatSpecial.activate(player);
	}
}
