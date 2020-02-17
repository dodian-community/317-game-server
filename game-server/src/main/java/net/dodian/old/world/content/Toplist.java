package net.dodian.old.world.content;

import java.util.ArrayList;
import java.util.List;

import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;

/**
 * Represents a toplist.
 * The list contains the best
 * pkers.
 * 
 * @author Professor Oak
 */
public class Toplist {

	/**
	 * The frame start (interface id)
	 */
	private static final int FRAME_START = 32002;

	/**
	 * The maximum size of the toplist
	 */
	private static final int TOPLIST_SIZE = 10;

	/**
	 * The toplist
	 */
	private static String[] toplist = new String[TOPLIST_SIZE];

	/**
	 * Fetches a new toplist and sends it to
	 * online players.
	 */
	public static void updateToplist() {
		List<Player> topList = new ArrayList<Player>();

		//Add all players to the list..
		for(Player p : World.getPlayers()) {
			if(p == null)
				continue;
			if(p.getKillstreak() > 0) {
				topList.add(p);
			}
		}
		
		//Sort the list..
		topList.sort((player1, player2) -> (player1.getKillstreak() > player2.getKillstreak()) ? -1 : 0);
		
		//Clear the list from entries that won't be used..
		if(topList.size() > TOPLIST_SIZE) {
			topList = topList.subList(0, TOPLIST_SIZE);
		}
		
		//Set the current top list data.
		for(int i = 0; i < TOPLIST_SIZE; i++) {
			if(i < topList.size()) {
				Player p = topList.get(i);
				
				//Set the entry
				String entry = "@or1@"+p.getUsername()+" @whi@has @yel@"+p.getKillstreak()+"@whi@ killstreak.";
				toplist[i] = entry;
			} else {
				toplist[i] = null;
			}
		}
		
		//Send the new toplist for all players
		World.getPlayers().forEach(p -> sendToplist(p));
	}
	
	/**
	 * Sends the current toplist entries to the specified player.
	 * @param p		The player to send the toplist to.
	 */
	public static void sendToplist(Player p) {
		//Send the toplist entries..
		for(int i = 0; i < TOPLIST_SIZE; i++) {
			String entry = "";
			if(toplist[i] != null) {
				entry = toplist[i];
			}
			p.getPacketSender().sendString(FRAME_START + i, entry);
		}
	}
}
