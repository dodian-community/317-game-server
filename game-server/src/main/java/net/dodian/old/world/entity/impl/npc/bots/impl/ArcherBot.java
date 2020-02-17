package net.dodian.old.world.entity.impl.npc.bots.impl;

import net.dodian.old.util.Misc;
import net.dodian.old.world.content.Food.FoodType;
import net.dodian.old.world.content.PrayerHandler;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.bountyhunter.Emblem;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.combat.ranged.RangedData.AmmunitionData;
import net.dodian.old.world.entity.combat.ranged.RangedData.RangedWeaponData;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.npc.bots.NPCBotHandler;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.old.world.model.GroundItem;
import net.dodian.old.world.model.Item;

/**
 * Represents a simple archer bot.
 * @author Professor Oak
 */
public class ArcherBot extends NPCBotHandler {

	private boolean ramboMode;
	private int ramboShots;

	public ArcherBot(NPC npc) {
		super(npc);
		npc.setHasVengeance(true);
		npc.getCombat().setAmmunition(AmmunitionData.RUNE_JAVELIN);
		npc.getCombat().setRangedWeaponData(RangedWeaponData.BALLISTA);	
	}

	@Override
	public void process() {

		//Check if npc is in combat..
		if(CombatFactory.inCombat(npc)) {

			//Make sure our opponent is valid..
			Player opponent = getOpponent();
			if(opponent == null) {
				return;
			}

			//Are we in distance to the opponent?
			final boolean inDistance = (npc.getPosition().getDistance(opponent.getPosition()) <= getMethod().getAttackDistance(npc));

			//Activate prayers..
			PrayerHandler.activatePrayer(npc, PrayerHandler.EAGLE_EYE);
			PrayerHandler.activatePrayer(npc, PrayerHandler.STEEL_SKIN);
			
			//Activate any overheads..
			int overhead = getOverheadPrayer(opponent, inDistance);
			if(overhead != -1) {

				//Activate overhead!
				PrayerHandler.activatePrayer(npc, overhead);

			} else {

				//We shouldn't be using any overhead.
				//Make sure to turn off any headicons.
				if(npc.getHeadIcon() != -1) {
					npc.setHeadIcon(-1);
				}
			}

			//Eat whenever we need to.
			if(npc.getHitpoints() > 0) {
				if(npc.getHitpoints() < 40 + Misc.getRandom(15)) {
					if(getEatCounter() < 28) {
						super.eat(FoodType.SHARK, 1100);
					}
				}

				//Cast vengeance when ever we can.
				super.castVengeance();

				//Sometimes go nuts
				if(Misc.getRandom(20) == 1) {
					ramboMode = true;
				}				
				if(ramboMode) {

					npc.forceChat("Raaaaaarrrrgggghhhhhh!");
					npc.getCombat().setDisregardDelay(true);

					if(ramboShots++ >= 1) {
						ramboShots = 0;
						ramboMode = false;
					}
				}

			} else {
				npc.forceChat("Gg");
			}

		} else {

			//Turn off prayers
			if(npc.getHeadIcon() != -1 || PrayerHandler.isActivated(npc, PrayerHandler.EAGLE_EYE)
					|| PrayerHandler.isActivated(npc, PrayerHandler.STEEL_SKIN)) {
				PrayerHandler.deactivatePrayers(npc);
			}

			//Reset all attributes
			super.reset();
		}
	}

	@Override
	public void onDeath(Player killer) {
		//There should be a small chance of receiving a tier 1 emblem
		if(Misc.getRandom(18) == 1) {
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(Emblem.MYSTERIOUS_EMBLEM_1.id, 1), npc.getPosition(), killer.getUsername(), killer.getHostAddress(), false, 150, false, -1));
			killer.getPacketSender().sendMessage("@red@You have been awarded with a mysterious emblem for successfully killing the bot.");
		} else {
			killer.getPacketSender().sendMessage("The bot didn't drop anything. Maybe you're more lucky next time.");
		}
	}

	@Override
	public int maxRecoilDamage() {
		return 100;
	}

	@Override
	public CombatMethod getMethod() {
		return CombatFactory.RANGED_COMBAT;
	}
}