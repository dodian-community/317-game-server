package net.dodian.old.engine.task.impl;

import net.dodian.old.engine.task.Task;
import net.dodian.old.world.entity.combat.CombatSpecial;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;

/**
 * A {@link Task} implementation which handles
 * the regeneration of special attack.
 * 
 * @author Professor Oak
 */
public class RestoreSpecialAttackTask extends Task {

	public RestoreSpecialAttackTask(Character character) {
		super(20, character, false);
		this.character = character;
		character.setRecoveringSpecialAttack(true);
	}

	private final Character character;

	@Override
	public void execute() {
		if (character == null || !character.isRegistered() || character.getSpecialPercentage() >= 100 || !character.isRecoveringSpecialAttack()) {
			character.setRecoveringSpecialAttack(false);
			stop();
			return;
		}
		int amount = character.getSpecialPercentage() + 5;
		if (amount >= 100) {
			amount = 100;
			character.setRecoveringSpecialAttack(false);
			stop();
		}
		character.setSpecialPercentage(amount);

		if(character.isPlayer()) {
			Player player = character.getAsPlayer();
			CombatSpecial.updateBar(player);
			if(amount == 25 || amount == 50 || amount == 75 || amount == 100) {
				player.getPacketSender().sendMessage("Your special attack energy is now " + player.getSpecialPercentage() + "%.");
			}
		}
	}
}