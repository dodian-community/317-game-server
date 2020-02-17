package net.dodian.old.world.entity.combat.method.impl.specials;

import net.dodian.old.world.entity.combat.CombatSpecial;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.Priority;
import net.dodian.old.world.model.Skill;

public class AbyssalBludgeonCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(3299, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1284, Priority.HIGH);

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		PendingHit hit = new PendingHit(character, target, this, true, 0);

		if(character.isPlayer()) {
			Player player = character.getAsPlayer();
			final int missingPrayer = player.getSkillManager().getMaxLevel(Skill.PRAYER) -
					player.getSkillManager().getCurrentLevel(Skill.PRAYER);
			int extraDamage = (int) (missingPrayer * 0.5);
			hit.getHits()[0].incrementDamage(extraDamage);
			hit.updateTotalDamage();
		}
		
		return new PendingHit[]{hit};
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public void preQueueAdd(Character character, Character target) {
		CombatSpecial.drain(character, CombatSpecial.ABYSSAL_DAGGER.getDrainAmount());
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
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
		if(hit.getTarget() != null) {
			hit.getTarget().performGraphic(GRAPHIC);
		}
	}
}