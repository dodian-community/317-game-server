package net.dodian.old.world.content;

import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;

/**
 * This class represents a "feed",
 * It holds recent events which
 * is broadcasted to the players.
 * 
 * @author Professor Oak
 */
public class Feed {

	/**
	 * The max amount of entries in the feed.
	 */
	private static final int MAX_ENTRIES = 10;
	
	/**
	 * The start of the string interface ids.
	 */
	private static final int FRAME_START = 32014;
	
	/**
	 * The skull sprite
	 */
	public static final int SKULL_SPRITE = 162;
	
	/**
	 * Our list of entries.
	 */
	private static final String[] entries = new String[MAX_ENTRIES];
	
	/**
	 * Adds an entry to the list.
	 * @param entry		The entry to add.
	 */
	public static void submit(final String entry) {

		//First we shift our other entries...
		System.arraycopy(entries, 0, entries, 1, MAX_ENTRIES - 1);
		
		//Then we set our new entry the first one..
		entries[0] = entry;
		
		//Update entries for all players
		World.getPlayers().forEach(p -> sendEntries(p));
	}
	
	/**
	 * Clears all entries.
	 */
	public static void clear() {
		
		//Reset entries
		for(int i = 0; i < MAX_ENTRIES; i++) {
			entries[i] = null;
		}
		
		//Update entries for all players
		World.getPlayers().forEach(p -> sendEntries(p));
	}
	
	/**
	 * Sends all current entries to the specified player.
	 * @param p		The player to send entries to.
	 */
	public static void sendEntries(Player p) {
				
		//Send new strings
		for(int i = 0; i < MAX_ENTRIES; i++) {
			
			//Fetch current entry
			String entry = "";
			if(entries[i] != null) {
				entry = entries[i];
			}
			
			//Send the entry
			p.getPacketSender().sendString(FRAME_START + i, entry);
		}
	}
}
