package net.dodian.old.net.packet.impl;

import net.dodian.Server;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.orm.models.definitions.ItemDefinition;

public class ExamineItemPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int item = packet.readShort();
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		//Coins
		if(item == 995 || item == 13307) {
			player.getPacketSender().sendMessage("@red@" + Misc.insertCommasToNumber(""+player.getInventory().getAmount(item)+"") +"x coins.");
			return;
		}
		
		//Blowpipe
		if(item == 12926) {
			player.getPacketSender().sendMessage("Fires Dragon darts while coating them with venom. Zulrah scales left: "+player.getBlowpipeScales());
			return;
		}
		
		ItemDefinition itemDef = Server.getDefinitionsHandler().getItemDefinitionById(item);
		if(itemDef != null) {
			player.getPacketSender().sendMessage(itemDef.getExamine());
		}
	}

}
