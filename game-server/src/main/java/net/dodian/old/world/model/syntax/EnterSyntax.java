package net.dodian.old.world.model.syntax;

import net.dodian.old.world.entity.impl.player.Player;

public interface EnterSyntax {
	void handleSyntax(Player player, String input);
	void handleSyntax(Player player, int input);
}
