package net.dodian.old.world.entity.combat;

import java.util.Arrays;

import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.engine.task.impl.RestoreSpecialAttackTask;
import net.dodian.old.world.content.Dueling.DuelRule;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.AbyssalBludgeonCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.AbyssalDaggerCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.AbyssalTentacleCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.AbyssalWhipCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.ArmadylCrossbowCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.ArmadylGodswordCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.BandosGodswordCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.BarrelchestAnchorCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.DarkBowCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.DragonClawCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.DragonDaggerCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.DragonHalberdCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.DragonLongswordCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.DragonMaceCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.DragonScimitarCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.DragonWarhammerCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.GraniteMaulCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.MagicShortbowCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.SaradominGodswordCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.SaradominSwordCombatMethod;
import net.dodian.old.world.entity.combat.method.impl.specials.ZamorakGodswordCombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Equipment;
import net.dodian.old.world.model.dialogue.DialogueManager;
import net.dodian.old.definitions.WeaponInterfaces;

/**
 * Holds constants that hold data for all of the special attacks that can be
 * used.
 * 
 * @author lare96
 */
public enum CombatSpecial {

	//Melee
	ABYSSAL_WHIP(new int[] { 4151, 21371, 15441, 15442, 15443, 15444 }, 50, 1.04, 1, new AbyssalWhipCombatMethod(), WeaponInterfaces.WeaponInterface.WHIP),
	ABYSSAL_TENTACLE(new int[] { 12006 }, 50, 1.07, 1, new AbyssalTentacleCombatMethod(), WeaponInterfaces.WeaponInterface.WHIP),

	BARRELSCHEST_ANCHOR(new int[] { 10887 }, 50, 1.22, 1.35, new BarrelchestAnchorCombatMethod(), WeaponInterfaces.WeaponInterface.WARHAMMER),
	DRAGON_SCIMITAR(new int[] { 4587 }, 55, 1.08, 1.1, new DragonScimitarCombatMethod(), WeaponInterfaces.WeaponInterface.SCIMITAR),
	DRAGON_LONGSWORD(new int[] { 1305 }, 25, 1.31, 1.33, new DragonLongswordCombatMethod(), WeaponInterfaces.WeaponInterface.LONGSWORD),
	DRAGON_MACE(new int[] { 1434 }, 25, 1.61, 1.25, new DragonMaceCombatMethod(), WeaponInterfaces.WeaponInterface.MACE),
	DRAGON_WARHAMMER(new int[] { 13576 }, 50, 1.67, 1.39, new DragonWarhammerCombatMethod(), WeaponInterfaces.WeaponInterface.WARHAMMER),

	SARADOMIN_SWORD(new int[] { 11838 }, 100, 1.1, 1.3, new SaradominSwordCombatMethod(), WeaponInterfaces.WeaponInterface.SARADOMIN_SWORD),

	ARMADYL_GODSWORD(new int[] { 11802 }, 50, 1.50, 1.6, new ArmadylGodswordCombatMethod(), WeaponInterfaces.WeaponInterface.GODSWORD),
	SARADOMIN_GODSWORD(new int[] { 11806 }, 50, 1.27, 1.5, new SaradominGodswordCombatMethod(), WeaponInterfaces.WeaponInterface.GODSWORD),
	BANDOS_GODSWORD(new int[] { 11804 }, 100, 1.22, 1.4, new BandosGodswordCombatMethod(), WeaponInterfaces.WeaponInterface.GODSWORD),
	ZAMORAK_GODSWORD(new int[] { 11808 }, 50, 1.15, 1.4, new ZamorakGodswordCombatMethod(), WeaponInterfaces.WeaponInterface.GODSWORD),

	ABYSSAL_BLUDGEON(new int[] { 13263 }, 50, 1.20, 1.37, new AbyssalBludgeonCombatMethod(), WeaponInterfaces.WeaponInterface.ABYSSAL_BLUDGEON),
	
	//Multiple hits 
	DRAGON_HALBERD(new int[] { 3204 }, 30, 1, 1.19, new DragonHalberdCombatMethod(), WeaponInterfaces.WeaponInterface.HALBERD),
	DRAGON_DAGGER(new int[] { 1215, 1231, 5680, 5698 }, 25, 1.27, 1.3, new DragonDaggerCombatMethod(), WeaponInterfaces.WeaponInterface.DRAGON_DAGGER),
	ABYSSAL_DAGGER(new int[] { 13271 }, 50, 1.10, 1.37, new AbyssalDaggerCombatMethod(), WeaponInterfaces.WeaponInterface.ABYSSAL_DAGGER),
	GRANITE_MAUL(new int[] { 4153, 12848 }, 50, 1.12, 1.18, new GraniteMaulCombatMethod(), WeaponInterfaces.WeaponInterface.GRANITE_MAUL),
	DRAGON_CLAWS(new int[] { 13652 }, 50, 1.3, 1.53, new DragonClawCombatMethod(), WeaponInterfaces.WeaponInterface.CLAWS),

	//Ranged
	MAGIC_SHORTBOW(new int[] { 861 }, 55, 1.03, 1.2, new MagicShortbowCombatMethod(), WeaponInterfaces.WeaponInterface.SHORTBOW),
	DARK_BOW(new int[] { 11235 }, 55, 1.29, 1.24, new DarkBowCombatMethod(), WeaponInterfaces.WeaponInterface.DARK_BOW),
	ARMADYL_CROSSBOW(new int[]{11785}, 40, 1.30, 2.0, new ArmadylCrossbowCombatMethod(), WeaponInterfaces.WeaponInterface.CROSSBOW),
	;

	/** The weapon ID's that perform this special when activated. */
	private int[] identifiers;

	/** The amount of special energy this attack will drain. */
	private int drainAmount;

	/** The strength bonus when performing this special attack. */
	private double strengthBonus;

	/** The accuracy bonus when performing this special attack. */
	private double accuracyBonus;

	/** The combat type used when performing this special attack. */
	private CombatMethod combatMethod;

	/** The weapon interface used by the identifiers. */
	private WeaponInterfaces.WeaponInterface weaponType;

	/**
	 * Create a new {@link CombatSpecial}.
	 * 
	 * @param drainAmount
	 *            the amount of special energy this attack will drain.
	 * @param strengthBonus
	 *            the strength bonus when performing this special attack.
	 * @param accuracyBonus
	 *            the accuracy bonus when performing this special attack.
	 * @param combatMethod
	 *            the combat type used when performing this special attack.
	 * @param weaponType
	 *            the weapon interface used by the identifiers.
	 */
	private CombatSpecial(int[] identifiers, int drainAmount,
			double strengthBonus, double accuracyBonus, CombatMethod combatMethod,
			WeaponInterfaces.WeaponInterface weaponType) {
		this.identifiers = identifiers;
		this.drainAmount = drainAmount;
		this.strengthBonus = strengthBonus;
		this.accuracyBonus = accuracyBonus;
		this.combatMethod = combatMethod;
		this.weaponType = weaponType;
	}

	/**
	 * Checks if a player has the reqs to perform the special attack
	 * @param player
	 * @param special
	 * @return
	 */
	public static boolean checkSpecial(Player player, CombatSpecial special) {
		return (player.getCombatSpecial() != null 
				&& player.getCombatSpecial() == special 
				&& player.isSpecialActivated() 
				&& player.getSpecialPercentage() >= special.getDrainAmount());
	}

	/**
	 * Drains the special bar for the argued {@link Character}.
	 * 
	 * @param character
	 *            the character who's special bar will be drained.
	 * @param amount
	 *            the amount of energy to drain from the special bar.
	 */
	public static void drain(Character character, int amount) {
		character.decrementSpecialPercentage(amount);
		character.setSpecialActivated(false);

		if(!character.isRecoveringSpecialAttack()) {
			TaskManager.submit(new RestoreSpecialAttackTask(character));
		}

		if(character.isPlayer()) {
			Player p = character.getAsPlayer();
			CombatSpecial.updateBar(p);
		}
	}

	/**
	 * Updates the special bar with the amount of special energy the argued
	 * {@link Player} has.
	 * 
	 * @param player
	 *            the player who's special bar will be updated.
	 */
	public static void updateBar(Player player) {
		if (player.getCombat().getWeapon().getSpecialBar() == -1 || player.getCombat().getWeapon().getSpecialMeter() == -1) {
			return;
		}
		int specialCheck = 10;
		int specialBar = player.getCombat().getWeapon().getSpecialMeter();
		int specialAmount = player.getSpecialPercentage() / 10;

		for (int i = 0; i < 10; i++) {
			player.getPacketSender().sendInterfaceComponentMoval(specialAmount >= specialCheck ? 500 : 0, 0, --specialBar);
			specialCheck--;
		}
		player.getPacketSender().updateSpecialAttackOrb().sendString(player.getCombat().getWeapon().getSpecialMeter(), player.isSpecialActivated() ? ("@yel@ Special Attack (" + player.getSpecialPercentage() + "%)") : ("@bla@ Special Attack (" +player.getSpecialPercentage()+ "%)"));
	}

	/**
	 * Assigns special bars to the attack style interface if needed.
	 * 
	 * @param player
	 *            the player to assign the special bar for.
	 */
	public static void assign(Player player) {
		if (player.getCombat().getWeapon().getSpecialBar() == -1) {
			player.setSpecialActivated(false);
			player.setCombatSpecial(null);
			CombatSpecial.updateBar(player);
			return;
		}

		for (CombatSpecial c : CombatSpecial.values()) {
			if (player.getCombat().getWeapon() == c.getWeaponType()) {
				if (Arrays.stream(c.getIdentifiers()).anyMatch(
						id -> player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == id)) {
					player.getPacketSender().sendInterfaceDisplayState(player.getCombat().getWeapon().getSpecialBar(), false);
					player.setCombatSpecial(c);
					return;
				}
			}
		}

		player.getPacketSender().sendInterfaceDisplayState(player.getCombat().getWeapon().getSpecialBar(), true);
		player.setCombatSpecial(null);
		player.setSpecialActivated(false);
	}

	/**
	 * Activates a player's special attack.
	 * @param player
	 */
	public static void activate(Player player) {

		//Make sure the player has a valid special attack
		if (player.getCombatSpecial() == null) {
			return;
		}

		//Duel, disabled special attacks?
		if(player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_SPECIAL_ATTACKS.ordinal()]) {
			DialogueManager.sendStatement(player, "Special attacks have been disabled in this duel!");
			return;
		}

		//Check if player has already activated special attack,
		//If that's the case - turn if off.
		if (player.isSpecialActivated()) {
			player.setSpecialActivated(false);
			CombatSpecial.updateBar(player);
		} else {

			//Get the special attack..
			final CombatSpecial spec = player.getCombatSpecial();

			//Set special attack activated
			player.setSpecialActivated(true);

			//Update special bar
			CombatSpecial.updateBar(player);

			//Handle instant special attacks here.
			//Example: Granite Maul, Dragon battleaxe...
			if(spec == CombatSpecial.GRANITE_MAUL) {

				//Make sure the player has enough special attack
				if (player.getSpecialPercentage() < player.getCombatSpecial().getDrainAmount()) {
					player.getPacketSender().sendMessage(
							"You do not have enough special attack energy left!");
					player.setSpecialActivated(false);
					CombatSpecial.updateBar(player);
					return;
				}

				//Check if the player is attacking and using Melee..
				if(CombatFactory.isAttacking(player) && CombatFactory.getMethod(player).getCombatType() == CombatType.MELEE) {

					//Handle an immediate attack by indicating
					//that we should disregard delay...
					player.getCombat().setDisregardDelay(true);
					player.getCombat().doCombat();
					return;
				} else {

					//Uninformed player using gmaul without being in combat..
					//Teach them a lesson!
					player.getPacketSender().sendMessage("Although not required, the Granite maul special attack should be used during").sendMessage("combat for maximum effect.");
				}			
			} /* else if(spec == CombatSpecial.DRAGON_BATTLEAXE) {

			}*/

		}
	}

	/**
	 * Gets the weapon ID's that perform this special when activated.
	 * 
	 * @return the weapon ID's that perform this special when activated.
	 */
	public int[] getIdentifiers() {
		return identifiers;
	}

	/**
	 * Gets the amount of special energy this attack will drain.
	 * 
	 * @return the amount of special energy this attack will drain.
	 */
	public int getDrainAmount() {
		return drainAmount;
	}

	/**
	 * Gets the strength bonus when performing this special attack.
	 * 
	 * @return the strength bonus when performing this special attack.
	 */
	public double getStrengthBonus() {
		return strengthBonus;
	}

	/**
	 * Gets the accuracy bonus when performing this special attack.
	 * 
	 * @return the accuracy bonus when performing this special attack.
	 */
	public double getAccuracyBonus() {
		return accuracyBonus;
	}

	/**
	 * Gets the combat type used when performing this special attack.
	 * 
	 * @return the combat type used when performing this special attack.
	 */
	public CombatMethod getCombatMethod() {
		return combatMethod;
	}

	/**
	 * Gets the weapon interface used by the identifiers.
	 * 
	 * @return the weapon interface used by the identifiers.
	 */
	public WeaponInterfaces.WeaponInterface getWeaponType() {
		return weaponType;
	}
}
