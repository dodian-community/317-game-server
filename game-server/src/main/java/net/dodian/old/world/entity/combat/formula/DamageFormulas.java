package net.dodian.old.world.entity.combat.formula;

import net.dodian.old.world.content.PrayerHandler;
import net.dodian.old.world.entity.combat.CombatEquipment;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.FightStyle;
import net.dodian.old.world.entity.combat.magic.CombatSpell;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.container.impl.Equipment;
import net.dodian.old.world.model.equipment.BonusManager;
import net.dodian.old.world.entity.combat.ranged.RangedData;

public class DamageFormulas {

	/*==============================================================================*/
	/*===================================MELEE=====================================*/

	public static int calculateMaxMeleeHit(Character entity) {
		double maxHit = 0;

		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			maxHit = npc.getDefinition().getMaxHit();
		} else {
			Player player = (Player) entity;

			double base = 0;
			double effective = getEffectiveStr(player);
			double strengthBonus = player.getBonusManager().getOtherBonus()[BonusManager.BONUS_STRENGTH];
			double specialBonus = 1;

			//Special effects also affect maxhit
			if (player.isSpecialActivated() && player.getCombatSpecial().getCombatMethod().getCombatType() == CombatType.MELEE) {
				specialBonus = player.getCombatSpecial().getStrengthBonus();
			}

			//Use our multipliers to adjust the maxhit...
			base = (13 + effective + (strengthBonus / 8) + ((effective * strengthBonus) / 65)) / 11;

			if (specialBonus > 1) {
				base = (base * specialBonus);
			}

			//Obsidian effect is +10% damage
			if (CombatEquipment.wearingObsidian(player)) {
				base = (base * 1.2);
			}

			//Void melee is +10% damage
			if(CombatEquipment.wearingVoid(player, CombatType.MELEE)) {
				base = (base * 1.1);
			}

			maxHit = (base *= 1);

		}

		//Dharoks effect
		if(CombatFactory.fullDharoks(entity)) {
			int hitpoints = entity.getHitpoints();
			if(entity.isNpc()) {
				maxHit += (int) ((int)(entity.getAsNpc().getDefinition().getHitpoints() - hitpoints) * 0.2);
			} else {
				maxHit += (int) ((int)(entity.getAsPlayer().getSkillManager().getMaxLevel(Skill.HITPOINTS) - hitpoints) * 0.45) + 1;
			}
		}

		return (int) Math.floor(maxHit);
	}

	public static double getEffectiveStr(Player plr) {
		int styleBonus = 0;
		FightStyle style = plr.getCombat().getFightType().getStyle();

		if(style == FightStyle.AGGRESSIVE
				|| style == FightStyle.ACCURATE) {
			styleBonus = 3;
		} else if(style == FightStyle.CONTROLLED) {
			styleBonus = 1;
		}

		double prayerMod = 1.0;

		if(PrayerHandler.isActivated(plr, PrayerHandler.BURST_OF_STRENGTH)) {
			prayerMod = 1.05;
		} else if(PrayerHandler.isActivated(plr, PrayerHandler.SUPERHUMAN_STRENGTH)) {
			prayerMod = 1.1;
		} else if(PrayerHandler.isActivated(plr, PrayerHandler.ULTIMATE_STRENGTH)) {
			prayerMod = 1.15;
		} else if(PrayerHandler.isActivated(plr, PrayerHandler.CHIVALRY)) {
			prayerMod = 1.18;
		} else if(PrayerHandler.isActivated(plr, PrayerHandler.PIETY)) {
			prayerMod = 1.23;
		}

		return ((plr.getSkillManager().getCurrentLevel(Skill.STRENGTH)) * prayerMod) + styleBonus;		
	}

	/**
	 * Calculates a player's magic max hit
	 * @return			The player's magic max hit damage
	 */
	public static int getMagicMaxhit(Character c) {
		int maxHit = 0;

		CombatSpell spell = c.getCombat().getCastSpell();
		if(spell == null) {
			spell = c.getCombat().getAutocastSpell();
		}
		
		if(spell != null) {

			if(spell.maximumHit() > 0) {

				maxHit = spell.maximumHit();

			} else {

				if(c.isNpc()) {
					maxHit = c.getAsNpc().getDefinition().getMaxHit();
				} else {
					maxHit = 1;
				}

			}
		} else {
			if(c.isNpc()) {
				maxHit = c.getAsNpc().getDefinition().getMaxHit();
			}
		}

		if(c.isPlayer()) {
			switch (c.getAsPlayer().getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
			case 4675: 
			case 6914:
				maxHit *= 1.10;
				break;
			case 11791:
				maxHit *= 1.15;
				break;
			}
		}

		return (int) Math.floor(maxHit);
	}


	/**
	 * Calculates the maximum ranged hit for the argued {@link Character} without
	 * taking the victim into consideration.
	 * 
	 * @param entity
	 *            the entity to calculate the maximum hit for.
	 * @return the maximum ranged hit that this entity can deal.
	 */
	public static int calculateMaxRangedHit(Character entity) {
		double maxHit = 0;

		if (entity.isNpc()) {
			NPC npc = (NPC) entity;
			maxHit = npc.getDefinition().getMaxHit();
		} else {
			Player player = (Player) entity;

			double prayerMultiplier = 1;
			int combatStyleBonus = 2;
			int rangeLevel = player.getSkillManager().getCurrentLevel(Skill.RANGED);
			int rangedStrength = ((int) player.getBonusManager().getAttackBonus()[4] / 10);
			//Include the arrows strength..

			if(player.getCombat().getAmmunition() != null) {
				rangedStrength += (player.getCombat().getAmmunition().getStrength());
			}

			//Boost blowpipe
			if(player.getCombat().getRangedWeaponData() != null) {
				if(player.getCombat().getRangedWeaponData() == RangedData.RangedWeaponData.TOXIC_BLOWPIPE) {
					rangedStrength += 35;
				}
			}
			
			//Include attack style... Accurate/long range hits harder.
			if(player.getCombat().getFightType().getStyle() == FightStyle.ACCURATE) {
				combatStyleBonus = 3;
			} else if(player.getCombat().getFightType().getStyle() == FightStyle.DEFENSIVE) {
				combatStyleBonus = 1;
			}

			//Do calculations of maxhit...
			int effectiveRangeDamage = (int) ((rangeLevel * prayerMultiplier) + combatStyleBonus);
			maxHit = 1.3 + (effectiveRangeDamage / 10) + (rangedStrength / 80) + ((effectiveRangeDamage * rangedStrength) / 640);

			//We now have the maxhit and can increase its damage...

			//Void hits 20% more
			if (CombatEquipment.wearingVoid(player, CombatType.RANGED)) {
				maxHit *= 1.2;
			}

			//Prayers
			double prayerMod = 1.0;
			if (PrayerHandler.isActivated(player, PrayerHandler.SHARP_EYE)) {
				prayerMod = 1.05;
			} else if (PrayerHandler.isActivated(player, PrayerHandler.HAWK_EYE)) {
				prayerMod = 1.10;
			} else if (PrayerHandler.isActivated(player, PrayerHandler.EAGLE_EYE)) {
				prayerMod = 1.15;
			} else if(PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
				prayerMod = 1.23;
			}
			maxHit *= prayerMod;

			//Special attacks!
			if (player.isSpecialActivated() && player.getCombatSpecial().getCombatMethod().getCombatType() == CombatType.RANGED) {
				maxHit *= player.getCombatSpecial().getStrengthBonus();
			}

		}

		return (int) Math.floor(maxHit);
	}
}
