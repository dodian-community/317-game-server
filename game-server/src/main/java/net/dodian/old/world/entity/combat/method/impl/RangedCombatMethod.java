package net.dodian.old.world.entity.combat.method.impl;

import net.dodian.old.world.content.Dueling.DuelRule;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.combat.ranged.RangedData.AmmunitionData;
import net.dodian.old.world.entity.combat.ranged.RangedData.RangedWeaponData;
import net.dodian.old.world.entity.combat.ranged.RangedData.RangedWeaponType;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Projectile;
import net.dodian.old.world.model.dialogue.DialogueManager;

/**
 * The ranged combat method.
 * @author Gabriel Hannason
 */
public class RangedCombatMethod implements CombatMethod {

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGED;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {

		//Darkbow is double hits.
		//Ballista is 1 tick slower
		if(character.getCombat().getRangedWeaponData() != null) {
			if(character.getCombat().getRangedWeaponData() == RangedWeaponData.DARK_BOW) {
				return new PendingHit[]{new PendingHit(character, target, this, true, 2), 
						new PendingHit(character, target, this, true, 3)};
			} else if(character.getCombat().getRangedWeaponData() == RangedWeaponData.BALLISTA) {
				return new PendingHit[]{new PendingHit(character, target, this, true, 3)};
			}
		}

		return new PendingHit[]{new PendingHit(character, target, this, true, 2)};
	}

	@Override
	public boolean canAttack(Character character, Character target) {

		if(character.isNpc()) {
			return true;
		}

		Player p = character.getAsPlayer();

		//Duel, disabled ranged?
		if(p.getDueling().inDuel() && p.getDueling().getRules()[DuelRule.NO_RANGED.ordinal()]) {
			DialogueManager.sendStatement(p, "Ranged has been disabled in this duel!");
			p.getCombat().reset();
			return false;
		}

		int ammoRequired = 1;

		//Check for weapons that require more arrows than 1 to fire..
		if(p.getCombat().getRangedWeaponData() == RangedWeaponData.DARK_BOW) {
			ammoRequired = 2;
		}

		//Check that we have the ammo required
		if(!CombatFactory.checkAmmo(p, ammoRequired)) {
			return false;
		}

		return true;
	}

	@Override
	public void preQueueAdd(Character character, Character target) {

		final AmmunitionData ammo = character.getCombat().getAmmunition();
		final RangedWeaponData rangedWeapon = character.getCombat().getRangedWeaponData();

		if(ammo == null || rangedWeapon == null) {
			return;
		}

		int projectileId = ammo.getProjectileId();
		int delay = 40;
		int speed = 60;
		int heightEnd = 31;
		int heightStart = 43;
		int curve = 0;

		if(rangedWeapon.getType() == RangedWeaponType.CROSSBOW) {
			delay = 46;
			speed = 62;
			heightStart = 44;
			heightEnd = 35;
			curve = 3;
		} else if(rangedWeapon.getType() == RangedWeaponType.DARK_BOW) {
			speed = 70;
		} else if(rangedWeapon.getType() == RangedWeaponType.BLOWPIPE) {
			speed = 60;
			heightStart = 40;
			heightEnd = 35;
			curve = 5;
		}
		if(ammo == AmmunitionData.TOKTZ_XIL_UL) {
			delay = 30;
			speed = 55;
		}

		//Fire projectile
		new Projectile(character, target, projectileId, delay, speed, heightStart, heightEnd, curve).sendProjectile();

		//Dark bow sends two arrows, so send another projectile and delete another arrow.
		if(rangedWeapon == RangedWeaponData.DARK_BOW) {
			new Projectile(character, target, ammo.getProjectileId(), delay - 7, speed + 4, heightStart + 5, heightEnd, curve).sendProjectile();

			//Decrement 2 ammo if d bow
			if(character.isPlayer()) {
				CombatFactory.decrementAmmo(character.getAsPlayer(), target.getPosition(), 2);
			}

		} else {

			//Decrement 1 ammo
			if(character.isPlayer()) {
				CombatFactory.decrementAmmo(character.getAsPlayer(), target.getPosition(), 1);
			}
		}
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		if(character.isPlayer()) {
			return character.getAsPlayer().getCombat().
					getRangedWeaponData().getType().getDistanceRequired();
		}
		return 6;
	}

	@Override
	public void startAnimation(Character character) {
		int animation = character.getAttackAnim();

		if(animation != -1) {
			character.performAnimation(new Animation(animation));
		}

		final AmmunitionData ammo = character.getCombat().getAmmunition();
		if(ammo != null && ammo.getStartGraphic() != null) {
			
			//Check toxic blowpipe, it shouldn't have any start gfx.
			if(character.getCombat().getRangedWeaponData() != null) {
				if(character.getCombat().getRangedWeaponData() == RangedWeaponData.TOXIC_BLOWPIPE) {
					return;
				}
			}
			
			//Perform start gfx for ammo
			character.performGraphic(ammo.getStartGraphic());
		}
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {

	}
}
