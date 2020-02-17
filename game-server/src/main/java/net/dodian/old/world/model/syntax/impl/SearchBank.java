package net.dodian.old.world.model.syntax.impl;

import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.old.world.model.syntax.EnterSyntax;

public class SearchBank implements EnterSyntax {
	
	@Override
	public void handleSyntax(Player player, String input) {
		Bank.search(player, input);
	}

	@Override
	public void handleSyntax(Player player, int input) {
		
	}

}
