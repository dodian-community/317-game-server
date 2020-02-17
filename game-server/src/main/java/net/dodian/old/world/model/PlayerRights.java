package net.dodian.old.world.model;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


/**
 * Represents a player's privilege rights.
 * @author Gabriel Hannason
 */

public enum PlayerRights {

	/*
	 * A regular member of the server.
	 */
	PLAYER(0, ""),
	/*
	 * A moderator who has more privilege than other regular members and donators.
	 */
	MODERATOR(0, "<col=20B2AA><shad=0>"),

	/*
	 * The second-highest-privileged member of the server.
	 */
	ADMINISTRATOR(0, "<col=FFFF64><shad=0>"),

	/*
	 * The highest-privileged member of the server
	 */
	OWNER(0, "<col=B40404><shad=0>"),

	/*
	 * The Developer of the server, has same rights as the owner.
	 */
	DEVELOPER(0, "<col=B40404><shad=0>"),

	/*
	 * A member who has donated to the server. 
	 */
	DONATOR(60, "<col=ff0000>"),
	
	/*
	 * A member who has donated more than a regular donator to the server.
	 */
	SUPER_DONATOR(40, "<col=1919ff>"),
	
	/*
	 * A member who has donated more than a super donator to the server.
	 */	
	LEGENDARY_DONATOR(20, "<col=006600>"),
	
	/*
	 * A Youtuber rank, granted to players who make videos for the server.
	 */
	YOUTUBER(30, "<col=ff0000>");

	PlayerRights(int yellDelaySeconds, String yellHexColorPrefix) {
		this.yellDelay = yellDelaySeconds;
		this.yellHexColorPrefix = yellHexColorPrefix;
	}
	
	/**
	 * Staff
	 */
	private static final ImmutableSet<PlayerRights> STAFF = Sets.immutableEnumSet(MODERATOR, ADMINISTRATOR, OWNER, DEVELOPER);
	
	public boolean isStaff() {
		return STAFF.contains(this);
	}
	
	private int yellDelay;
	private String yellHexColorPrefix;
	
	public int getYellDelay() {
		return yellDelay;
	}
	
	public String getYellPrefix() {
		return yellHexColorPrefix;
	}
}
