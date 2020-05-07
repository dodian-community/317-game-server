package net.dodian.old.world.entity.impl.object;

import net.dodian.old.definitions.ObjectDefinition;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.Entity;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.SecondsTimer;

/**
 * This file manages a game object entity on the globe.
 * 
 * @author Relex lawl / iRageQuit2012
 *
 */

public class GameObject extends Entity {
	
	/** The action that happens when this object despawns **/
	public void onDespawn() {
		
	}
	
	
	/**
	 * GameObject constructor to call upon a new game object.
	 * @param id		The new object's id.
	 * @param position	The new object's position on the globe.
	 */
	public GameObject(int id, Position position) {
		super(position);
		this.id = id;
	}

	public GameObject(int id, Position position, String message) {
		super(position);
		this.id = id;
		this.message = message;
	}

	
	/**
	 * GameObject constructor to call upon a new game object.
	 * @param id		The new object's id.
	 * @param position	The new object's position on the globe.
	 */
	public GameObject(int id, int dissapear_seconds, Position position) {
		super(position);
		this.id = id;
		this.timer = new SecondsTimer(dissapear_seconds);
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * @param id		The new object's id.
	 * @param position	The new object's position on the globe.
	 * @param type		The new object's type.
	 */
	public GameObject(int id, Position position, int type) {
		super(position);
		this.id = id;
		this.type = type;
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * @param id		The new object's id.
	 * @param position	The new object's position on the globe.
	 * @param type		The new object's type.
	 * @param face		The new object's facing position.
	 */
	public GameObject(int id, Position position, int type, int face) {
		super(position);
		this.id = id;
		this.type = type;
		this.face = face;
	}

	/**
	 * GameObject constructor to call upon a new game object.
	 * @param id		The new object's id.
	 * @param position	The new object's position on the globe.
	 * @param type		The new object's type.
	 * @param face		The new object's facing position.
	 * @param face		The new object's seconds before dissapearing.
	 */
	public GameObject(int id, Position position, int type, int face, int seconds) {
		super(position);
		this.id = id;
		this.type = type;
		this.face = face;

		if(seconds != -1) {
			this.timer = new SecondsTimer(seconds);
		}
	}

	/**
	 * The object's id.
	 */
	private int id;

	/**
	 * Gets the object's id.
	 * @return id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * The object's type (default=10).
	 */
	private int type = 10;

	/**
	 * Gets the object's type.
	 * @return	type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the object's type.
	 * @param type	New type value to assign.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * The object's current direction to face.
	 */
	private int face;

	private String message;

	/**
	 * Gets the object's current face direction.
	 * @return	face.
	 */
	public int getFace() {
		return face;
	}

	/**
	 * Sets the object's face direction.
	 * @param face	Face value to which object will face.
	 */
	public void setFace(int face) {
		this.face = face;
	}

	/** The amount of time before the object dissapears **/
	private SecondsTimer timer;

	public SecondsTimer getTimer() {
		return timer;
	}
	
	/**
	 * Gets the object's definition.
	 * @return	definition.
	 */
	public ObjectDefinition getDefinition() {
		return ObjectDefinition.forId(id);
	}

	@Override
	public void performAnimation(Animation animation) {
		for (Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if(player.getPosition().isWithinDistance(getPosition()))
				player.getPacketSender().sendObjectAnimation(this, animation);
		}
	}

	@Override
	public void performGraphic(Graphic graphic) {
		for (Player player : World.getPlayers()) {
			if(player == null)
				continue;
			if (player.getPosition().isWithinDistance(getPosition()))
				player.getPacketSender().sendGraphic(graphic, getPosition());
		}
	}

	@Override
	public int getSize() {
		ObjectDefinition definition = getDefinition();
		if(definition == null)
			return 1;
		return (definition.getSizeX() + definition.getSizeY()) - 1;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() != this.getClass()) {
			return false;
		}

		GameObject object = (GameObject) obj;
		return object.getId() == id && object.getPosition().equals(getPosition());
	}
}
