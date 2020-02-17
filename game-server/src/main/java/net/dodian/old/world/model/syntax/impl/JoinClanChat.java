package net.dodian.old.world.model.syntax.impl;

import net.dodian.old.world.content.clan.ClanChatManager;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.syntax.EnterSyntax;

public class JoinClanChat implements EnterSyntax {
	
	@Override
	public void handleSyntax(Player player, String input) {
		ClanChatManager.join(player, input);
	}

	@Override
	public void handleSyntax(Player player, int input) {
		
	}
}
