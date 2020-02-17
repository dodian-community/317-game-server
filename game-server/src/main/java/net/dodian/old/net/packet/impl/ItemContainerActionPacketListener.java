package net.dodian.old.net.packet.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;

public class ItemContainerActionPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		switch (packet.getOpcode()) {
		case PacketConstants.FIRST_ITEM_CONTAINER_ACTION_OPCODE:
			firstAction(player, packet);
			break;
		case PacketConstants.SECOND_ITEM_CONTAINER_ACTION_OPCODE:
			secondAction(player, packet);
			break;
		case PacketConstants.THIRD_ITEM_CONTAINER_ACTION_OPCODE:
			thirdAction(player, packet);
			break;
		case PacketConstants.FOURTH_ITEM_CONTAINER_ACTION_OPCODE:
			fourthAction(player, packet);
			break;
		case PacketConstants.FIFTH_ITEM_CONTAINER_ACTION_OPCODE:
			fifthAction(player, packet);
			break;
		case PacketConstants.SIXTH_ITEM_CONTAINER_ACTION_OPCODE:
			sixthAction(player, packet);
			break;
		}
	}

	private static void firstAction(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int slot = packet.readShortA();
		int id = packet.readShortA();
	}

	private static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int id = packet.readLEShortA();
		int slot = packet.readLEShort();
	}

	private static void thirdAction(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int id = packet.readShortA();
		int slot = packet.readShortA();
	}

	private static void fourthAction(Player player, Packet packet) {
		int slot = packet.readShortA();
		int interfaceId = packet.readInt();
		int id = packet.readShortA();
	}

	private static void fifthAction(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int slot = packet.readLEShort();
		int id = packet.readLEShort();
	}

	private static void sixthAction(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int slot = packet.readLEShort();
		int id = packet.readLEShort();
	}
}
