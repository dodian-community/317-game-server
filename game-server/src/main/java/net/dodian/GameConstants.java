package net.dodian;

import net.dodian.old.world.model.Position;

/**
 * A class containing different attributes
 * which affect the game in different ways.
 * @author Professor Oak
 */
public class GameConstants {

	/**
	 * The directory of the definition files.
	 */
	public static final String DEFINITIONS_DIRECTORY = "./data/definitions/";
	
	/**
	 * The directory of the clipping files.
	 */
	public static final String CLIPPING_DIRECTORY = "./data/clipping/";

	/**
	 * Is JAGGRAB enabled?
	 */
	public static final boolean JAGGRAB_ENABLED = false;

	/**
	 * The game engine cycle rate in milliseconds.
	 */
	public static final int GAME_ENGINE_PROCESSING_CYCLE_RATE = 600;

	/**
	 * The maximum amount of iterations that should occur per queue each cycle.
	 */
	public static final int QUEUED_LOOP_THRESHOLD = 50;

	/**
	 * The current game/client version.
	 */
	public static final int GAME_VERSION = 3;

	/**
	 * The secure game UID /Unique Identifier/ 
	 */
	public static final int GAME_UID = 4 >> 1;

	/**
	 *  The default position in game.
	 */
	public static final Position DEFAULT_POSITION = new Position(3093, 3509);
	
	/**
	 * Blood money.
	 * Current currency ingame.
	 */
	public static final int BLOOD_MONEY = 13307;

	/**
	 * Should the inventory be refreshed immediately
	 * on switching items or should it be delayed
	 * until next game cycle?
	 */
	public static final boolean QUEUE_SWITCHING_REFRESH = false;

	/**
	 * Multiplies the experience gained.
	 */
	public static final double EXP_MULTIPLIER = 6;
	
	/**
	 * The tab interfaces in game.
	 * {Gameframe}
	 * [0] = tab Id, [1] = tab interface Id
	 */
	/*public static final int TAB_INTERFACES[][] =
		{
				{0, 2423}, {1, 3917}, {2, 638}, {3, 3213}, {4, 1644}, {5, 5608}, {6, -1}, //Row 1

				{7, 37128}, {8, 5065}, {9, 5715}, {10, 2449}, {11, 42500}, {12, 147}, {13, 32000} //ROw 2
		};*/

	public static final int[] TAB_INTERFACES = {
		2423, 3917, 638, 3213, 1644, 5608, 12855,
		37128, 5065, 5715, 2449, 42500, 147, 32000
	};

}
