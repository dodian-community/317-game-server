package net.dodian.old.world.model.syntax;

import net.dodian.old.world.entity.impl.player.Player;

public interface EnterSyntax {

	public abstract void handleSyntax(Player player, String input);
	public abstract void handleSyntax(Player player, int input);
}
