package net.dodian.old.world.entity.combat;

import java.util.Optional;

import net.dodian.GameConstants;
import net.dodian.old.definitions.WeaponInterfaces;
import net.dodian.old.engine.task.Task;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.engine.task.impl.CombatPoisonEffect;
import net.dodian.old.net.SessionState;
import net.dodian.old.util.Misc;
import net.dodian.old.world.collision.region.RegionClipping;
import net.dodian.old.world.content.PrayerHandler;
import net.dodian.old.world.entity.combat.formula.DamageFormulas;
import net.dodian.old.world.entity.combat.hit.HitDamage;
import net.dodian.old.world.entity.combat.hit.HitMask;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.combat.method.impl.MagicCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.MeleeCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.RangedCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.npcs.CallistoCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.npcs.ChaosElementalCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.npcs.VenenatisCombatMethod;
import net.dodian.old.world.entity.combat.ranged.RangedData;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.npc.bots.NPCBotHandler;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.EffectTimer;
import net.dodian.old.world.model.Flag;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.GraphicHeight;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.SkullType;
import net.dodian.old.world.model.container.impl.Equipment;
import net.dodian.old.world.model.movement.path.RS317PathFinder;
import net.dodian.old.world.entity.impl.npc.NPCMovementCoordinator;
import net.dodian.old.world.model.*;

/**
 * "The Combat Factory"
 * Contains a bunch of methods and other things needed for combat.
 * @author Swiffy
 */
public class CombatFactory {

	/**
	 * The default melee combat method.
	 */
	public static final MeleeCombatMethod MELEE_COMBAT = new MeleeCombatMethod();

	/**
	 * The default ranged combat method
	 */
	public static final RangedCombatMethod RANGED_COMBAT = new RangedCombatMethod();

	/**
	 * The default magic combat method
	 */
	public static final MagicCombatMethod MAGIC_COMBAT = new MagicCombatMethod();

	/**
	 * Gets a character's combat method.
	 * 
	 * @param attacker		The character to get the combat method for.
	 * @return				
	 */
	public static CombatMethod getMethod(Character attacker) {

		if(attacker.isPlayer()) {
			Player p = attacker.getAsPlayer();

			//Update player data..
			//Update ranged ammo / weapon
			p.getCombat().setAmmunition(RangedData.AmmunitionData.getFor(p));
			p.getCombat().setRangedWeaponData(RangedData.RangedWeaponData.getFor(p));

			//Check if player is maging..
			if(p.getCombat().getCastSpell() != null
					|| p.getCombat().getAutocastSpell() != null) {
				return MAGIC_COMBAT;
			}

			//Check special attacks..
			if(p.getCombatSpecial() != null) {
				if(p.isSpecialActivated()) {
					return p.getCombatSpecial().getCombatMethod();
				}
			}

			//Check if player is ranging..
			if(p.getCombat().getRangedWeaponData() != null) {
				return RANGED_COMBAT;
			}

		} else if(attacker.isNpc()) {

			NPC npc = attacker.getAsNpc();

			/**
			 * If it's not, return it's combat method.
			 */
			if(npc.getCombatMethod() != null) {
				return npc.getCombatMethod();
			}
		}

		//Return melee by default
		return MELEE_COMBAT;
	}

	/**
	 * Assigns the combat method for the specified 
	 * {@link NPC}.
	 * @param npc
	 */
	public static void assignCombatMethod(NPC npc) {

		//Assign combat methods for npcs.
		switch(npc.getId()) {
		case 2054:
			npc.setCombatMethod(new ChaosElementalCombatMethod());
			break;
		case 6504:
			npc.setCombatMethod(new VenenatisCombatMethod());
			break;
		case 6609:
			npc.setCombatMethod(new CallistoCombatMethod());
			break;			
		}

		//If they haven't been given a combat method yet and they're a bot,
		//Simply use the their bot handler's choice of method.
		if(npc.getCombatMethod() == null) {
			if(npc.getBotHandler() != null) {
				npc.setCombatMethod(npc.getBotHandler().getMethod());
			}
		}
	}

	/**
	 * Generates a random {@link HitDamage} based on the argued entity's stats.
	 * 
	 * @param entity
	 *            the entity to generate the random hit for.
	 * @param victim
	 *            the victim being attacked.
	 * @param type
	 *            the combat type being used.
	 * @return the HitDamage.
	 */
	public static HitDamage getHitDamage(Character entity, Character victim, CombatType type) {

		int damage = 0;

		if(type == CombatType.MELEE) {
			damage = Misc.inclusive(1, DamageFormulas.calculateMaxMeleeHit(entity));

			//Do melee effects with the calculated damage..

		} else if(type == CombatType.RANGED) {
			damage = Misc.inclusive(1, DamageFormulas.calculateMaxRangedHit(entity));

			//Do ranged effects with the calculated damage..
			if (entity.isPlayer()) {

				Player player = entity.getAsPlayer();

				//Check if player is using dark bow and set damage to minimum 8, maxmimum 48 if that's the case...
				if(player.getAsPlayer().isSpecialActivated()
						&& player.getAsPlayer().getCombatSpecial() == CombatSpecial.DARK_BOW) {
					if(damage < 8) {
						damage = 8;
					} else if(damage > 48) {
						damage = 48;
					}
				}

				//Handle bolt special effects for a player whose using crossbow
				if(player.getCombat().getWeapon() == WeaponInterfaces.WeaponInterface.CROSSBOW && Misc.getRandom(10) == 1) {
					double multiplier = RangedData.getSpecialEffectsMultiplier(player, victim, damage);
					damage *= multiplier;
				}
			}
		} else if(type == CombatType.MAGIC) {
			damage = Misc.inclusive(1, DamageFormulas.getMagicMaxhit(entity));

			//Do magic effects with the calculated damage..
		}

		//We've got our damage. We can now create a HitDamage
		//instance.
		HitDamage hitDamage = new HitDamage(damage, HitMask.RED);

		/**
		 * Prayers decreasing damage.
		 */

		//Decrease damage if victim is a player and has prayers active..
		if ((!CombatFactory.fullVeracs(entity) || Misc.getRandom(4) == 1)) {

			//Check if victim is is using correct protection prayer
			if (PrayerHandler.isActivated(victim, PrayerHandler.getProtectingPrayer(type))) {

				//Apply the damage reduction mod
				if(entity.isNpc()) {
					hitDamage.multiplyDamage(CombatConstants.PRAYER_DAMAGE_REDUCTION_AGAINST_NPCS);
				} else {
					hitDamage.multiplyDamage(CombatConstants.PRAYER_DAMAGE_REDUCTION_AGAINST_PLAYERS);
				}
			}
		}

		//Check elysian spirit shield damage reduction
		if(victim.isPlayer() && Misc.getRandom(100) <= 70) {
			if(victim.getAsPlayer().getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 12817) {
				hitDamage.multiplyDamage(CombatConstants.ELYSIAN_DAMAGE_REDUCTION);
				victim.performGraphic(new Graphic(321, 40)); //Elysian spirit shield effect gfx
			}
		}

		//Return our hitDamage that may have been modified slightly.
		return hitDamage;
	}


	/**
	 * Checks if an entity is a valid target.
	 * @param attacker
	 * @param target
	 * @return
	 */
	public static boolean validTarget(Character attacker, Character target) {
		if (!target.isRegistered() || !attacker.isRegistered() || attacker.getHitpoints() <= 0 || target.getHitpoints() <= 0) {
			attacker.getCombat().reset();
			return false;
		}

		if(target.isUntargetable()) {
			return false;
		}

		// Check if teleporting away/teleported away
		if (attacker.getPosition().getDistance(target.getPosition()) >= 40) {
			attacker.getCombat().reset();
			return false;
		}

		//Check if any of the two have wrong session state.
		if(target.isPlayer()) {
			if(target.getAsPlayer().getSession().getState() != SessionState.LOGGED_IN) {
				return false;
			}
		}
		if(attacker.isPlayer()) {
			if(attacker.getAsPlayer().getSession().getState() != SessionState.LOGGED_IN) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if an entity can reach a target.
	 * @param attacker		The entity which wants to attack.
	 * @param target		The victim.
	 * @return				True if attacker has the proper distance to attack, otherwise false.
	 */
	public static boolean canReach(Character attacker, CombatMethod method, Character target) {
		if(!validTarget(attacker, target)) {
			return false;
		}

		//Walk back if npc is too far away from spawn position.
		if(attacker.isNpc()) {
			NPC npc = attacker.getAsNpc();
			if(npc.getDefinition().doesRetreat()) {
				if(npc.getMovementCoordinator().getCoordinateState() == NPCMovementCoordinator.CoordinateState.RETREATING) {
					npc.getCombat().reset();
					return false;
				}
				if(npc.getPosition().getDistance(npc.getSpawnPosition()) >= npc.getDefinition().getCombatFollowDistance()) {
					npc.getCombat().reset();
					npc.getMovementCoordinator().setCoordinateState(NPCMovementCoordinator.CoordinateState.RETREATING);
					return false;
				}
			}
		}

		int distance = method.getAttackDistance(attacker);
		distance += target.getSize() - 1;

		// We're moving so increase the distance by 2.
		if (attacker.getMovementQueue().isMoving() && target.getMovementQueue().isMoving()) {
			distance += 2;
		}

		boolean goodDistance = attacker.getPosition().isWithinDistance(target.getPosition(), distance);

		//Check if there's a square between us and if player is moving
		//Used for allowing smoother combos with ranged to melee
		boolean runningTowards = false;
		if(!target.getMovementQueue().isMoving()) {
			if(attacker.getMovementQueue().isMoving()) {
				if(method.getCombatType() == CombatType.MELEE) {
					int deltaDistance = attacker.getPosition().getDistance(target.getPosition());
					if(deltaDistance <= 3) {
						runningTowards = true;
					}
				}
			}
		}


		//Check good distance?
		if(!runningTowards) {
			if(!goodDistance) {
				return false;
			}
		}

		//Stop running forward if we're in distance.
		attacker.getMovementQueue().reset();

		//Check diagonal
		if(!runningTowards) {
			if(method.getCombatType() == CombatType.MELEE) {
				if(RegionClipping.isInDiagonalBlock(attacker, target)) {
					RS317PathFinder.solveDiagonalBlock(attacker, target);
					return false;
				}
			}
		}

		//Check projectiles..
		if(!RegionClipping.canProjectileAttack(attacker, target)) {
			return false;
		}

		//Check same spot
		if(attacker.getPosition().equals(target.getPosition())) {

			if(!attacker.getCombat().getFreezeTimer().finished()) {
				return false;
			}

			RS317PathFinder.solveDiagonalBlock(attacker, target);
		}

		return true;
	}

	/**
	 * Checks if an entity can attack a target.
	 * @param attacker		The entity which wants to attack.
	 * @param target		The victim.
	 * @return				True if attacker has the requirements to attack, otherwise false.
	 */
	public static boolean canAttack(Character attacker, CombatMethod method, Character target) {
		if(!validTarget(attacker, target)) {
			return false;
		}

		// Here we check if we are already in combat with another entity.
		//Only check if we aren't in multi.
		if(!(Locations.Location.inMulti(attacker) && Locations.Location.inMulti(target))) {
			if (isBeingAttacked(attacker) && attacker.getCombat().getAttacker() != target && attacker.getCombat().getAttacker().getHitpoints() > 0
					|| !attacker.getCombat().getHitQueue().isEmpty(target)) {
				if (attacker.isPlayer()) {
					attacker.getAsPlayer().getPacketSender().sendMessage("You are already under attack!");
				}
				attacker.getCombat().reset();
				return false;
			}

			// Here we check if we are already in combat with another entity.
			if(isBeingAttacked(target) && target.getCombat().getAttacker() != attacker || !target.getCombat().getHitQueue().isEmpty(attacker)) {
				if (attacker.isPlayer()) {
					attacker.getAsPlayer().getPacketSender().sendMessage("They are already under attack!");
				}
				attacker.getCombat().reset();
				return false;
			}
		}

		//Check if they can attack in their location instance
		if(attacker.isPlayer() && target.isPlayer()) {
			if(!attacker.getLocation().canAttack(attacker.getAsPlayer(), target.getAsPlayer())) {
				attacker.getCombat().reset();
				return false;
			}
		}

		if(!method.canAttack(attacker, target)) {
			return false;
		}

		//Check special attack
		if(attacker.isPlayer()) {
			Player p = attacker.getAsPlayer();

			//Check if we're using a special attack..
			if(p.isSpecialActivated() && p.getCombatSpecial() != null) {
				//Check if we have enough special attack percentage.
				//If not, reset special attack.
				if (p.getSpecialPercentage() < p.getCombatSpecial().getDrainAmount()) {
					p.getPacketSender().sendMessage("You do not have enough special attack energy left!");
					p.setSpecialActivated(false);
					CombatSpecial.updateBar(p);
					p.getCombat().reset();
					return false;
				}
			}

			if(p.getCombat().isStunned()) {
				p.getPacketSender().sendMessage("You're currently stunned and cannot attack.");
				p.getCombat().reset();
				return false;
			}

			if(target.isNpc()) {
				NPC npc = target.getAsNpc();
				if(npc.getBotHandler() != null) {
					if(npc.getLocation() != Locations.Location.WILDERNESS) {
						p.getPacketSender().sendMessage("That bot cannot be attacked, because they are not in the Wilderness.");
						p.getMovementQueue().reset();
						//p.getCombat().reset();
						//return false;
					}
					int combatDiff = combatLevelDifference(p.getSkillManager().getCombatLevel(), npc.getDefinition().getCombatLevel());
					if (combatDiff > 5) {
						p.getPacketSender().sendMessage("Your combat level difference is too great to attack that bot.");
						attacker.getMovementQueue().reset();
						attacker.getCombat().reset();
						return false;
					}
				}
			}
		} else if(attacker.isNpc()) {
			NPC npc = attacker.getAsNpc();
			if(npc.getBotHandler() != null) {
				if(target.getLocation() != Locations.Location.WILDERNESS) {
					npc.getCombat().reset();
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Adds a hit to a target's queue.
	 * 
	 * @param qHit
	 */
	public static void addPendingHit(PendingHit qHit) {
		Character attacker = qHit.getAttacker();
		Character target = qHit.getTarget();
		HitDamage[] damage = qHit.getHits();
		if(damage == null || target.getHitpoints() <= 0) {
			return;
		}

		if(attacker.isPlayer()) {			
			//Reward the player experience for this attack..
			rewardExp(attacker.getAsPlayer(), qHit);

			//Check if the player should be skulled for making this attack..
			if(target.isPlayer()) {
				handleSkull(attacker.getAsPlayer(), target.getAsPlayer());
			}
		}

		//If target is teleporting or needs placement
		//Dont continue to add the hit.
		if(target.isUntargetable() || 
				target.isNeedsPlacement()) {
			return;
		}

		//Add this hit to the target's hitQueue
		target.getCombat().getHitQueue().addPendingHit(qHit);
	}

	/**
	 * Executes a hit that has been ticking until now.
	 * 
	 * @param qHit		The QueueableHit to execute.
	 */
	public static void executeHit(PendingHit qHit) {
		final Character attacker = qHit.getAttacker();
		final Character target = qHit.getTarget();
		final CombatMethod method = qHit.getCombatMethod();
		final int damage = qHit.getTotalDamage();

		//If target/attacker is dead, don't continue.
		if(target.getHitpoints() <= 0 || attacker.getHitpoints() <= 0) {
			return;
		}

		//If target is teleporting or needs placement
		//Don't continue to add the hit.
		if(target.isUntargetable() || 
				target.isNeedsPlacement()) {
			return;
		}

		//Do block animation
		target.performAnimation(new Animation(target.getBlockAnim()));

		//Do other stuff for players..
		if(target.isPlayer()) {
			final Player p_ = target.getAsPlayer();

			//Close their current interface
			p_.getPacketSender().sendInterfaceRemoval();

			//Prayer effects
			if(qHit.isAccurate()) {

				if (PrayerHandler.isActivated(p_, PrayerHandler.REDEMPTION)) {
					handleRedemption(attacker, p_, damage);
				}

				if (PrayerHandler.isActivated(attacker, PrayerHandler.SMITE)) {
					handleSmite(attacker, p_, damage);
				}
			}
		}

		//Here, we take the damage.
		//BUT, don't take damage if the attack was a magic splash by a player.
		boolean magic_splash = method.getCombatType() == CombatType.MAGIC && 
				!qHit.isAccurate();
		if(!(magic_splash && attacker.isPlayer())) {
			target.getCombat().getHitQueue().addPendingDamage(qHit.getHits());
		}

		//Make sure to let the combat method know we finished the attack
		//Only if this isn't custom hit (handleAfterHitEffects() will be false then)
		if(qHit.handleAfterHitEffects()) {
			if(method != null) {
				method.handleAfterHitEffects(qHit);
			}
		}

		//Check for poisonous weapons..
		//And do other effects, such as barrows effects..
		if(attacker.isPlayer()) {

			Player p_ = attacker.getAsPlayer();

			//Randomly apply poison if poisonous weapon is equipped.
			if(Misc.getRandom(10) <= 5) {

				Optional<CombatPoisonEffect.PoisonType> poison = Optional.empty();

				if (method.getCombatType() == CombatType.MELEE || p_.getCombat().getWeapon() == WeaponInterfaces.WeaponInterface.DART || p_.getCombat().getWeapon() == WeaponInterfaces.WeaponInterface.KNIFE || p_.getCombat().getWeapon() == WeaponInterfaces.WeaponInterface.THROWNAXE || p_.getCombat().getWeapon() == WeaponInterfaces.WeaponInterface.JAVELIN) {
					poison = CombatPoisonEffect.CombatPoisonData.getPoisonType(p_.getEquipment().get(Equipment.WEAPON_SLOT));
				} else if (method.getCombatType() == CombatType.RANGED) {
					poison = CombatPoisonEffect.CombatPoisonData.getPoisonType(p_.getEquipment().get(Equipment.AMMUNITION_SLOT));
				}

				if(poison.isPresent()) {
					CombatFactory.poisonEntity(target, poison.get());
				}
			}

			//Handle barrows effects if damage is more than zero.
			if(qHit.getTotalDamage() > 0) {
				if(Misc.getRandom(10) >= 8) {

					//Apply Guthan's effect..
					if (fullGuthans(p_)) {
						handleGuthans(p_, target, qHit.getTotalDamage());
					}

					//Other barrows effects here..
				}
			}
		} else if(attacker.isNpc()) {
			NPC npc = attacker.getAsNpc();
			if(npc.getDefinition().isPoisonous()) {
				if(Misc.getRandom(10) <= 5) {
					CombatFactory.poisonEntity(target, CombatPoisonEffect.PoisonType.SUPER);
				}
			}
		}

		//Handle ring of recoil for target
		//Also handle vengeance for target
		if(qHit.getTotalDamage() > 0) {
			handleRecoil(target, attacker, qHit.getTotalDamage());
			handleVengeance(target, attacker, qHit.getTotalDamage());
		}

		//Auto retaliate if needed
		handleRetaliation(attacker, target);

		//Set under attack
		target.getCombat().setUnderAttack(attacker);

		//Add damage to target damage map
		target.getCombat().addDamage(attacker, qHit.getTotalDamage());
	}


	/**
	 * Rewards a player with experience in respective skills based on
	 * how much damage they've dealt.
	 * 
	 * @param player		The player.
	 * @param hit			The damage dealt.
	 */
	public static void rewardExp(Player player, PendingHit hit) {

		//Add magic exp, even if total damage is 0.
		//Since spells have a base exp reward
		if(hit.getCombatMethod().getCombatType() == CombatType.MAGIC) {
			if(player.getCombat().getPreviousCast() != null) {
				if(hit.isAccurate()) {
					player.getSkillManager().addExperience(Skill.MAGIC, (int)(hit.getTotalDamage() * GameConstants.EXP_MULTIPLIER) + player.getCombat().getPreviousCast().baseExperience());
				} else {
					//Splash should only give 52 exp..
					player.getSkillManager().addExperience(Skill.MAGIC, 52);
				}
			}
		}

		//Don't add any exp to other skills if total damage is 0.
		if(hit.getTotalDamage() <= 0) {
			return;
		}

		//Add hp xp
		player.getSkillManager().addExperience(Skill.HITPOINTS, (int) ((hit.getTotalDamage() * .70) * GameConstants.EXP_MULTIPLIER));

		//Magic xp was already added
		if(hit.getCombatMethod().getCombatType() == CombatType.MAGIC) {
			return;
		}

		//Add all other skills xp
		final int[] exp = hit.getSkills();
		for (int i : exp) {
			Skill skill = Skill.values()[i];
			player.getSkillManager().addExperience(skill, (int) (((hit.getTotalDamage()) / exp.length) * GameConstants.EXP_MULTIPLIER));
		}
	}

	/**
	 * Checks if a character is currently attacking.
	 * @param character		The character to check for.
	 * @return				true if character is attacking, false otherwise.
	 */
	public static boolean isAttacking(Character character) {
		return character.getCombat().getTarget() != null;		
	}

	/**
	 * Checks if a character is currently under attack.
	 * @param character		The character to check for.
	 * @return				true if character is being attacked, false otherwise.
	 */
	public static boolean isBeingAttacked(Character character) {
		return character.getCombat().getAttacker() != null;
	}

	/**
	 * Checks if a character is currently in combat.
	 * @param character		The character to check for.
	 * @return				true if character is in combat, false otherwise.
	 */
	public static boolean inCombat(Character character) {
		return isAttacking(character) || isBeingAttacked(character);
	}

	/**
	 * Attempts to poison the argued {@link Character} with the argued
	 * {@link CombatPoisonEffect.PoisonType}. This method will have no effect if the entity is
	 * already poisoned.
	 * 
	 * @param entity
	 *            the entity that will be poisoned, if not already.
	 * @param poisonType
	 *            the poison type that this entity is being inflicted with.
	 */
	public static void poisonEntity(Character entity, CombatPoisonEffect.PoisonType poisonType) {

		// We are already poisoned or the poison type is invalid, do nothing.
		if (entity.isPoisoned()) {
			return;
		}

		// If the entity is a player, we check for poison immunity. If they have
		// no immunity then we send them a message telling them that they are
		// poisoned.
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			if (!player.getCombat().getPoisonImmunityTimer().finished()) {
				return;
			}
			player.getPacketSender().sendMessage("You have been poisoned!");
		}

		entity.setPoisonDamage(poisonType.getDamage());
		TaskManager.submit(new CombatPoisonEffect(entity));
	}

	/**
	 * Disables protection prayers for a player.
	 * @param player		The player to disable protecetion prayers for.
	 */
	public static void disableProtectionPrayers(Player player) {

		//Player has already been prayer-disabled
		if(!player.getCombat().getPrayerBlockTimer().finished()) {
			return;
		}

		player.getCombat().getPrayerBlockTimer().start(200);
		PrayerHandler.resetPrayers(player, PrayerHandler.PROTECTION_PRAYERS);
		player.getPacketSender().sendMessage("You have been disabled and can no longer use protection prayers.");
	}

	/**
	 * Handles the item "Ring of Recoil" for a player.
	 * The item returns damage to the attacker.
	 * 
	 * @param attacker
	 * @param damage
	 */
	public static void handleRecoil(Character character, Character attacker, int damage) {
		final int returnDmg = (int) Math.ceil(damage * 0.1D);
		if(returnDmg <= 0) {
			return;
		}

		if(character.isPlayer()) {
			Player player = character.getAsPlayer();

			//Check if player is wearing recoil.
			if(player.getEquipment().get(Equipment.RING_SLOT).getId() == CombatConstants.RING_OF_RECOIL_ID) {

				//Increase recoil damage for a player.
				player.setRecoilDamage(player.getRecoilDamage() + returnDmg);

				//Deal damage back to attacker
				attacker.getCombat().getHitQueue().addPendingDamage(new HitDamage(returnDmg, HitMask.RED));

				//Degrading ring of recoil for a player.
				if(player.getRecoilDamage() >= 40 || Misc.getRandom(200) >= 195) {
					player.getEquipment().set(Equipment.RING_SLOT, new Item(-1));
					player.getEquipment().refreshItems();
					player.getPacketSender().sendMessage("Your ring of recoil has degraded.");
					player.setRecoilDamage(0);
				}
			}
		} else if(character.isNpc()) {
			NPC npc = character.getAsNpc();

			if(npc.getBotHandler() != null) {
				NPCBotHandler bot = npc.getBotHandler();
				if(bot.getRecoiledDamage() < bot.maxRecoilDamage()) {

					//Deal damage back to attacker
					attacker.getCombat().getHitQueue().addPendingDamage(new HitDamage(returnDmg, HitMask.RED));

					//Increment bot recoil damage
					bot.incrementRecoiledDamage(returnDmg);
				}
			}
		}
	}

	/**
	 * Handles the spell "Vengeance" for a player.
	 * The spell returns damage to the attacker.
	 * 
	 * @param attacker
	 * @param damage
	 */
	public static void handleVengeance(Character character, Character attacker, int damage) {
		if(character.hasVengeance()) {

			int returnDmg = (int) (damage * 0.75);
			if(returnDmg <= 0) {
				return;
			}

			attacker.getCombat().getHitQueue().addPendingDamage(new HitDamage(returnDmg, HitMask.RED));
			character.forceChat("Taste Vengeance!");
			character.setHasVengeance(false);
		}
	}

	/**
	 * Handles the Guthan's set effect for a player.
	 * Wearing full guthan's has a small chance of healing the player.
	 * 
	 * @param player
	 * @param target
	 * @param damage
	 */
	public static void handleGuthans(Player player, Character target, int damage) {
		target.performGraphic(new Graphic(398));
		player.heal(damage);
	}

	/**
	 * Checks if a player should be skulled or not.
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleSkull(Player attacker, Player target) {

		if(attacker.isSkulled()) {
			return;
		}

		if(attacker.getLocation() != Locations.Location.WILDERNESS) {
			return;
		}

		//We've probably already been skulled by this player.
		if(target.getCombat().damageMapContains(attacker) 
				|| attacker.getCombat().damageMapContains(target)) {
			return;
		}

		if(target.getCombat().getAttacker() != null 
				&& target.getCombat().getAttacker() == attacker) {
			return;
		}

		if(attacker.getCombat().getAttacker() != null 
				&& attacker.getCombat().getAttacker() == target) {
			return;
		}

		skull(attacker, SkullType.WHITE_SKULL, 300);
	}

	/**
	 * Skulls the specified player
	 * @param player
	 * @param type
	 * @param seconds
	 */
	public static void skull(Player player, SkullType type, int seconds) {
		player.setSkullType(type);
		player.setSkullTimer(seconds);
		player.getUpdateFlag().flag(Flag.PLAYER_APPEARANCE);
		if(type == SkullType.RED_SKULL) {
			player.getPacketSender().sendMessage("@bla@You have received a @red@red skull@bla@! You can no longer use the Protect item prayer!");
			PrayerHandler.deactivatePrayer(player, PrayerHandler.PROTECT_ITEM);		
		} else if(type == SkullType.WHITE_SKULL) {
			player.getPacketSender().sendMessage("You've been skulled!");
		}
	}

	/**
	 * Stuns a character for the specified seconds.
	 * @param seconds
	 */
	public static void stun(Character character, int seconds) {
		if(character.getCombat().isStunned()) {
			return;
		}

		character.getCombat().getStunTimer().start(seconds);
		character.getCombat().reset();
		character.getMovementQueue().reset();
		character.performGraphic(new Graphic(348, GraphicHeight.HIGH));

		if(character.isPlayer()) {
			character.getAsPlayer().getPacketSender().sendMessage("You've been stunned!");
		}
	}

	/**
	 * Handles retalation for a character.
	 * 
	 * @param attacker
	 * @param target
	 */
	public static void handleRetaliation(Character attacker, Character target) {
		if(!CombatFactory.isAttacking(target)) {

			boolean auto_ret;

			if(target.isPlayer()) {
				auto_ret = target.getCombat().autoRetaliate() && target.getMovementQueue().isMovementDone();
			} else {
				auto_ret = target.getAsNpc().getMovementCoordinator().getCoordinateState() == NPCMovementCoordinator.CoordinateState.HOME;
			}

			if(!auto_ret) {
				return;
			}

			//Start a task, don't autoretaliate immediately
			TaskManager.submit(new Task(1, attacker, false) {
				@Override
				protected void execute() {

					//Double check reqs again
					target.getCombat().attack(attacker);

					stop();
				}						
			});
		}
	}

	/**
	 * Freezes a character.
	 * 
	 * @param character
	 * @param seconds
	 */
	public static void freeze(Character character, int seconds) {
		//Don't allow freeze more than once
		if(!character.getCombat().getFreezeTimer().finished()) {
			return;
		}

		//Make sure we're immune to spells for some time
		if(!character.getCombat().getFreezeImmunityTimer().finished()) {
			return;
		}

		//Add check for npc: Only small npcs should be freeze-able
		if(character.isNpc()) {
			//if(size > 1) {
			//return;
			//}
		}

		//Make us frozen for the amount of seconds
		character.getCombat().getFreezeTimer().start(seconds);

		//Make us immune for at least 2 seconds after freeze expired
		character.getCombat().getFreezeImmunityTimer().start(seconds + 3);

		character.getMovementQueue().reset();
		if(character.isPlayer()) {

			//Send message and effect timer to client
			character.getAsPlayer().getPacketSender().sendMessage("You have been frozen!")
			.sendEffectTimer(seconds, EffectTimer.FREEZE);

			//Actually reset combat too
			//I think it's that way on osrs
			character.getCombat().reset();
		}
	}

	/**
	 * Handles the redemption prayer effect
	 * @param attacker
	 * @param victim
	 */
	private static void handleRedemption(Character attacker, Player victim, int damage) {
		if ((victim.getHitpoints() - damage) <= (victim.getSkillManager().getMaxLevel(Skill.HITPOINTS) / 10)) {
			int amountToHeal = (int) (victim.getSkillManager().getMaxLevel(Skill.PRAYER) * .25);
			victim.performGraphic(new Graphic(436));
			victim.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
			victim.getSkillManager().setCurrentLevel(Skill.HITPOINTS, victim.getHitpoints() + amountToHeal);
			victim.getPacketSender().sendMessage("You've run out of prayer points!");
			PrayerHandler.deactivatePrayers(victim);
		}
	}

	/**
	 * Handles the smite prayer effect
	 * @param attacker
	 * @param victim
	 * @param damage
	 */
	private static void handleSmite(Character attacker, Player victim, int damage) {
		victim.getSkillManager().decreaseCurrentLevel(Skill.PRAYER, (damage / 4), 0);
	}

	/**
	 * Handles the retribution prayer effect
	 * @param killed
	 * @param killer
	 */
	public static void handleRetribution(Player killed, Player killer) {
		killed.performGraphic(new Graphic(437));
		if (killer.getPosition().isWithinDistance(killer.getPosition(),CombatConstants.RETRIBUTION_RADIUS)) {
			killer.getCombat().getHitQueue().addPendingDamage(new HitDamage(Misc.getRandom(CombatConstants.MAXIMUM_RETRIBUTION_DAMAGE), HitMask.RED));
		}
	}

	/**
	 * Checks if a player has enough ammo to perform a ranged attack
	 * @param player	The player to run the check for
	 * @return			True if player has ammo, false otherwise
	 */
	public static boolean checkAmmo(Player player, int amountRequired) {
		//Get the ranged weapon data
		final RangedData.RangedWeaponData rangedWeapon = player.getCombat().getRangedWeaponData();

		//Get the ranged ammo data
		final RangedData.AmmunitionData ammoData = player.getCombat().getAmmunition();

		if(rangedWeapon == null) {
			player.getCombat().reset();
			return false;
		}

		if(rangedWeapon == RangedData.RangedWeaponData.TOXIC_BLOWPIPE) {
			if(player.getBlowpipeScales() <= 0) {
				player.getPacketSender().sendMessage("You must recharge your Toxic blowpipe using some Zulrah scales.");
				player.getCombat().reset();
				return false;
			}
			return true;
		}

		if(ammoData == null) {
			player.getPacketSender().sendMessage("You don't have any ammunition to fire.");
			player.getCombat().reset();
			return false;
		}

		if(rangedWeapon.getType() == RangedData.RangedWeaponType.THROW) {
			return true;
		}

		Item ammoSlotItem = player.getEquipment().getItems()[Equipment.AMMUNITION_SLOT];
		if(ammoSlotItem.getId() == -1 || ammoSlotItem.getAmount() < amountRequired) {
			player.getPacketSender().sendMessage("You don't have the required amount of ammunition to fire that.");
			player.getCombat().reset();
			return false;
		}

		boolean properReq = false;

		//BAD LOOP
		for(RangedData.AmmunitionData d : rangedWeapon.getAmmunitionData()) {
			if(d == ammoData) {
				if(d.getItemId() == ammoSlotItem.getId()) {
					properReq = true;
					break;
				}
			}
		}

		if(!properReq) {
			String ammoName = ammoSlotItem.getDefinition().getName(), weaponName = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().getName(), add = !ammoName.endsWith("s") && !ammoName.endsWith("(e)") ? "s" : "";
			player.getPacketSender().sendMessage("You can not use "+ammoName+""+add+" with "+Misc.anOrA(weaponName)+" "+weaponName+".");
			player.getCombat().reset();
			return false;
		}

		return true;
	}

	/**
	 * Decrements the amount ammo the {@link Player} currently has equipped.
	 * 
	 * @param player
	 *            the player to decrement ammo for.
	 */
	public static void decrementAmmo(Player player, Position pos, int amount) {

		//Get the ranged weapon data
		final RangedData.RangedWeaponData rangedWeapon = player.getCombat().getRangedWeaponData();

		// Determine which slot we are decrementing ammo from.
		int slot = Equipment.AMMUNITION_SLOT;

		//Is the weapon using a throw weapon?
		//The ammo should be dropped from the weapon slot.
		if(rangedWeapon.getType() == RangedData.RangedWeaponType.THROW) {
			slot = Equipment.WEAPON_SLOT;
		}

		boolean accumalator = player.getEquipment().get(Equipment.CAPE_SLOT).getId() == 10499;
		if(accumalator) {
			if(Misc.getRandom(12) <= 9) {
				return;
			}
		}

		if(rangedWeapon == RangedData.RangedWeaponData.TOXIC_BLOWPIPE) {
			if(player.decrementAndGetBlowpipeScales() <= 0) {
				player.getPacketSender().sendMessage("Your Toxic blowpipe has run out of scales!");
				player.getCombat().reset();
			}
			return;
		}

		// Decrement the ammo in the selected slot.
		player.getEquipment().get(slot).decrementAmountBy(amount);

		//Drop arrows if the player isn't using an accumalator
		if(player.getCombat().getAmmunition().dropOnFloor()) {
			if(!accumalator) {
				/*for(int i = 0; i < amount; i++) {
					GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(player.getEquipment().get(slot).getId()), pos, player.getUsername(), false, 120, true, 120));
				}*/
			}
		}

		// If we are at 0 ammo remove the item from the equipment completely.
		if (player.getEquipment().get(slot).getAmount() == 0) {
			player.getPacketSender().sendMessage("You have run out of ammunition!");
			player.getEquipment().set(slot, new Item(-1));

			if (slot == Equipment.WEAPON_SLOT) {
				WeaponInterfaces.assign(player);
				player.getUpdateFlag().flag(Flag.PLAYER_APPEARANCE);
			}
		}

		// Refresh the equipment interface.
		player.getEquipment().refreshItems();
	}

	/**
	 * Determines if the entity is wearing full veracs.
	 * 
	 * @param entity
	 *            the entity to determine this for.
	 * @return true if the player is wearing full veracs.
	 */
	public static boolean fullVeracs(Character entity) {
		return entity.isNpc() ? entity.getAsNpc().getDefinition().getName().equals(
				"Verac the Defiled")
				: entity.getAsPlayer().getEquipment().containsAll(4753, 4757, 4759,
						4755);
	}

	/**
	 * Determines if the entity is wearing full dharoks.
	 * 
	 * @param entity
	 *            the entity to determine this for.
	 * @return true if the player is wearing full dharoks.
	 */
	public static boolean fullDharoks(Character entity) {
		return entity.isNpc() ? entity.getAsNpc().getDefinition().getName().equals(
				"Dharok the Wretched")
				: entity.getAsPlayer().getEquipment().containsAll(4716, 4720, 4722,
						4718);
	}

	/**
	 * Determines if the entity is wearing full karils.
	 * 
	 * @param entity
	 *            the entity to determine this for.
	 * @return true if the player is wearing full karils.
	 */
	public static boolean fullKarils(Character entity) {
		return entity.isNpc() ? entity.getAsNpc().getDefinition().getName().equals(
				"Karil the Tainted")
				: entity.getAsPlayer().getEquipment().containsAll(4732, 4736, 4738,
						4734);
	}

	/**
	 * Determines if the entity is wearing full ahrims.
	 * 
	 * @param entity
	 *            the entity to determine this for.
	 * @return true if the player is wearing full ahrims.
	 */
	public static boolean fullAhrims(Character entity) {
		return entity.isNpc() ? entity.getAsNpc().getDefinition().getName().equals(
				"Ahrim the Blighted")
				: entity.getAsPlayer().getEquipment().containsAll(4708, 4712, 4714,
						4710);
	}

	/**
	 * Determines if the entity is wearing full torags.
	 * 
	 * @param entity
	 *            the entity to determine this for.
	 * @return true if the player is wearing full torags.
	 */
	public static boolean fullTorags(Character entity) {
		return entity.isNpc() ? entity.getAsNpc().getDefinition().getName().equals(
				"Torag the Corrupted")
				: entity.getAsPlayer().getEquipment().containsAll(4745, 4749, 4751,
						4747);
	}

	/**
	 * Determines if the entity is wearing full guthans.
	 * 
	 * @param entity
	 *            the entity to determine this for.
	 * @return true if the player is wearing full guthans.
	 */
	public static boolean fullGuthans(Character entity) {
		return entity.isNpc() ? entity.getAsNpc().getDefinition().getName().equals(
				"Guthan the Infested")
				: entity.getAsPlayer().getEquipment().containsAll(4724, 4728, 4730,
						4726);
	}

	/**
	 * Calculates the combat level difference for wilderness player vs. player
	 * combat.
	 * 
	 * @param combatLevel
	 *            the combat level of the first person.
	 * @param otherCombatLevel
	 *            the combat level of the other person.
	 * @return the combat level difference.
	 */
	public static int combatLevelDifference(int combatLevel,
			int otherCombatLevel) {
		if (combatLevel > otherCombatLevel) {
			return (combatLevel - otherCombatLevel);
		} else if (otherCombatLevel > combatLevel) {
			return (otherCombatLevel - combatLevel);
		} else {
			return 0;
		}
	}
}
