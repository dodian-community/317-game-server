package net.dodian.old.net.packet.impl;

import net.dodian.old.net.ByteBufUtils;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;

/**
 * This packet manages the input taken from chat box interfaces that allow input,
 * such as withdraw x, bank x, enter name of friend, etc.
 * 
 * @author Gabriel Hannason
 */

public class EnterInputPacketListener implements PacketListener {


	@Override
	public void handleMessage(Player player, Packet packet) {
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		switch (packet.getOpcode()) {
		case PacketConstants.ENTER_SYNTAX_OPCODE:
			String name = ByteBufUtils.readString(packet.getBuffer());
			if(name == null)
				return;
			if(player.getEnterSyntax() != null) {
				player.getEnterSyntax().handleSyntax(player, name);
			}
			player.setEnterSyntax(null);
			break;
		case PacketConstants.ENTER_AMOUNT_OPCODE:
			int amount = packet.readInt();
			if(amount <= 0)
				return;
			if(player.getEnterSyntax() != null) {
				player.getEnterSyntax().handleSyntax(player, amount);
			}
			player.setEnterSyntax(null);
			break;
		}
	}
}
