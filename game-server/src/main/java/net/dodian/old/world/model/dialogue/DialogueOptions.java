package net.dodian.old.world.model.dialogue;

import net.dodian.old.world.entity.impl.player.Player;

/**
 * An abstract class for handling dialogue options.
 * 
 * @author Professor Oak
 */
public abstract class DialogueOptions {

	public abstract void handleOption(Player player, int option);
}
