package net.dodian.old.net.login;

import org.springframework.stereotype.Component;

@Component
public final class LoginResponses {
		
	/**
	 * This login opcode signifies a successful login.
	 */
	public static final int LOGIN_SUCCESSFUL = 2;
	
	/**
	 * This login opcode is used when the player
	 * has entered an invalid username and/or password.
	 */
	public static final int LOGIN_INVALID_CREDENTIALS = 3;
	
	/**
	 * This login opcode is used when the account
	 * has been disabled.
	 */
	public static final int LOGIN_DISABLED_ACCOUNT = 4;
	
	/**
	 * This login opcode is used when the player's IP
	 * has been disabled.
	 */
	public static final int LOGIN_DISABLED_COMPUTER = 22;
	
	/**
	 * This login opcode is used when the player's IP
	 * has been disabled.
	 */
	public static final int LOGIN_DISABLED_IP = 27;
	
	/**
	 * This login opcode is used when the account
	 * attempting to connect is already online in the server.
	 */
	public static final int LOGIN_ACCOUNT_ONLINE = 5;
	
	/**
	 * This login opcode is used when the game has been or
	 * is being updated.
	 */
	public static final int LOGIN_GAME_UPDATE = 6;
	
	/**
	 * This login opcode is used when the world being
	 * connected to is full.
	 */
	public static final int LOGIN_WORLD_FULL = 7;
		
	/**
	 * This login opcode is used when the connections
	 * from an ip address has exceeded maximum connections.
	 */
	public static final int LOGIN_CONNECTION_LIMIT = 9;
	
	/**
	 * This login opcode is used when a connection
	 * has received a bad session id.
	 */
	public static final int LOGIN_BAD_SESSION_ID = 10;
	
	/**
	 * This login opcode is used when the login procedure
	 * has rejected the session.
	 */
	public static final int LOGIN_REJECT_SESSION = 11;
			
	/**
	 * This login opcode is used when a player has
	 * entered invalid credentials.
	 */
	public static final int INVALID_CREDENTIALS_COMBINATION = 28;
	
	/**
	 * This login opcode is used when a player has
	 * attempted to login with a old client.
	 */
	public static final int OLD_CLIENT_VERSION = 30;
	
	/**
	 * New account
	 */
	public static final int NEW_ACCOUNT = -1;
	
}
