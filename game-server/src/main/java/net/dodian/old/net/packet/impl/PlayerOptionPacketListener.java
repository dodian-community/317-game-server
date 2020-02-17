package net.dodian.old.net.packet.impl;

import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Locations;


/**
 * This packet listener is called when a player has clicked on another player's
 * menu actions.
 * 
 * @author relex lawl
 */

public class PlayerOptionPacketListener implements PacketListener {


	@Override
	public void handleMessage(Player player, Packet packet) {

		if(player == null || player.getHitpoints() <= 0) {
			return;
		}

		if(player.busy()) {
			return;
		}
		switch(packet.getOpcode()) {
		case PacketConstants.ATTACK_PLAYER_OPCODE:
			attack(player, packet);
			break;
		case PacketConstants.PLAYER_OPTION_1_OPCODE:
			option1(player, packet);
			break;
		case PacketConstants.PLAYER_OPTION_2_OPCODE:
			option2(player, packet);
			break;
		case PacketConstants.PLAYER_OPTION_3_OPCODE:
			option3(player, packet);
			break;
		}
	}

	private static void attack(Player player, Packet packet) {
		int index = packet.readLEShort();
		if(index > World.getPlayers().capacity() || index < 0)
			return;
		final Player attacked = World.getPlayers().get(index);

		if(attacked == null || attacked.getHitpoints() <= 0 || attacked.equals(player)) {
			player.getMovementQueue().reset();
			return;
		}
				
		player.getCombat().attack(attacked);
	}

	/**
	 * Manages the first option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void option1(Player player, Packet packet) {
		int id = packet.readShort() & 0xFFFF;
		if(id < 0 || id > World.getPlayers().capacity())
			return;
		Player player2 = World.getPlayers().get(id);
		if (player2 == null)
			return;
		player.setWalkToTask(new WalkToTask(player, player2.getPosition(), 1, new WalkToTask.FinalizedMovementTask() {
			@Override
			public void execute() {
				if(player.getLocation() == Locations.Location.DEFAULT) {
					if(player.busy()) {
						player.getPacketSender().sendMessage("You cannot do that right now.");
						return;
					}
					if(player2.busy()) {
						player.getPacketSender().sendMessage("That player is currently busy.");
						return;
					}
					player.getDueling().requestDuel(player2);
					return;
				}
			}
		}));
	}

	/**
	 * Manages the second option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void option2(Player player, Packet packet) {
		int id = packet.readShort() & 0xFFFF;
		if(id < 0 || id > World.getPlayers().capacity())
			return;
		Player victim = World.getPlayers().get(id);
		if (victim == null)
			return;
		/*player.setWalkToTask(new WalkToTask(player, victim.getPosition(), 1, new FinalizedMovementTask() {
			@Override
			public void execute() {

			}
		}));*/
	}

	/**
	 * Manages the third option click on a player option menu.
	 * @param player	The player clicking the other entity.
	 * @param packet	The packet to read values from.
	 */
	private static void option3(Player player, Packet packet) {
		int id = packet.readLEShortA() & 0xFFFF;
		if(id < 0 || id > World.getPlayers().capacity())
			return;
		Player victim = World.getPlayers().get(id);
		if (victim == null)
			return;
		/*player.setWalkToTask(new WalkToTask(player, victim.getPosition(), 1, new FinalizedMovementTask() {
			@Override
			public void execute() {

			}
		}));*/
	}
}
