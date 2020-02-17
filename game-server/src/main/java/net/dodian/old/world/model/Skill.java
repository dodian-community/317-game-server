package net.dodian.old.world.model;

import java.util.HashMap;
import java.util.Map;

import net.dodian.old.util.Misc;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * This enum represents a skill in the game.
 * Every skill should be added with its
 * proper chatbox level up interface.
 * 
 * @author Professor Oak
 */
public enum Skill {

	ATTACK(6247, 52004),
	DEFENCE(6253, 52010),
	STRENGTH(6206, 52007),
	HITPOINTS(6216, 52001),
	RANGED(4443, 52013),
	PRAYER(6242, 52019),
	MAGIC(6211, 52016),
	COOKING(6226, 24222),
	WOODCUTTING(4272, 24228),
	FLETCHING(6231, 24227),
	FISHING(6258, 24219),
	FIREMAKING(4282, 24225),
	CRAFTING(6263, 24224),
	SMITHING(6221, 24216),
	MINING(4416, 24213),
	HERBLORE(6237, 24218),
	AGILITY(4277, 24215),
	THIEVING(4261, 24221),
	SLAYER(12122, 24230),
	FARMING(5267, 24231),
	RUNECRAFTING(4267, 24229),
	CONSTRUCTION(7267, -1),
	HUNTER(8267, 24232);
	
	private static Map<Integer, Skill> skillMap = new HashMap<Integer, Skill>();

	static {
		for (Skill skill : Skill.values()) {
			skillMap.put(skill.button, skill);
		}
	}
	
	/**
	 * The {@link ImmutableSet} which represents the skills that a player can set to a desired level.
	 */
	private static final ImmutableSet<Skill> ALLOWED_TO_SET_LEVLES = Sets.immutableEnumSet(ATTACK, DEFENCE, STRENGTH, HITPOINTS, RANGED, PRAYER, MAGIC);
	
	/**
	 * Checks if a skill can be manually set to a level by a player.
	 * @return		true if the player can set their level in this skill, false otherwise.
	 */
	public boolean canSetLevel() {
		return ALLOWED_TO_SET_LEVLES.contains(this);
	}
	
	/**
	 * Gets a skill for its button id.
	 * @param button		The button id.
	 * @return				The skill with the matching button.
	 */
	public static Skill forButton(int button) {
		return skillMap.get(button);
	}

	/**
	 * Constructor
	 * @param chatboxInterface
	 * @param button
	 */
	private Skill(int chatboxInterface, int button) {
		this.chatboxInterface = chatboxInterface;
		this.button = button;
	}

	/**
	 * The {@link Skill}'s chatbox interface
	 * The interface which will be sent
	 * on levelup.
	 */
	private final int chatboxInterface;
	
	/**
	 * Gets the {@link Skill}'s chatbox interface.
	 * @return The interface which will be sent on levelup.
	 */
	public int getChatboxInterface() {
		return chatboxInterface;
	}
	
	/**
	 * The {@link Skill}'s button in the skills tab
	 * interface.
	 */
	private final int button;
	
	/**
	 * Gets the {@link Skill}'s button id.
	 * @return		The button for this skill.
	 */
	public int getButton() {
		return button;
	}
	
	/**
	 * Gets the {@link Skill}'s name.
	 * @return	The {@link Skill}'s name in a suitable format.
	 */
	public String getName() {
		return Misc.formatText(toString().toLowerCase());
	}
}