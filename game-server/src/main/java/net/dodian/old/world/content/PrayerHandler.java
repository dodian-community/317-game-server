package net.dodian.old.world.content;

import java.util.HashMap;

import net.dodian.old.engine.task.Task;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.util.Misc;
import net.dodian.old.world.content.Dueling.DuelRule;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.PlayerRights;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.SkullType;
import net.dodian.old.world.model.dialogue.DialogueManager;
import net.dodian.old.world.model.equipment.BonusManager;

/**
 * All of the prayers that can be activated and deactivated. This currently only
 * has support for prayers present in the <b>317 protocol</b>.
 * 
 * @author Swiffy
 */
public class PrayerHandler {

	/**
	 * Represents a prayer's configurations, such as their
	 * level requirement, buttonId, configId and drain rate.
	 * 
	 * @author relex lawl
	 */
	public enum PrayerData {
		THICK_SKIN(1, 1, 5609, 83),
		BURST_OF_STRENGTH(4, 1, 5610, 84),
		CLARITY_OF_THOUGHT(7, 1, 5611, 85),
		SHARP_EYE(8, 1, 19812, 700),
		MYSTIC_WILL(9, 1, 19814, 701),
		ROCK_SKIN(10, 2, 5612, 86),
		SUPERHUMAN_STRENGTH(13, 1.5, 5613, 87),
		IMPROVED_REFLEXES(16, 1.5, 5614, 88),
		RAPID_RESTORE(19, .4, 5615, 89),
		RAPID_HEAL(22, .6, 5616, 90),
		PROTECT_ITEM(25, .6, 5617, 91),
		HAWK_EYE(26, 1.5, 19816, 702),
		MYSTIC_LORE(27, 1.5, 19818, 703),
		STEEL_SKIN(28, 2.5, 5618, 92),
		ULTIMATE_STRENGTH(31, 2.8, 5619, 93),
		INCREDIBLE_REFLEXES(34, 2.8, 5620, 94),
		PROTECT_FROM_MAGIC(37, 2.8, 5621, 95, 2),
		PROTECT_FROM_MISSILES(40, 2.8, 5622, 96, 1),
		PROTECT_FROM_MELEE(43, 2.8, 5623, 97, 0),
		EAGLE_EYE(44, 3, 19821, 704),
		MYSTIC_MIGHT(45, 3, 19823, 705),
		RETRIBUTION(46, 1, 683, 98, 4),
		REDEMPTION(49, 2, 684, 99, 5),
		SMITE(52, 5, 685, 100, 100, 6),
		PRESERVE(55, 1, 28001, 708),
		CHIVALRY(60, 5, 19825, 706),
		PIETY(70, 6, 19827, 707),
		RIGOUR(74, 6.4, 28004, 710),
		AUGURY(77, 6.1, 28007, 712);

		private PrayerData(int requirement, double drainRate, int buttonId, int configId, int... hint) {
			this.requirement = requirement;
			this.drainRate = drainRate;
			this.buttonId = buttonId;
			this.configId = configId;
			if (hint.length > 0)
				this.hint = hint[0];
		}

		/**
		 * The prayer's level requirement for player to be able
		 * to activate it.
		 */
		private int requirement;

		/**
		 * The prayer's action button id in prayer tab.
		 */
		private int buttonId;

		/**
		 * The prayer's config id to switch their glow on/off by 
		 * sending the sendConfig packet.
		 */
		private int configId;

		/**
		 * The prayer's drain rate as which it will drain
		 * the associated player's prayer points.
		 */
		private double drainRate;

		/**
		 * The prayer's head icon hint index.
		 */
		private int hint = -1;

		/**
		 * The prayer's formatted name.
		 */
		private String name;

		/**
		 * Gets the prayer's formatted name.
		 * @return	The prayer's name
		 */
		private final String getPrayerName() {
			if (name == null)
				return Misc.capitalizeWords(toString().toLowerCase().replaceAll("_", " "));
			return name;
		}

		/**
		 * Contains the PrayerData with their corresponding prayerId.
		 */
		private static HashMap <Integer, PrayerData> prayerData = new HashMap <Integer, PrayerData> ();

		/**
		 * Contains the PrayerData with their corresponding buttonId.
		 */
		private static HashMap <Integer, PrayerData> actionButton = new HashMap <Integer, PrayerData> ();

		/**
		 * Populates the prayerId and buttonId maps.
		 */
		static {
			for (PrayerData pd : PrayerData.values()) {
				prayerData.put(pd.ordinal(), pd);
				actionButton.put(pd.buttonId, pd);
			}
		}
	}

	/**
	 * Gets the protecting prayer based on the argued combat type.
	 * 
	 * @param type
	 *            the combat type.
	 * @return the protecting prayer.
	 */
	public static int getProtectingPrayer(CombatType type) {
		switch (type) {
		case MELEE:
			return PROTECT_FROM_MELEE;
		case MAGIC:
			return PROTECT_FROM_MAGIC;
		case RANGED:
			return PROTECT_FROM_MISSILES;
		default:
			throw new IllegalArgumentException("Invalid combat type: " + type);
		}
	}

	public static boolean isActivated(Character c, int prayer) {
		return c.getPrayerActive()[prayer];
	}

	/**
	 * Activates a prayer with specified <code>buttonId</code>.
	 * @param player	The player clicking on prayer button.
	 * @param buttonId	The button the player is clicking.
	 */
	public static boolean togglePrayer(Player player, final int buttonId) {
		PrayerData prayerData = PrayerData.actionButton.get(buttonId);
		if(prayerData != null) {
			if (!player.getPrayerActive()[prayerData.ordinal()])
				activatePrayer(player, prayerData.ordinal());
			else
				deactivatePrayer(player, prayerData.ordinal());
			return true;
		}
		return false;
	}

	/**
	 * Activates said prayer with specified <code>prayerId</code> and de-activates
	 * all non-stackable prayers.
	 * @param character		The player activating prayer.
	 * @param prayerId		The id of the prayer being turned on, also known as the ordinal in the respective enum.
	 */
	public static void activatePrayer(Character character, final int prayerId) {

		//Get the prayer data.
		PrayerData pd = PrayerData.prayerData.get(prayerId);

		//Check if it's availble
		if(pd == null) {
			return;
		}

		//Check if we're already praying this prayer.
		if (character.getPrayerActive()[prayerId]) {

			//If we are an npc, make sure our headicon
			//is up to speed.
			if(character.isNpc()) {
				NPC npc = character.getAsNpc();
				if (pd.hint != -1) {
					int hintId = getHeadHint(character);
					if(npc.getHeadIcon() != hintId) {
						npc.setHeadIcon(hintId);
					}
				}
			}

			return;
		}

		//If we're a player, make sure we can use this prayer.
		if(character.isPlayer()) {
			Player player = character.getAsPlayer();
			if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
				player.getPacketSender().sendConfig(pd.configId, 0);
				player.getPacketSender().sendMessage("You do not have enough Prayer points.");
				return;
			}
			if(!canUse(player, pd, true)) {
				return;
			}
		}

		switch (prayerId) {
		case THICK_SKIN:
		case ROCK_SKIN:
		case STEEL_SKIN:
			resetPrayers(character, DEFENCE_PRAYERS, prayerId);
			break;
		case BURST_OF_STRENGTH:
		case SUPERHUMAN_STRENGTH:
		case ULTIMATE_STRENGTH:
			resetPrayers(character, STRENGTH_PRAYERS, prayerId);
			resetPrayers(character, RANGED_PRAYERS, prayerId);
			resetPrayers(character, MAGIC_PRAYERS, prayerId);
			break;
		case CLARITY_OF_THOUGHT:
		case IMPROVED_REFLEXES:
		case INCREDIBLE_REFLEXES:
			resetPrayers(character, ATTACK_PRAYERS, prayerId);
			resetPrayers(character, RANGED_PRAYERS, prayerId);
			resetPrayers(character, MAGIC_PRAYERS, prayerId);
			break;
		case SHARP_EYE:
		case HAWK_EYE:
		case EAGLE_EYE:
		case MYSTIC_WILL:
		case MYSTIC_LORE:
		case MYSTIC_MIGHT:
			resetPrayers(character, STRENGTH_PRAYERS, prayerId);
			resetPrayers(character, ATTACK_PRAYERS, prayerId);
			resetPrayers(character, RANGED_PRAYERS, prayerId);
			resetPrayers(character, MAGIC_PRAYERS, prayerId);
			break;
		case CHIVALRY:
		case PIETY:
		case RIGOUR:
		case AUGURY:
			resetPrayers(character, DEFENCE_PRAYERS, prayerId);
			resetPrayers(character, STRENGTH_PRAYERS, prayerId);
			resetPrayers(character, ATTACK_PRAYERS, prayerId);
			resetPrayers(character, RANGED_PRAYERS, prayerId);
			resetPrayers(character, MAGIC_PRAYERS, prayerId);
			break;
		case PROTECT_FROM_MAGIC:
		case PROTECT_FROM_MISSILES:
		case PROTECT_FROM_MELEE:
			resetPrayers(character, OVERHEAD_PRAYERS, prayerId);
			break;
		case RETRIBUTION:
		case REDEMPTION:
		case SMITE:
			resetPrayers(character, OVERHEAD_PRAYERS, prayerId);
			break;
		}
		character.setPrayerActive(prayerId, true);

		if(character.isPlayer()) {
			Player p = character.getAsPlayer();
			p.getPacketSender().sendConfig(pd.configId, 1);
			if (hasNoPrayerOn(p, prayerId) && !p.isDrainingPrayer()) {
				if(p.getRights() != PlayerRights.DEVELOPER) {
					startDrain(p);
				}
			}
			if (pd.hint != -1) {
				int hintId = getHeadHint(character);
				p.getAppearance().setHeadHint(hintId);
			}

			BonusManager.update(p);
		} else if(character.isNpc()) {

			NPC npc = character.getAsNpc();
			if (pd.hint != -1) {
				int hintId = getHeadHint(character);
				if(npc.getHeadIcon() != hintId) {
					npc.setHeadIcon(hintId);
				}
			}
		}
	}


	/**
	 * Checks if the player can use the specified prayer.
	 * @param player
	 * @param prayer
	 * @return
	 */
	public static boolean canUse(Player player, PrayerData prayer, boolean msg) {
		if (player.getSkillManager().getMaxLevel(Skill.PRAYER) < (prayer.requirement)) {
			if(msg) {
				player.getPacketSender().sendConfig(prayer.configId, 0);
				player.getPacketSender().sendMessage("You need a Prayer level of at least " + prayer.requirement + " to use " + prayer.getPrayerName() + ".");
			}
			return false;
		}
		if (prayer == PrayerData.CHIVALRY && player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 60) {
			if(msg) {
				player.getPacketSender().sendConfig(prayer.configId, 0);
				player.getPacketSender().sendMessage("You need a Defence level of at least 60 to use Chivalry.");
			}
			return false;
		}
		if (prayer == PrayerData.PIETY && player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 70) {
			if(msg) {
				player.getPacketSender().sendConfig(prayer.configId, 0);
				player.getPacketSender().sendMessage("You need a Defence level of at least 70 to use Piety.");
			}
			return false;
		}
		if((prayer == PrayerData.RIGOUR || prayer == PrayerData.AUGURY) && player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 70) {
			if(msg) {
				player.getPacketSender().sendConfig(prayer.configId, 0);
				player.getPacketSender().sendMessage("You need a Defence level of at least 70 to use that prayer.");
			}
			return false;
		}
		if(prayer == PrayerData.PROTECT_ITEM) {
			if(player.isSkulled() && player.getSkullType() == SkullType.RED_SKULL) {
				if(msg) {
					player.getPacketSender().sendConfig(prayer.configId, 0);
					DialogueManager.sendStatement(player, "You cannot use the Protect Item prayer with a red skull!");
				}
				return false;
			}
		}
		if(!player.getCombat().getPrayerBlockTimer().finished()) {
			if(prayer == PrayerData.PROTECT_FROM_MELEE || prayer == PrayerData.PROTECT_FROM_MISSILES || prayer == PrayerData.PROTECT_FROM_MAGIC) {
				if(msg) {
					player.getPacketSender().sendConfig(prayer.configId, 0);
					player.getPacketSender().sendMessage("You have been disabled and can no longer use protection prayers.");
				}
				return false;
			}
		}

		//Prayer locks
		boolean locked = false;

		if(prayer == PrayerData.PRESERVE && !player.isPreserveUnlocked()
				|| prayer == PrayerData.RIGOUR && !player.isRigourUnlocked()
				|| prayer == PrayerData.AUGURY && !player.isAuguryUnlocked()) {
			locked = true;
		}

		if(locked) {
			if(msg) {
				player.getPacketSender().sendMessage("You have not unlocked that Prayer yet.");
			}
			return false;
		}

		//Duel, disabled prayer?
		if(player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_PRAYER.ordinal()]) {
			if(msg) {
				DialogueManager.sendStatement(player, "Prayer has been disabled in this duel!");
				player.getPacketSender().sendConfig(prayer.configId, 0);
			}
			return false;
		}

		return true;
	}

	/**
	 * Deactivates said prayer with specified <code>prayerId</code>.
	 * @param c		The player deactivating prayer.
	 * @param prayerId		The id of the prayer being deactivated.
	 */
	public static void deactivatePrayer(Character c, int prayerId) {
		if (!c.getPrayerActive()[prayerId]) {
			return;
		}
		PrayerData pd = PrayerData.prayerData.get(prayerId);
		c.getPrayerActive()[prayerId] = false;
		if(c.isPlayer()) {
			Player p = c.getAsPlayer();
			p.getPacketSender().sendConfig(pd.configId, 0);
			if (pd.hint != -1) {
				int hintId = getHeadHint(c);
				p.getAppearance().setHeadHint(hintId);
			}

			p.getQuickPrayers().checkActive();
			BonusManager.update(p);
		} else if (c.isNpc()) {
			if (pd.hint != -1) {
				int hintId = getHeadHint(c);
				if(c.getAsNpc().getHeadIcon() != hintId) {
					c.getAsNpc().setHeadIcon(hintId);
				}
			}
		}		
		//	Sounds.sendSound(player, Sound.DEACTIVATE_PRAYER_OR_CURSE);
	}

	/**
	 * Deactivates every prayer in the player's prayer book.
	 */
	public static void deactivatePrayers(Character character) {
		for (int i = 0; i < character.getPrayerActive().length; i++) {
			deactivatePrayer(character, i);
		}
		if(character.isPlayer()) {
			character.getAsPlayer().getQuickPrayers().setEnabled(false);
			character.getAsPlayer().getPacketSender().sendQuickPrayersState(false);
		} else if(character.isNpc()) {
			if(character.getAsNpc().getHeadIcon() != -1) {
				character.getAsNpc().setHeadIcon(-1);
			}
		}
	}

	public static void resetAll(Player player) {
		for (int i = 0; i < player.getPrayerActive().length; i++) {
			PrayerData pd = PrayerData.prayerData.get(i);
			if(pd == null)
				continue;
			player.getPrayerActive()[i] = false;
			player.getPacketSender().sendConfig(pd.configId, 0);
			if (pd.hint != -1) {
				int hintId = getHeadHint(player);
				player.getAppearance().setHeadHint(hintId);
			}
		}
		player.getQuickPrayers().setEnabled(false);
		player.getPacketSender().sendQuickPrayersState(false);
	}

	/**
	 * Gets the player's current head hint if they activate or deactivate
	 * a head prayer.
	 * @return			The player's current head hint index.
	 */
	private static int getHeadHint(Character character) {
		boolean[] prayers = character.getPrayerActive();
		if (prayers[PROTECT_FROM_MELEE])
			return 0;
		if (prayers[PROTECT_FROM_MISSILES])
			return 1;
		if (prayers[PROTECT_FROM_MAGIC])
			return 2;
		if (prayers[RETRIBUTION])
			return 3;
		if (prayers[SMITE])
			return 4;
		if (prayers[REDEMPTION])
			return 5;
		return -1;
	}

	/**
	 * Initializes the player's prayer drain once a first prayer
	 * has been selected.
	 * @param player	The player to start prayer drain for.
	 */
	private static void startDrain(final Player player) {
		if (getDrain(player) <= 0 && !player.isDrainingPrayer())
			return;
		player.setDrainingPrayer(true);
		TaskManager.submit(new Task(1, player, false) {
			@Override
			public void execute() {

				if(player.getHitpoints() <= 0) {
					this.stop();
					return;
				}

				double drainAmount = getDrain(player);

				if (drainAmount <= 0) {
					this.stop();
					return;
				}

				if (player.getPrayerPointDrain() < 0) {
					int total = (int) (player.getSkillManager().getCurrentLevel(Skill.PRAYER) - 1);
					player.getSkillManager().setCurrentLevel(Skill.PRAYER, total, true);
					player.setPrayerPointDrain(1.0);
				}

				if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
					deactivatePrayers(player);
					player.getPacketSender().sendMessage("You have run out of Prayer points!");
					this.stop();
					return;
				}

				player.setPrayerPointDrain(player.getPrayerPointDrain() - drainAmount);

			}
			@Override
			public void stop() {
				setEventRunning(false);
				player.setDrainingPrayer(false);
			}
		});
	}

	/**
	 * Gets the amount of prayer to drain for <code>player</code>.
	 * @param player	The player to get drain amount for.
	 * @return			The amount of prayer that will be drained from the player.
	 */
	private static final double getDrain(Player player) {
		double toRemove = 0.0;
		for (int i = 0; i < player.getPrayerActive().length; i++) {
			if (player.getPrayerActive()[i]) {
				PrayerData prayerData = PrayerData.prayerData.get(i);
				toRemove += prayerData.drainRate / 10;
			}
		}
		if (toRemove > 0) {
			toRemove /= (1 + (0.05 * player.getBonusManager().getOtherBonus()[BonusManager.BONUS_PRAYER]));		
		}
		return toRemove;
	}

	/**
	 * Checks if a player has no prayer on.
	 * @param player		The player to check prayer status for.
	 * @param exceptionId	The prayer id currently being turned on/activated.
	 * @return				if <code>true</code>, it means player has no prayer on besides <code>exceptionId</code>.
	 */
	private final static boolean hasNoPrayerOn(Player player, int exceptionId) {
		int prayersOn = 0;
		for (int i = 0; i < player.getPrayerActive().length; i++) {
			if (player.getPrayerActive()[i] && i != exceptionId)
				prayersOn++;
		}
		return prayersOn == 0;
	}

	/**
	 * Resets <code> prayers </code> with an exception for <code> prayerID </code>
	 * 
	 * @param prayers	The array of prayers to reset
	 * @param prayerID	The prayer ID to not turn off (exception)
	 */
	public static void resetPrayers(Character c, int[] prayers, int prayerID) {
		for (int i = 0; i < prayers.length; i++) {
			if (prayers[i] != prayerID)
				deactivatePrayer(c, prayers[i]);
		}
	}

	/**
	 * Resets prayers in the array
	 * @param player
	 * @param prayers
	 */
	public static void resetPrayers(Player player, int[] prayers) {
		for (int i = 0; i < prayers.length; i++) {
			deactivatePrayer(player, prayers[i]);
		}
	}

	/**
	 * Checks if action button ID is a prayer button.
	 *
	 */
	public static final boolean isButton(final int actionButtonID) {
		return PrayerData.actionButton.containsKey(actionButtonID);
	}

	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3, MYSTIC_WILL = 4,
			ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7, RAPID_RESTORE = 8, RAPID_HEAL = 9, 
			PROTECT_ITEM = 10, HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13, ULTIMATE_STRENGTH = 14,
			INCREDIBLE_REFLEXES = 15, PROTECT_FROM_MAGIC = 16, PROTECT_FROM_MISSILES = 17,
			PROTECT_FROM_MELEE = 18, EAGLE_EYE = 19, MYSTIC_MIGHT = 20, RETRIBUTION = 21, REDEMPTION = 22, SMITE = 23, PRESERVE = 24,
			CHIVALRY = 25, PIETY = 26, RIGOUR = 27, AUGURY = 28;

	/**
	 * Contains every prayer that counts as a defense prayer.
	 */
	public static final int[] DEFENCE_PRAYERS = {THICK_SKIN, ROCK_SKIN, STEEL_SKIN, CHIVALRY, PIETY, RIGOUR, AUGURY};

	/**
	 * Contains every prayer that counts as a strength prayer.
	 */
	public static final int[] STRENGTH_PRAYERS = {BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CHIVALRY, PIETY};

	/**
	 * Contains every prayer that counts as an attack prayer.
	 */
	public static final int[] ATTACK_PRAYERS = {CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY};

	/**
	 * Contains every prayer that counts as a ranged prayer.
	 */
	public static final int[] RANGED_PRAYERS = {SHARP_EYE, HAWK_EYE, EAGLE_EYE, RIGOUR};

	/**
	 * Contains every prayer that counts as a magic prayer.
	 */
	public static final int[] MAGIC_PRAYERS = {MYSTIC_WILL, MYSTIC_LORE, MYSTIC_MIGHT, AUGURY};

	/**
	 * Contains every prayer that counts as an overhead prayer, excluding protect from summoning.
	 */
	public static final int[] OVERHEAD_PRAYERS = {PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, RETRIBUTION, REDEMPTION, SMITE};

	/**
	 * Contains every protection prayer
	 */
	public static final int[] PROTECTION_PRAYERS = {PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE};
}
