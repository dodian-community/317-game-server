package net.dodian.old.world.model;

import net.dodian.old.world.entity.combat.magic.Autocasting;
import net.dodian.old.world.entity.impl.player.Player;

/**
 * Represents a player's magic spellbook.
 * 
 * @author relex lawl
 */

public enum MagicSpellbook {

	NORMAL(1151, TeleportType.NORMAL),
	ANCIENT(12855, TeleportType.ANCIENT),
	LUNAR(29999, TeleportType.LUNAR);

	/**
	 * The MagicSpellBook constructor.
	 * @param interfaceId	The spellbook's interface id.
     */
	private MagicSpellbook(int interfaceId, TeleportType teleportType) {
		this.interfaceId = interfaceId;
		this.teleportType = teleportType;
	}

	/**
	 * The spellbook's interface id
	 */
	private final int interfaceId;

	/**
	 * The spellbook's teleport type
	 */
	private TeleportType teleportType;

	/**
	 * Gets the interface to switch tab interface to.
	 * @return	The interface id of said spellbook.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the spellbook's teleport type
	 * @return	The teleport type of said spellbook.
	 */
	public TeleportType getTeleportType() {
		return teleportType;
	}

	/**
	 * Gets the MagicSpellBook for said id.
	 * @param id	The ordinal of the SpellBook to fetch.
	 * @return		The MagicSpellBook who's ordinal is equal to id.
	 */
	public static MagicSpellbook forId(int id) {
		for (MagicSpellbook book : MagicSpellbook.values()) {
			if (book.ordinal() == id) {
				return book;
			}
		}
		return NORMAL;
	}

	/**
	 * Changes the magic spellbook for a player.
	 * @param player		The player changing spellbook.
	 * @param book			The new spellbook.
	 */
	public static void changeSpellbook(Player player, MagicSpellbook book) {
		if(book == LUNAR) {
			if(player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
				player.getPacketSender().sendMessage("You need at least level 40 Defence to use the Lunar spellbook.");
				return;
			}
		}
		
		//Update spellbook
		player.setSpellbook(book);
		
		//Reset autocast
		Autocasting.setAutocast(player, null);
		
		//Send notification message
		player.getPacketSender().sendMessage("You have changed your magic spellbook.").
		
		//Send the new spellbook interface to the client side tabs
		sendTabInterface(6, player.getSpellbook().getInterfaceId());
	}
}
