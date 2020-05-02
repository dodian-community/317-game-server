package net.dodian.old.net.packet.impl;

import net.dodian.managers.DefinitionsManager;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.orm.models.definitions.NpcDefinition;
import org.springframework.stereotype.Component;

@Component
public class ExamineNpcPacketListener implements PacketListener {

	private final DefinitionsManager definitionsManager;

	public ExamineNpcPacketListener(DefinitionsManager definitionsManager) {
		this.definitionsManager = definitionsManager;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		if(npc <= 0) {
			return;
		}

		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		NpcDefinition npcDef = definitionsManager.getNpcDefinitionById(npc);
		if(npcDef != null) {
			player.getPacketSender().sendMessage(npcDef.getExamine());
		}
	}

}
