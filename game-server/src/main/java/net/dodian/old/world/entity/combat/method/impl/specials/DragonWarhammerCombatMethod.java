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

public class DragonWarhammerCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(1378, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(1292, Priority.HIGH);
	
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
		return true;
	}

	@Override
	public void preQueueAdd(Character character, Character target) {
		CombatSpecial.drain(character, CombatSpecial.DRAGON_WARHAMMER.getDrainAmount());
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
		if(hit.isAccurate() && hit.getTarget().isPlayer()) {
			int damageDrain = (int) (hit.getTotalDamage() * 0.3);
			if(damageDrain < 0)
				return;
			Player player = hit.getAttacker().getAsPlayer();
			Player target = hit.getTarget().getAsPlayer();
			target.getSkillManager().decreaseCurrentLevel(Skill.DEFENCE, damageDrain, 1);
			player.getPacketSender().sendMessage("You've drained "+target.getUsername()+"'s Defence level by "+damageDrain+".");
			target.getPacketSender().sendMessage("Your Defence level has been drained.");
		}
	}
}