package net.dodian.old.world.entity.combat;

import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Equipment;

public class CombatEquipment {

	/**
	 * Is the player wearing obsidian?
	 * @param player		The player.
	 * @return				true if player is wearing obsidian, false otherwise.
	 */
	public static boolean wearingObsidian(Player player) {
		if (player.getEquipment().getItems()[2].getId() != 11128) 
			return false;

		for (int weapon : OBSIDIAN_WEAPONS) {
			if (player.getEquipment().getItems()[3].getId() == weapon)  {
				return true;
			}
		}
		return false;
	}

	/**
	 * Is the player wearing void?
	 * @param player		The player.
	 * @return				true if player is wearing void, false otherwise.
	 */
	public static boolean wearingVoid(Player player, CombatType attackType) {
		int correctEquipment = 0;
		int helmet = attackType == CombatType.MAGIC ? MAGE_VOID_HELM :
			attackType == CombatType.RANGED ? RANGED_VOID_HELM : MELEE_VOID_HELM;
		for (int armour[] : VOID_ARMOUR) {
			if (player.getEquipment().getItems()[armour[0]].getId() == armour[1]) {
				correctEquipment++;
			}
		}
		if (player.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == VOID_KNIGHT_DEFLECTOR) {
			correctEquipment++;
		}
		return correctEquipment >= 3 && player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == helmet;
	}

	public static final int MAGE_VOID_HELM = 11663;

	public static final int RANGED_VOID_HELM = 11664;

	public static final int MELEE_VOID_HELM = 11665;

	private static final int VOID_KNIGHT_DEFLECTOR = 19712;

	public static final int[][] VOID_ARMOUR = {
			{Equipment.BODY_SLOT, 8839},
			{Equipment.LEG_SLOT, 8840},
			{Equipment.HANDS_SLOT, 8842}
	};


	public static final int[] OBSIDIAN_WEAPONS = {
			746, 747, 6523, 6525, 6526, 6527, 6528
	};
}
