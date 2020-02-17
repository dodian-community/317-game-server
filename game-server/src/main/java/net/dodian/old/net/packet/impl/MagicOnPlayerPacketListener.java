package net.dodian.old.net.packet.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.combat.magic.CombatSpell;
import net.dodian.old.world.entity.combat.magic.CombatSpells;
import net.dodian.old.world.entity.impl.player.Player;

public class MagicOnPlayerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int playerIndex = packet.readShortA();
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		if(playerIndex < 0 || playerIndex > World.getPlayers().capacity())
			return;
		int spellId = packet.readLEShort();
		if (spellId < 0) {
			return;
		}

		Player attacked = World.getPlayers().get(playerIndex);

		if (attacked == null || attacked.equals(player)) {
			player.getMovementQueue().reset();
			return;
		}


		if(attacked.getHitpoints() <= 0) {
			player.getMovementQueue().reset();
			return;
		}

		CombatSpell spell = CombatSpells.getCombatSpell(spellId);

		if(spell == null) {
			player.getMovementQueue().reset();
			return;
		}
		
		player.setPositionToFace(attacked.getPosition());
		player.getCombat().setCastSpell(spell);

		player.getCombat().attack(attacked);
	}

}
