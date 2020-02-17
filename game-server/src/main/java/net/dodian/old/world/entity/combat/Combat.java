package net.dodian.old.world.entity.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.dodian.old.util.Stopwatch;
import net.dodian.old.world.entity.combat.hit.HitDamageCache;
import net.dodian.old.world.entity.combat.hit.HitQueue;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.magic.CombatSpell;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.SecondsTimer;
import net.dodian.old.definitions.WeaponInterfaces;
import net.dodian.old.world.entity.combat.ranged.RangedData;

/**
 * My entity-based combat system.
 * The main class of the system.
 * @author Swiffy
 */

public class Combat {

	public Combat(Character character) {
		this.character = character;
		this.hitQueue = new HitQueue();
	}

	/**
	 * Attacks an entity by updating our current target.
	 * 
	 * @param target	The target to attack.
	 */
	public void attack(Character target) {
		//Set new target
		setTarget(target); 

		//Set facing
		if(character.getInteractingEntity() != target) {
			character.setEntityInteraction(target);
		}

		//Set following
		character.getMovementQueue().setFollowCharacter(target);
		character.getMovementQueue().follow();
	}

	/**
	 * Processes combat.
	 */
	public void onTick() {
		
		//Process the hit queue
		hitQueue.process(character);

		//Decrease attack timer
		if(attackTimer > 0) {
			attackTimer--;
		}

		//Handle attacking
		doCombat();

		//Reset attacker if we haven't been attacked in 6 seconds.
		if(lastAttack.elapsed(6000)) {
			setUnderAttack(null);
		}
	}

	/**
	 * Handles the actual combat.
	 * Attacking and following the target.
	 */
	public void doCombat() {

		if(target != null) {

			//Fetch the combat method the character will be attacking with
			method = CombatFactory.getMethod(character);

			//Follow target
			character.getMovementQueue().setFollowCharacter(target);

			//Check if the character can reach the target before attempting attack
			if(CombatFactory.canReach(character, method, target)) {
				
				//Make sure attack timer is <= 0
				if(attackTimer <= 0 || disregardDelay) {

					//Check if the character can perform the attack
					if(CombatFactory.canAttack(character, method, target)) {

						//Face target
						if(character.getInteractingEntity() != target) {
							character.setEntityInteraction(target);
						}

						//Do animation
						method.startAnimation(character);

						//Create a new {PendingHit}[] using the player's combat method (melee/range/magic)
						PendingHit[] hits = method.getHits(character, target);
						if(hits == null)
							return;

						//Perform the abstract method "preQueueAdd" before adding the hit for the target
						method.preQueueAdd(character, target);

						//Put all of the {PendingHit} in the target's HitQueue
						//And also do other things, such as reward attacker experience
						//If they're a player.
						for(PendingHit hit : hits) {
							CombatFactory.addPendingHit(hit);
						}

						//Let the method know we finished the attack
						//And perform final actions.
						//Example: After attack for magic, reset spell if player is not autocasting.
						method.finished(character);

						//Reset attack timer
						attackTimer = method.getAttackSpeed(character);
						disregardDelay = false;
					}
				}
			}
		}
	}


	/**
	 * Resets combat for the {@link Character}.
	 */
	public void reset() {

		if(target != null) {

			if(character.getInteractingEntity() == target) {
				character.setEntityInteraction(null);
			}

			if(character.getMovementQueue().getFollowCharacter() == target) {
				character.getMovementQueue().setFollowCharacter(null);
			}

			target = null;
		}
	}

	/**
	 * Adds damage to the damage map, as long as the argued amount of damage is
	 * above 0 and the argued entity is a player.
	 * 
	 * @param entity
	 *            the entity to add damage for.
	 * @param amount
	 *            the amount of damage to add for the argued entity.
	 */
	public void addDamage(Character entity, int amount) {

		if (amount <= 0 || entity.isNpc()) {
			return;
		}

		Player player = (Player) entity;
		if (damageMap.containsKey(player)) {
			damageMap.get(player).incrementDamage(amount);
			return;
		}

		damageMap.put(player, new HitDamageCache(amount));
	}

	/**
	 * Performs a search on the <code>damageMap</code> to find which
	 * {@link Player} dealt the most damage on this controller.
	 * 
	 * @param clearMap
	 *            <code>true</code> if the map should be discarded once the
	 *            killer is found, <code>false</code> if no data in the map
	 *            should be modified.
	 * @return the player who killed this entity, or <code>null</code> if an npc
	 *         or something else killed this entity.
	 */
	public Player getKiller(boolean clearMap) {

		// Return null if no players killed this entity.
		if (damageMap.size() == 0) {
			return null;
		}

		// The damage and killer placeholders.
		int damage = 0;
		Player killer = null;

		for (Entry<Player, HitDamageCache> entry : damageMap.entrySet()) {

			// Check if this entry is valid.
			if (entry == null) {
				continue;
			}

			// Check if the cached time is valid.
			long timeout = entry.getValue().getStopwatch().elapsed();
			if (timeout > CombatConstants.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			// Check if the key for this entry has logged out.
			Player player = entry.getKey();
			if (!player.isRegistered()) {
				continue;
			}

			// If their damage is above the placeholder value, they become the
			// new 'placeholder'.
			if (entry.getValue().getDamage() > damage) {
				damage = entry.getValue().getDamage();
				killer = entry.getKey();
			}
		}

		// Clear the damage map if needed.
		if (clearMap)
			damageMap.clear();

		// Return the killer placeholder.
		return killer;
	}

	public boolean damageMapContains(Player player) {
		return damageMap.containsKey(player);
	}

	//The user's attack timer
	private int attackTimer;

	//Should we disregard attack timers?
	private boolean disregardDelay;

	//The user's HitQueue
	private HitQueue hitQueue;

	//The user's damage map
	private final Map<Player, HitDamageCache> damageMap = new HashMap<>();

	//The character
	private Character character;

	//The character's current target
	private Character target;

	//The last person who attacked the character this instance belongs to.
	private Character attacker;

	//The timer of the last attack which occured
	private final Stopwatch lastAttack = new Stopwatch();

	//The last combat method used
	private CombatMethod method;

	//Fight type
	private FightType fightType = FightType.UNARMED_KICK;

	//WeaponInterface
	private WeaponInterfaces.WeaponInterface weapon;

	//Autoretaliate
	private boolean autoRetaliate;

	//Ranged data
	public RangedData.RangedWeaponData rangeWeaponData;
	public RangedData.AmmunitionData rangeAmmoData;

	//Magic data
	private CombatSpell castSpell;
	private CombatSpell autoCastSpell;
	private CombatSpell previousCast;

	//Timers
	private final SecondsTimer poisonImmunityTimer = new SecondsTimer();
	private final SecondsTimer fireImmunityTimer = new SecondsTimer();
	private final SecondsTimer teleblockTimer = new SecondsTimer();
	private final SecondsTimer prayerBlockTimer = new SecondsTimer();
	private final SecondsTimer freezeTimer = new SecondsTimer();
	private final SecondsTimer freezeImmunityTimer = new SecondsTimer();
	private final SecondsTimer stunTimer = new SecondsTimer();
	
	/** Getters and setters **/

	public Character getCharacter() {
		return character;
	}

	public Character getTarget() {
		return target;
	}

	public void setTarget(Character target) {
		this.target = target;
	}

	public int getAttackTimer() {
		return attackTimer;
	}

	public void setAttackTimer(int attackTimer) {
		this.attackTimer = attackTimer;
	}

	public HitQueue getHitQueue() {
		return hitQueue;
	}

	public Character getAttacker() {
		return attacker;
	}

	public void setUnderAttack(Character attacker) {
		this.attacker = attacker;
		this.lastAttack.reset();
	}

	public CombatSpell getCastSpell() {
		return castSpell;
	}

	public void setCastSpell(CombatSpell castSpell) {
		this.castSpell = castSpell;
	}

	public CombatSpell getAutocastSpell() {
		return autoCastSpell;
	}


	public void setAutocastSpell(CombatSpell autoCastSpell) {
		this.autoCastSpell = autoCastSpell;
	}

	public CombatSpell getPreviousCast() {
		return previousCast;
	}

	public void setPreviousCast(CombatSpell previousCast) {
		this.previousCast = previousCast;
	}

	public RangedData.RangedWeaponData getRangedWeaponData() {
		return rangeWeaponData;
	}

	public void setRangedWeaponData(RangedData.RangedWeaponData rangedWeaponData) {
		this.rangeWeaponData = rangedWeaponData;
	}

	public RangedData.AmmunitionData getAmmunition() {
		return rangeAmmoData;
	}

	public void setAmmunition(RangedData.AmmunitionData rangeAmmoData) {
		this.rangeAmmoData = rangeAmmoData;
	}

	public WeaponInterfaces.WeaponInterface getWeapon() {
		return weapon;
	}

	public void setWeapon(WeaponInterfaces.WeaponInterface weapon) {
		this.weapon = weapon;
	}

	public FightType getFightType() {
		return fightType;
	}

	public void setFightType(FightType fightType) {
		this.fightType = fightType;
	}

	public boolean autoRetaliate() {
		return autoRetaliate;
	}

	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	public SecondsTimer getPoisonImmunityTimer() {
		return poisonImmunityTimer;
	}

	public SecondsTimer getFireImmunityTimer() {
		return fireImmunityTimer;
	}

	public SecondsTimer getTeleBlockTimer() {
		return teleblockTimer;
	}

	public SecondsTimer getPrayerBlockTimer() {
		return prayerBlockTimer;
	}

	public SecondsTimer getFreezeTimer() {
		return freezeTimer;
	}

	public void setDisregardDelay(boolean disregardDelay) {
		this.disregardDelay = disregardDelay;
	}

	public boolean disregardDelay() {
		return disregardDelay;
	}

	public SecondsTimer getFreezeImmunityTimer() {
		return freezeImmunityTimer;
	}
	
	public boolean isStunned() {
		return !stunTimer.finished();
	}

	public SecondsTimer getStunTimer() {
		return stunTimer;
	}
	
	public Stopwatch getLastAttack() {
		return lastAttack;
	}
}
