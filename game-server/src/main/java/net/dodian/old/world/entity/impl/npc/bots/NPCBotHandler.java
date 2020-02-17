package net.dodian.old.world.entity.impl.npc.bots;

import net.dodian.old.util.Stopwatch;
import net.dodian.old.world.content.Food.FoodType;
import net.dodian.old.world.content.PrayerHandler;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.npc.bots.impl.ArcherBot;
import net.dodian.old.world.entity.impl.npc.bots.impl.RuneMainBot;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.GraphicHeight;
import net.dodian.old.world.model.Priority;
import net.dodian.old.world.model.SecondsTimer;

/**
 * Represents an NPC Bot.
 * 
 * @author Professor Oak
 */
public abstract class NPCBotHandler {

	/**
	 * The id of the Rune main bot npc.
	 */
	private static final int RUNE_MAIN_BOT_ID = 1158;
	
	/**
	 * The id of the archer bot npc.
	 */
	private static final int ARCHER_BOT_ID = 4096;
	
	/**
	 * Assigns a bot handler to specified {@link NPC}.
	 */
	public static void assignBotHandler(NPC npc) {
		switch(npc.getId()) {
		case RUNE_MAIN_BOT_ID:
			npc.setBotHandler(new RuneMainBot(npc));
			break;
		case ARCHER_BOT_ID:
			npc.setBotHandler(new ArcherBot(npc));
			break;
		}
	}
	
	/**
	 * The npc, owner of this instance.
	 */
	public NPC npc;
	
	/**
	 * Constructs a new npc bot.
     */
	public NPCBotHandler(NPC npc) {
		this.npc = npc;
		this.eatDelay = new Stopwatch();
		this.vengeanceDelay = new SecondsTimer();
	}

	/**
	 * Processes this bot.
	 */
	public abstract void process();
	
	/**
	 * Handles what happens when the bot
	 * dies.
	 */
	public abstract void onDeath(Player killer);

	/**
	 * Gets the bot's combat method.
	 * @return
	 */
	public abstract CombatMethod getMethod();

	/**
	 * The max amount of damage the bot can return
	 * using the Ring Of Recoil.
	 */
	public abstract int maxRecoilDamage();

	/**
	 * The amount of times we have eaten food.
	 */
	private int eatCounter;

	/**
	 * The amount of damage we've recoiled back
	 * to an attacker.
	 */
	private int recoiledDamage;

	/**
	 * The delay for eating food.
	 * Makes sure food isn't consumed too quick.
	 */
	private Stopwatch eatDelay;

	/**
	 * The delay for casting vengeance
	 * Makes sure vengeance is only cast every 30 seconds.
	 */
	private SecondsTimer vengeanceDelay;

	/**
	 * Resets all attributes.
	 */
	public void reset() {

		//Reset our attributes
		recoiledDamage = 0;
		eatCounter = 0;
		npc.setSpecialPercentage(100);
		npc.setSpecialActivated(false);

		//Reset hitpoints
		npc.setHitpoints(npc.getDefinition().getHitpoints());
	}

	/**
	 * Eats the specified {@link FoodType}.
	 * @param food			The food to eat.
	 * @param minDelayMs	The minimum delay between each eat in ms.
	 */
	public void eat(FoodType food, int minDelayMs) {
		//Make sure delay has finished..
		if(eatDelay.elapsed(minDelayMs)) {
			int heal = food.getHeal();
			int currentHp = npc.getHitpoints();
			int maxHp = npc.getDefinition().getHitpoints();

			//Heal us..
			if(currentHp + heal > maxHp) {
				npc.setHitpoints(maxHp);
			} else {
				npc.setHitpoints(currentHp + heal);
			}

			//Increase attack delay..
			npc.getCombat().setAttackTimer(npc.getCombat().getAttackTimer() + 2);

			//Perform eat animation..
			npc.performAnimation(new Animation(829, Priority.HIGH));

			//Increase counter..
			eatCounter++;

			//Reset the eat delay..
			eatDelay.reset();
		}
	}

	/**
	 * Cast vengeances.
	 * There's a delay, allowing it to only be cast every 30 seconds.
	 */
	public void castVengeance() {

		//Make sure we don't already have vengeance active.
		if(npc.hasVengeance()) {
			return;
		}

		//Make sure delay has finished..
		if(vengeanceDelay.finished()) {

			//Perform veng animation..
			npc.performAnimation(new Animation(4410));

			//Perform veng graphic..
			npc.performGraphic(new Graphic(726, GraphicHeight.HIGH));

			//Force chat..
			npc.forceChat("Taste Vengeance!");

			//Set has vengeance..
			npc.setHasVengeance(true);

			//Reset the veng delay..
			vengeanceDelay.start(30);
		}
	}

	/**
	 * Attempts to get the bot's current opponent.
	 * Either it's the target or it's an attacker.
	 * @return		The opponent player.
	 */
	public Player getOpponent() {
		Character p = npc.getCombat().getTarget();
		if(p == null) {
			p = npc.getCombat().getAttacker();
		}
		if(p != null && p.isPlayer()) {
			return p.getAsPlayer();
		}
		return null;
	}

	/**
	 * Gets the overhead prayer which the bot
	 * should currently be using, based on the opponent's
	 * choices.
	 * @return
	 */
	public int getOverheadPrayer(final Player p, final boolean inDistance) {
		int prayer = -1;
		
		//Check if the enemy isn't in range..
		if(inDistance) {

			//Check if enemy is in range and if they're smiting..
			//If so, we will do the same.
			if(PrayerHandler.isActivated(p, PrayerHandler.SMITE)) {
				prayer = PrayerHandler.SMITE;
			}

		}

		//Check if enemy is protecting against our combat type..
		//Or if they're farcasting..
		//If so, we will counter pray.
		if(prayer == -1) {
			int counterPrayer = PrayerHandler.getProtectingPrayer(getMethod().getCombatType());
			if(PrayerHandler.isActivated(p, counterPrayer) || (!inDistance && !npc.getCombat().getFreezeTimer().finished())) {
				prayer = PrayerHandler.getProtectingPrayer(CombatFactory.getMethod(p).getCombatType());
			}
		}
		return prayer;
	}
	
	/**
	 * Transforms an npc into a different one.
	 * @param id		The new npc id.
	 */
	public void transform(int id) {
		
		//Check if we haven't already transformed..
		if(npc.getTransformationId() == id) {
			return;
		}
		
		//Set the transformation id.
		npc.setTransformationId(id);
	}

	public Stopwatch getEatDelay() {
		return eatDelay;
	}

	public int getEatCounter() {
		return eatCounter;
	}

	public int getRecoiledDamage() {
		return recoiledDamage;
	}

	public void incrementRecoiledDamage(int recoiledDamage) {
		this.recoiledDamage += recoiledDamage;
	}
}
