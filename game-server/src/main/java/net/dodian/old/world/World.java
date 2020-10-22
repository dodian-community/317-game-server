package net.dodian.old.world;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.dodian.GameConstants;
import net.dodian.Server;
import net.dodian.old.util.Misc;
import net.dodian.old.world.content.Toplist;
import net.dodian.old.world.entity.impl.CharacterList;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.object.ObjectHandler;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.entity.updating.NpcUpdateSequence;
import net.dodian.old.world.entity.updating.PlayerUpdateSequence;
import net.dodian.old.world.entity.updating.UpdateSequence;
import net.dodian.old.world.model.SecondsTimer;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * Represents the game and its entities.
 * 
 * @author Professor Oak
 * Thanks to lare96 for help with parallel updating system
 */
public class World {

	/** All of the registered players. */
	private static CharacterList<Player> players = new CharacterList<>(1000);

	/** All of the registered NPCs. */
	private static CharacterList<NPC> npcs = new CharacterList<>(2027);

	/** Used to block the game thread until updating has completed. */
	private static Phaser synchronizer = new Phaser(1);

	/** A thread pool that will update players in parallel. */
	private static ExecutorService updateExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build());

	/**The queue of {@link Player}s waiting to be added to the game. **/
	private static Queue<Player> playerAddQueue = new ConcurrentLinkedQueue<>();
	
	/**The queue of {@link Player}s waiting to be removed from the game. **/
	private static Queue<Player> playerRemoveQueue = new ConcurrentLinkedQueue<>();

	/**The queue of {@link NPC}s waiting to be added to the game. **/
	private static Queue<NPC> npcAddQueue = new ConcurrentLinkedQueue<>();

	/**The queue of {@link NPC}s waiting to be removed from the game. **/
	private static Queue<NPC> npcRemoveQueue = new ConcurrentLinkedQueue<>();

	/** The delay for the toplist refresh **/
	public static final SecondsTimer TOPLIST_REFRESH_TIMER = new SecondsTimer();

	/** The delay for handling pid switching **/
	public static final SecondsTimer PID_SHUFFLE_TIMER = new SecondsTimer();

	/**
	 * Gets a player by their username.
	 * @param username		The username of the player.
	 * @return				The player with the matching username.
	 */
	public static Player getPlayerByName(String username) {
		Optional<Player> op = players.search(p -> p != null && p.getUsername().equals(Misc.formatText(username)));
		return op.isPresent() ? op.get() : null;
	}

	/**
	 * Gets a player by their encoded username.
	 * @param encodedName		The username in a long.
	 * @return					The player with the matching encoded username.
	 */
	public static Player getPlayerByLong(long encodedName) {
		Optional<Player> op = players.search(p -> p != null && p.getLongUsername().equals(encodedName));
		return op.isPresent() ? op.get() : null;
	}

	/**
	 * Broadcasts a message to all players in the game.
	 * @param message		The message to broadcast.
	 */
	public static void sendMessage(String message) {
		players.forEach(p -> p.getPacketSender().sendMessage(message));
	}

	/**
	 * Broadcasts a message to all staff-members in the game.
	 * @param message		The message to broadcast.
	 */
	public static void sendStaffMessage(String message) {
		players.stream().filter(p -> !Objects.isNull(p) && p.getRights().isStaff()).forEach(p -> p.getPacketSender().sendMessage(message));
	}

	/**
	 * Saves all players in the game.
	 */
	public static void savePlayers() {
		players.forEach(Player::save);
	}

	/**
	 * Processes the world.
	 */
	public static void sequence() {

		//Shuffle entities (rearrange PID, every 60 seconds)
		final boolean shuffle = PID_SHUFFLE_TIMER.finished();

		players.updateShuffleList(shuffle);
		npcs.updateShuffleList(shuffle);

		//Restart PID shuffle timer
		if(shuffle) {
			PID_SHUFFLE_TIMER.start(60);
		}

		//Update them

		// First we construct the update sequences.
		UpdateSequence<Player> playerUpdate = new PlayerUpdateSequence(synchronizer, updateExecutor);
		UpdateSequence<NPC> npcUpdate = new NpcUpdateSequence();

		// Then we execute pre-updating code.
		players.forEach(playerUpdate::executePreUpdate);
		npcs.forEach(npcUpdate::executePreUpdate);

		// Then we execute parallelized updating code.
		synchronizer.bulkRegister(players.size());
		players.forEach(playerUpdate::executeUpdate);
		synchronizer.arriveAndAwaitAdvance();

		// Then we execute post-updating code.
		players.forEach(playerUpdate::executePostUpdate);
		npcs.forEach(npcUpdate::executePostUpdate);

		//Check if top list should be refreshed
		if(TOPLIST_REFRESH_TIMER.finished()) {
			Toplist.updateToplist();
			TOPLIST_REFRESH_TIMER.start(60);
		}

		//Register queued players
		int amount;
		for (amount = 0; amount < GameConstants.QUEUED_LOOP_THRESHOLD; amount++) {
			Player player = playerAddQueue.poll();
			if (player == null)
				break;
			getPlayers().add(player);
			player.onLogin();
		}

		// Deregister queued players.
		amount = 0;
		Iterator<Player> $it = playerRemoveQueue.iterator();
		while ($it.hasNext()) {
			Player player = $it.next();
			if (player == null || amount >= GameConstants.QUEUED_LOOP_THRESHOLD) {
				break;
			}
			if(player.canLogout() || player.getForcedLogoutTimer().finished() || Server.isUpdating()) {
				getPlayers().remove(player);
				player.onLogout();
				$it.remove();
				amount++;
			}
		}
		
		//Register queued npcs
		for (amount = 0; amount < GameConstants.QUEUED_LOOP_THRESHOLD; amount++) {
			NPC npc = npcAddQueue.poll();
			if (npc == null)
				break;
			getNpcs().add(npc);
		}

		//Deregister queued npcs
		for (amount = 0; amount < GameConstants.QUEUED_LOOP_THRESHOLD; amount++) {
			NPC npc = npcRemoveQueue.poll();
			if (npc == null)
				break;
			getNpcs().remove(npc);
		}

		//Objects updating
		ObjectHandler.process();
	}

	public static CharacterList<Player> getPlayers() {
		return players;
	}

	public static CharacterList<NPC> getNpcs() {
		return npcs;
	}

	public static Queue<Player> getPlayerRemoveQueue() {
		return playerRemoveQueue;
	}

	public static Queue<Player> getPlayerAddQueue() {
		return playerAddQueue;
	}

	public static Queue<NPC> getNpcAddQueue() {
		return npcAddQueue;
	}

	public static Queue<NPC> getNpcRemoveQueue() {
		return npcRemoveQueue;
	}
}
