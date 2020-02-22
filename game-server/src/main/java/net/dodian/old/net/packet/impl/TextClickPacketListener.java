package net.dodian.old.net.packet.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.content.Presetables;
import net.dodian.old.world.content.clan.ClanChatManager;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextClickPacketListener implements PacketListener {

	private final Bank bank;

	@Autowired
	public TextClickPacketListener(Bank bank) {
		this.bank = bank;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		int frame = packet.readInt();
		int action = packet.readByte();
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		if(bank.handleButton(player, frame, action)) {
			return;
		}
		if(ClanChatManager.handleButton(player, frame, action)) {
			return;
		}
		if(Presetables.handleButton(player, frame)) {
			return;
		}
	}
}
