package net.dodian.old.world.model.equipment;

import net.dodian.Server;
import net.dodian.old.world.entity.combat.formula.DamageFormulas;
import net.dodian.old.world.entity.combat.ranged.RangedData;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import net.dodian.orm.models.definitions.ItemDefinition;

/**
 * Represents a bonus manager.
 * Handles a player's equipment bonuses.
 * 
 * @author Professor Oak
 */
public class BonusManager {

	/**
	 * Opens the interface which displays the player's bonuses.
	 * @param player
	 */
	public static void open(Player player) {
		player.getPacketSender().sendInterface(INTERFACE_ID);
		BonusManager.update(player);
	}

	/**
	 * Updates bonuses for a player.
	 * @param player
	 */
	public static void update(Player player) {
		double[] bonuses = new double[18];
		for (Item item : player.getEquipment().getItems()) {
			ItemDefinition definition = Server.getDefinitionsHandler().getItemDefinitionById(item.getId());
			if(definition.getBonuses() != null) {
				for (int i = 0; i < definition.getBonuses().length; i++) {
					bonuses[i] += definition.getBonuses()[i];
				}
			}
		}
		for (int i = 0; i < STRING_ID.length; i++) {
			if (i <= 4) {
				player.getBonusManager().attackBonus[i] = bonuses[i];
			} else if (i <= 9) {
				int index = i - 5;
				player.getBonusManager().defenceBonus[index] = bonuses[i];
			} else {
				int index = i - 10;
				player.getBonusManager().otherBonus[index] = bonuses[i];
			}
			player.getPacketSender().sendString(Integer.parseInt(STRING_ID[i][0]), STRING_ID[i][1] + ": " + bonuses[i]);
		}


		/**
		 * Update maxhit frames on the interface.
		 */
		if(player.getInterfaceId() == INTERFACE_ID) {
			
			//Update some combat data first,
			//including ranged ammunition/weapon
			player.getCombat().setAmmunition(RangedData.AmmunitionData.getFor(player));
			player.getCombat().setRangedWeaponData(RangedData.RangedWeaponData.getFor(player));
			
			player.getPacketSender().sendString(MELEE_MAXHIT_FRAME, "Melee maxhit: "+getDamageString(DamageFormulas.calculateMaxMeleeHit(player)));
			player.getPacketSender().sendString(RANGED_MAXHIT_FRAME, "Ranged maxhit: "+getDamageString(DamageFormulas.calculateMaxRangedHit(player)));
			player.getPacketSender().sendString(MAGIC_MAXHIT_FRAME, "Magic maxhit: "+getDamageString(DamageFormulas.getMagicMaxhit(player)));
		}
	}

	private static String getDamageString(int damage) {
		if(damage == 0) {
			return "---";
		}
		if(damage <= 10) {
			return "@red@"+damage;
		}
		if(damage <= 25) {
			return "@yel@"+damage;
		}
		return "@gre@"+damage;
	}

	public double[] getAttackBonus() {
		return attackBonus;
	}

	public double[] getDefenceBonus() {
		return defenceBonus;
	}

	public double[] getOtherBonus() {
		return otherBonus;
	}

	private double[] attackBonus = new double[5];

	private double[] defenceBonus = new double[5];

	private double[] otherBonus = new double[2];

	private static final String[][] STRING_ID = {
			{"1675", "Stab"},
			{"1676", "Slash"},
			{"1677", "Crush"},
			{"1678", "Magic"},
			{"1679", "Range"},

			{"1680", "Stab"},
			{"1681", "Slash"},
			{"1682", "Crush"},
			{"1683", "Magic"},
			{"1684", "Range"},

			{"1686", "Strength"},
			{"1687", "Prayer"},
	};

	public static final int 
	ATTACK_STAB = 0, 
	ATTACK_SLASH = 1,
	ATTACK_CRUSH = 2, 
	ATTACK_MAGIC = 3, 
	ATTACK_RANGE = 4, 

	DEFENCE_STAB = 0, 
	DEFENCE_SLASH = 1, 
	DEFENCE_CRUSH = 2, 
	DEFENCE_MAGIC = 3,
	DEFENCE_RANGE = 4,

	BONUS_STRENGTH = 0,
	BONUS_PRAYER = 1;

	private static final int MELEE_MAXHIT_FRAME = 15115;
	private static final int RANGED_MAXHIT_FRAME = 15116;
	private static final int MAGIC_MAXHIT_FRAME = 15117;

	private static final int INTERFACE_ID = 15106;

}
