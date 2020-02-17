package net.dodian.old.world.entity.combat.method;

import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.impl.Character;

public interface CombatMethod {

	
	/**
	 * Checks if this combat method can perform the upcoming attack.
	 * @param character
	 * @param target
	 * @return
	 */
	public abstract boolean canAttack(Character character, Character target);
	public abstract void preQueueAdd(Character character, Character target);
	public abstract int getAttackSpeed(Character character);
	public abstract int getAttackDistance(Character character);
	public abstract void startAnimation(Character character);
	public abstract CombatType getCombatType();
	public abstract PendingHit[] getHits(Character character, Character target);
	public abstract void finished(Character character);
	public abstract void handleAfterHitEffects(PendingHit hit);
	
}
