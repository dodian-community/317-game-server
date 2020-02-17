package net.dodian.old.world.entity.combat.method.impl.npcs;

import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.GraphicHeight;
import net.dodian.old.world.model.Projectile;
import net.dodian.old.world.model.Skill;

/**
 * Handles Venenatis' combat.
 * @author Professor Oak
 */
public class VenenatisCombatMethod implements CombatMethod {

	private static final Animation MELEE_ATTACK_ANIMATION = new Animation(5319);
	private static final Animation MAGIC_ATTACK_ANIMATION = new Animation(5322);
	private static final Graphic DRAIN_PRAYER_GRAPHIC = new Graphic(172, GraphicHeight.MIDDLE);
	
	private CombatType currentAttackType = CombatType.MELEE;

	@Override
	public CombatType getCombatType() {
		return currentAttackType;
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[]{new PendingHit(character, target, this, true, 1)};
	}

	@Override
	public void preQueueAdd(Character character, Character target) {
		if(currentAttackType == CombatType.MAGIC) {
			new Projectile(character, target, 165, 40, 55, 31, 43, 0).sendProjectile();
		}
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 4;
	}

	@Override
	public void startAnimation(Character character) {
		if(currentAttackType == CombatType.MELEE) {
			character.performAnimation(MELEE_ATTACK_ANIMATION);
		} else {
			character.performAnimation(MAGIC_ATTACK_ANIMATION);
		}
	}

	@Override
	public void finished(Character character) {
		//Switch attack type after each attack
		if(currentAttackType == CombatType.MAGIC) {
			currentAttackType = CombatType.MELEE;
		} else {
			currentAttackType = CombatType.MAGIC;
			
			//Have a chance of comboing with magic by reseting combat delay.
			if(Misc.getRandom(10) <= 3) {
				character.getCombat().setDisregardDelay(true);
				character.getCombat().doCombat();
			}
		}
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
		if(!hit.isAccurate() || hit.getTarget() == null || !hit.getTarget().isPlayer()) {
			return;
		}

		//Drain prayer randomly 15% chance
		if(Misc.getRandom(100) <= 15) {
			Player player = hit.getTarget().getAsPlayer();
			hit.getTarget().performGraphic(DRAIN_PRAYER_GRAPHIC);
			player.getSkillManager().decreaseCurrentLevel(Skill.PRAYER, (int)(hit.getTotalDamage() * 0.35), 0);
			player.getPacketSender().sendMessage("Venenatis drained your prayer!");
		}
	}
}
