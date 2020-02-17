package net.dodian.old.net.packet.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.PlayerRelations;

public class ChatSettingsPacketListener implements PacketListener {


	@Override
	public void handleMessage(Player player, Packet packet) {
		@SuppressWarnings("unused")
		int publicMode = packet.readByte();
		
		int privateMode = packet.readByte();
		
		@SuppressWarnings("unused")
		int tradeMode = packet.readByte();
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		/*
		 * Did the player change their private chat status? 
		 * If yes, update status for all friends.
		 */
		
		if(privateMode > PlayerRelations.PrivateChatStatus.values().length) {
			return;
		}
		
		PlayerRelations.PrivateChatStatus privateChatStatus = PlayerRelations.PrivateChatStatus.values()[privateMode];
		if(player.getRelations().getStatus() != privateChatStatus) {
			player.getRelations().setStatus(privateChatStatus, true);
		}
	}
}
