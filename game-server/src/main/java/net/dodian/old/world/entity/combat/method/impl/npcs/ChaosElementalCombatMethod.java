package net.dodian.old.world.entity.combat.method.impl.npcs;

import net.dodian.old.definitions.WeaponInterfaces;
import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Flag;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Projectile;
import net.dodian.old.world.model.equipment.BonusManager;

/**
 * Handles the Chaos Elemental's combat.
 * @author Professor Oak
 */
public class ChaosElementalCombatMethod implements CombatMethod {
	
	private static final Graphic MELEE_COMBAT_GFX = new Graphic(869);
	private static final Graphic RANGED_COMBAT_GFX = new Graphic(867);
	private static final Graphic MAGIC_COMBAT_GFX = new Graphic(868);

	private static enum ChaosElementalAttackType {
		DEFAULT(558),
		DISARM(551),
		TELEPORT(554);

		ChaosElementalAttackType(final int projectileId) {
			this.projectileId = projectileId;
		}

		private final int projectileId;
	}

	private ChaosElementalAttackType currentAttack = ChaosElementalAttackType.DEFAULT;
	private CombatType combatType = CombatType.MELEE;
	
	@Override
	public CombatType getCombatType() {
		return combatType;
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
		//Fire projectile
		new Projectile(character, target, currentAttack.projectileId, 40, 70, 31, 43, 0).sendProjectile();
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 8;
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
		
		/**
		 * Handles special attacks
		 */
		currentAttack = ChaosElementalAttackType.DEFAULT;
	
		if(Misc.getRandom(100) <= 10) {
			currentAttack = ChaosElementalAttackType.DISARM;
		} else if(Misc.getRandom(100) <= 10) {
			currentAttack = ChaosElementalAttackType.TELEPORT;
		}
		
		/**
		 * Always switch to random combat type
		 */
		int randomAtackType = Misc.getRandom(CombatType.values().length);
		combatType = CombatType.values()[randomAtackType];
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
		if(hit.getTarget() != null) {
			
			switch(combatType) {
			case MELEE:
				hit.getTarget().performGraphic(MELEE_COMBAT_GFX);
				break;
			case RANGED:
				hit.getTarget().performGraphic(RANGED_COMBAT_GFX);
				break;
			case MAGIC:
				hit.getTarget().performGraphic(MAGIC_COMBAT_GFX);
				break;			
			}

			if(hit.getTarget().isPlayer()) {
				if(Misc.getRandom(100) <= 20) {
					Player player = hit.getTarget().getAsPlayer();

					//DISARMING
					if(currentAttack == ChaosElementalAttackType.DISARM) {
						if(!player.getInventory().isFull()) {
							final int randomSlot = Misc.getRandom(player.getEquipment().capacity());
							final Item toDisarm = player.getEquipment().getItems()[randomSlot];
							if(toDisarm.isValid()) {
								player.getEquipment().set(randomSlot, new Item(-1, 0));
								player.getInventory().add(toDisarm.copy());
								player.getPacketSender().sendMessage("The Chaos elemental has disarmed you!");
								WeaponInterfaces.assign(player);
								BonusManager.update(player);
								player.getUpdateFlag().flag(Flag.PLAYER_APPEARANCE);
							}
						}
					}
					//TELEPORTING
					else if(currentAttack == ChaosElementalAttackType.TELEPORT) {
						player.moveTo(player.getPosition().add(Misc.getRandom(4), Misc.getRandom(4)));
						player.getPacketSender().sendMessage("The Chaos elemental has teleported you.");
					}
				}				
			}
		}
	}
}
