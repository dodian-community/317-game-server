package net.dodian.old.world.entity.combat.method.impl.npcs;

import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.engine.task.impl.ForceMovementTask;
import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.ForceMovement;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.GraphicHeight;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.Projectile;
import net.dodian.old.world.model.SecondsTimer;

/**
 * Handles Callisto's combat.
 * @author Professor Oak
 */
public class CallistoCombatMethod implements CombatMethod {

	private static final Animation MELEE_ATTACK_ANIMATION = new Animation(4925);
	private static final Graphic END_PROJECTILE_GRAPHIC = new Graphic(359, GraphicHeight.HIGH);

	private SecondsTimer comboTimer = new SecondsTimer();
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
		return new PendingHit[]{new PendingHit(character, target, this, true, 2)};
	}

	@Override
	public void preQueueAdd(Character character, Character target) {
		if(currentAttackType == CombatType.MAGIC) {
			new Projectile(character, target, 395, 40, 60, 31, 43, 0).sendProjectile();
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
		character.performAnimation(MELEE_ATTACK_ANIMATION);
	}

	@Override
	public void finished(Character character) {

		currentAttackType = CombatType.MELEE;

		//Switch attack to magic randomly
		if(comboTimer.finished()) {
			if(Misc.getRandom(10) <= 2) {
				comboTimer.start(5);
				currentAttackType = CombatType.MAGIC;
				character.getCombat().setDisregardDelay(true);
				character.getCombat().doCombat();
			}
		}
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
		if(hit.getTarget() == null || !hit.getTarget().isPlayer()) {
			return;
		}

		final Player player = hit.getTarget().getAsPlayer();

		if(currentAttackType == CombatType.MAGIC) {
			player.performGraphic(END_PROJECTILE_GRAPHIC);
		}

		//Stun player 15% chance
		if(!player.getCombat().isStunned() && Misc.getRandom(100) <= 10) {
			player.performAnimation(new Animation(3131));
			final Position toKnock = new Position(player.getPosition().getX() > 3325 ? -3 : 1 + Misc.getRandom(2), player.getPosition().getY() > 3834 && player.getPosition().getY() < 3843 ? 3 : -3);
			TaskManager.submit(new ForceMovementTask(player, 3, new ForceMovement(player.getPosition().copy(), toKnock, 0, 15, 0, 0)));
			CombatFactory.stun(player, 4);
		}
	}
}
