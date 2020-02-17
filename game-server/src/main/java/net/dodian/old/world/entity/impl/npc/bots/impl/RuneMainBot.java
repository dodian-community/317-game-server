package net.dodian.old.world.entity.impl.npc.bots.impl;

import net.dodian.old.util.Misc;
import net.dodian.old.world.content.Food.FoodType;
import net.dodian.old.world.content.PrayerHandler;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.CombatSpecial;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.bountyhunter.Emblem;
import net.dodian.old.world.entity.combat.hit.PendingHit;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.npc.bots.NPCBotHandler;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.GroundItem;
import net.dodian.old.world.model.Item;

/**
 * Represents a main rune bot.
 * @author Professor Oak
 */
public class RuneMainBot extends NPCBotHandler implements CombatMethod {

	/**
	 * The default npc.
	 */
	private static final int DEFAULT_BOT_ID = 1158;
	
	/**
	 * The npc which has
	 * a special attack weapon equipped.
	 */
	private static final int SPEC_BOT_ID = 1200;
	
	public RuneMainBot(NPC npc) {
		super(npc);
		npc.setHasVengeance(true);
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

			//Activate piety..
			PrayerHandler.activatePrayer(npc, PrayerHandler.PIETY);

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

			} else {
				npc.forceChat("Good fight!");
			}

			//Check farcasting..
			if(!npc.getCombat().getFreezeTimer().finished()) {
				if(Misc.getRandom(20) == 10) {
					npc.forceChat("Farcasting...?");
				}
			}


			//Activate it randomly and if they're in distance..
			if(inDistance && npc.getSpecialPercentage() > CombatSpecial.DRAGON_DAGGER.getDrainAmount()) {
				if(Misc.getRandom(20) <= 3) {
					npc.setSpecialActivated(true);
				}
			}

			//Randomly turn it off..
			if(!inDistance || Misc.getRandom(5) == 1) {
				npc.setSpecialActivated(false);
			}

			//Update npc depending on the special attack state
			if(!npc.isSpecialActivated()) {
				transform(DEFAULT_BOT_ID);
			} else {
				transform(SPEC_BOT_ID);
			}

		} else {

			//Turn off prayers
			if(npc.getHeadIcon() != -1 || PrayerHandler.isActivated(npc, PrayerHandler.PIETY)) {
				PrayerHandler.deactivatePrayers(npc);
			}

			//Reset weapon
			transform(DEFAULT_BOT_ID);

			//Reset all attributes
			super.reset();
		}
	}

	@Override
	public void onDeath(Player killer) {
		//There should be a small chance of receiving a tier 1 emblem
		if(Misc.getRandom(20) == 1) {
			GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(Emblem.MYSTERIOUS_EMBLEM_1.id, 1), npc.getPosition(), killer.getUsername(), killer.getHostAddress(), false, 150, false, -1));
			killer.getPacketSender().sendMessage("@red@You have been awarded with a mysterious emblem for successfully killing the bot.");
		} else {
			killer.getPacketSender().sendMessage("The bot didn't drop anything. Maybe you're more lucky next time.");
		}
	}

	@Override
	public CombatMethod getMethod() {
		return npc.isSpecialActivated() ? CombatSpecial.DRAGON_DAGGER.getCombatMethod() : this;
	}

	@Override
	public int maxRecoilDamage() {
		return 100;
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public void preQueueAdd(Character character, Character target) {		
	}

	@Override
	public int getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 1;
	}

	@Override
	public void startAnimation(Character character) {
		int animation = character.getAttackAnim();
		if(animation != -1) {
			character.performAnimation(new Animation(animation));
		}
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	@Override
	public PendingHit[] getHits(Character character, Character target) {
		return new PendingHit[]{new PendingHit(character, target, this, true, 0)};
	}

	@Override
	public void finished(Character character) {
	}

	@Override
	public void handleAfterHitEffects(PendingHit hit) {
	}
}