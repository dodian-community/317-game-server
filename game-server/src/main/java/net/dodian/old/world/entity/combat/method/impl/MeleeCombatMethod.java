package net.dodian.old.world.entity.combat.method.impl;

import net.dodian.old.world.content.Dueling.DuelRule;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.dialogue.DialogueManager;
import net.dodian.old.definitions.WeaponInterfaces;

/**
 * The melee combat method.
 * @author Gabriel Hannason
 */
public class MeleeCombatMethod implements CombatMethod {

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[]{new PendingHit(character, target, this, true, 0)};
	}

	@Override
	public boolean canAttack(Character character, Character target) {

		//Duel, disabled melee?
		if(character.isPlayer()) {
			Player player = character.getAsPlayer();
			if(player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MELEE.ordinal()]) {
				DialogueManager.sendStatement(player, "Melee has been disabled in this duel!");
				player.getCombat().reset();
				return false;
			}
		}

		return true;
	}

	@Override
	public void preQueueAdd(Character character, Character target) {

	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		if(character.isPlayer()) {
			if(character.getAsPlayer().getCombat().getWeapon() == WeaponInterfaces.WeaponInterface.HALBERD) {
				return 2;
			}
		}
		return 1;
	}

	@Override
	public void startAnimation(Character character) {
		int animation = character.getAttackAnim();

		if(animation != -1) {
			character.performAnimation(new Animation(animation));
		}
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {

	}

}
