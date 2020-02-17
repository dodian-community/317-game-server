package net.dodian.old.world.entity.combat.method.impl.specials;

import net.dodian.old.world.entity.combat.CombatSpecial;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.GraphicHeight;
import net.dodian.old.world.model.Priority;

public class DragonDaggerCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(1062, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(252, GraphicHeight.HIGH, Priority.HIGH);

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[]{new PendingHit(character, target, this, true, 0), new PendingHit(character, target, this, true, target.isNpc() ? 1 : 0)};
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public void preQueueAdd(Character character, Character target) {
		CombatSpecial.drain(character, CombatSpecial.DRAGON_DAGGER.getDrainAmount());
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 1;
	}

	@Override
	public void startAnimation(Character character) {
		character.performAnimation(ANIMATION);
		character.performGraphic(GRAPHIC);
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
	}
}