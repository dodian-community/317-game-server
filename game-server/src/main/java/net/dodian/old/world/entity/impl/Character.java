package net.dodian.old.world.entity.impl;


import net.dodian.old.util.Stopwatch;
import net.dodian.old.world.entity.Entity;
import net.dodian.old.world.entity.combat.Combat;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.combat.hit.HitDamage;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Direction;
import net.dodian.old.world.model.Flag;
import net.dodian.old.world.model.Graphic;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.UpdateFlag;
import net.dodian.old.world.model.movement.MovementQueue;
import net.dodian.old.world.model.*;

/**
 * A player or NPC
 * @author Swiffy
 */

public abstract class Character extends Entity {

	public Character() {}

	public Character(Position position) {
		super(position);
	}

	public Character moveTo(Position teleportTarget) {
		getMovementQueue().reset();
		setPosition(teleportTarget.copy());
		setNeedsPlacement(true);
		setResetMovementQueue(true);
		setEntityInteraction(null);
		if(isPlayer()) {
			getMovementQueue().handleRegionChange();
		}
		return this;
	}

	public Character forceChat(String message) {
		setForcedChat(message);
		getUpdateFlag().flag(Flag.FORCED_CHAT);
		return this;
	}

	public Character setEntityInteraction(Entity entity) {
		this.interactingEntity = entity;
		getUpdateFlag().flag(Flag.ENTITY_INTERACTION);
		return this;
	}

	@Override
	public void performAnimation(Animation animation) {
		setAnimation(animation);
	}

	@Override
	public void performGraphic(Graphic graphic) {
		setGraphic(graphic);
	}

	/*
	 * Fields
	 */


	private final Combat combat = new Combat(this);
	private final MovementQueue movementQueue = new MovementQueue(this);
	private String forcedChat;
	private Direction walkingDirection = Direction.NONE, runningDirection = Direction.NONE, lastDirection = Direction.NONE;
	private Stopwatch lastCombat = new Stopwatch();
	private UpdateFlag updateFlag = new UpdateFlag();
	private Locations.Location location = Locations.Location.DEFAULT;
	private Position positionToFace;
	private Animation animation;
	private Graphic graphic;
	private Entity interactingEntity;
	public Position singlePlayerPositionFacing;
	private int npcTransformationId;
	private int poisonDamage;
	private boolean[] prayerActive = new boolean[30], curseActive = new boolean[20];
	private boolean resetMovementQueue;
	private boolean needsPlacement;
	private boolean untargetable;
	private boolean hasVengeance;
	private int specialPercentage = 100;
	private boolean specialActivated;
	private boolean recoveringSpecialAttack;

	private HitDamage primaryHit;
	private HitDamage secondaryHit;
	public abstract Character setHitpoints(int hitpoints);
	public abstract void appendDeath();
	public abstract void heal(int damage);
	public abstract int getHitpoints();
	public abstract int getBaseAttack(CombatType type);
	public abstract int getBaseDefence(CombatType type);
	public abstract int getBaseAttackSpeed();
	public abstract int getAttackAnim();
	public abstract int getBlockAnim();

	/**
	 * Is this entity registered.
	 */
	private boolean registered;

	/*
	 * Getters and setters
	 * Also contains methods.
	 */

	public Locations.Location getLocation() {
		return location;
	}

	public void setLocation(Locations.Location location) {
		this.location = location;
	}


	public Character setGraphic(Graphic newGraphic) {

		/**
		 * Graphic priorities.
		 * This piece of code below will stop other graphics
		 * from being performed if there's already one
		 * with higher priority queued to be sent to the
		 * client via updating.
		 */
		if(this.graphic != null && newGraphic != null) {
			if(this.graphic.getPriority().ordinal() > newGraphic.getPriority().ordinal()) {
				return this;
			}
		}

		this.graphic = newGraphic;

		if(newGraphic != null) {
			getUpdateFlag().flag(Flag.GRAPHIC);
		}

		return this;
	}

	public Character setAnimation(Animation newAnimation) {

		/**
		 * Animation priorities.
		 * This piece of code below will stop other animations
		 * from being performed if there's already one
		 * with higher priority queued to be sent to the
		 * client via updating.
		 */
		if(this.animation != null && newAnimation != null) {
			if(this.animation.getPriority().ordinal() > newAnimation.getPriority().ordinal()) {
				return this;
			}
		}

		this.animation = newAnimation;

		if(animation != null) {
			getUpdateFlag().flag(Flag.ANIMATION);
		}

		return this;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	public Animation getAnimation() {
		return animation;
	}

	/**
	 * @return the lastCombat
	 */
	public Stopwatch getLastCombat() {
		return lastCombat;
	}

	public int getAndDecrementPoisonDamage() {
		return poisonDamage--;
	}

	public int getPoisonDamage() {
		return poisonDamage;
	}

	public void setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	public boolean isPoisoned() {
		return poisonDamage > 0;
	}

	public Position getPositionToFace() {
		return positionToFace;
	}

	public Character setPositionToFace(Position positionToFace) {
		this.positionToFace = positionToFace;
		getUpdateFlag().flag(Flag.FACE_POSITION);
		return this;
	}

	public UpdateFlag getUpdateFlag() {
		return updateFlag;
	}

	public MovementQueue getMovementQueue() {
		return movementQueue;
	}

	public Combat getCombat() {
		return combat;
	}

	public Entity getInteractingEntity() {
		return interactingEntity;
	}

	public void setDirection(Direction direction) {
		int[] directionDeltas = direction.getDirectionDelta();
		setPositionToFace(getPosition().copy().add(directionDeltas[0], directionDeltas[1]));
	}

	public final void setRunningDirection(Direction secondaryDirection) {
		this.runningDirection = secondaryDirection;
	}


	public final Direction getLastDirection() {
		return lastDirection;
	}

	public final void setLastDirection(Direction lastDirection) {
		this.lastDirection = lastDirection;
	}

	public String getForcedChat() {
		return forcedChat;
	}

	public Character setForcedChat(String forcedChat) {
		this.forcedChat = forcedChat;
		return this;
	}

	public boolean[] getPrayerActive() {
		return prayerActive;
	}

	public boolean[] getCurseActive() {
		return curseActive;
	}

	public Character setPrayerActive(boolean[] prayerActive) {
		this.prayerActive = prayerActive;
		return this;
	}

	public Character setPrayerActive(int id, boolean prayerActive) {
		this.prayerActive[id] = prayerActive;
		return this;
	}

	public Character setCurseActive(boolean[] curseActive) {
		this.curseActive = curseActive;
		return this;
	}

	public Character setCurseActive(int id, boolean curseActive) {
		this.curseActive[id] = curseActive;
		return this;
	}

	public int getNpcTransformationId() {
		return npcTransformationId;
	}

	public Character setNpcTransformationId(int npcTransformationId) {
		this.npcTransformationId = npcTransformationId;
		return this;
	}

	public HitDamage decrementHealth(HitDamage hit) {
		if (getHitpoints() <= 0)
			return hit;
		if(hit.getDamage() > getHitpoints())
			hit.setDamage(getHitpoints());
		if(hit.getDamage() < 0)
			hit.setDamage(0);
		int outcome = getHitpoints() - hit.getDamage();
		if (outcome < 0)
			outcome = 0;
		setHitpoints(outcome);
		return hit;
	}

	/**
	 * Get the primary hit for this entity.
	 * 
	 * @return the primaryHit.
	 */
	public HitDamage getPrimaryHit() {
		return primaryHit;
	}
	
	public void setPrimaryHit(HitDamage hit) {
		this.primaryHit = hit;
	}
	
	public void setSecondaryHit(HitDamage hit) {
		this.secondaryHit = hit;
	}

	/**
	 * Get the secondary hit for this entity.
	 * 
	 * @return the secondaryHit.
	 */
	public HitDamage getSecondaryHit() {
		return secondaryHit;
	}

	/*
	 * Movement queue
	 */

	public void setWalkingDirection(Direction primaryDirection) {
		this.walkingDirection = primaryDirection;
	}

	public Direction getWalkingDirection() {
		return walkingDirection;
	}

	public Direction getRunningDirection() {
		return runningDirection;
	}

	/**
	 * Determines if this character needs to reset their movement queue.
	 *
	 * @return {@code true} if this character needs to reset their movement
	 *         queue, {@code false} otherwise.
	 */
	public final boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	/**
	 * Gets if this entity is registered.
	 * 
	 * @return the unregistered.
	 */
	public boolean isRegistered() {
		return registered;
	}

	/**
	 * Sets if this entity is registered,
	 *
     */
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	/**
	 * Sets the value for .
	 *
	 * @param resetMovementQueue
	 *            the new value to set.
	 */
	public final void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	public boolean isNeedsPlacement() {
		return needsPlacement;
	}

	public boolean hasVengeance() {
		return hasVengeance;
	}

	public void setHasVengeance(boolean hasVengeance) {
		this.hasVengeance = hasVengeance;
	}

	public boolean isSpecialActivated() {
		return specialActivated;
	}

	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
	}

	public int getSpecialPercentage() {
		return specialPercentage;
	}

	public void setSpecialPercentage(int specialPercentage) {
		this.specialPercentage = specialPercentage;
	}

	public void decrementSpecialPercentage(int drainAmount) {
		this.specialPercentage -= drainAmount;

		if (specialPercentage < 0) {
			specialPercentage = 0;
		}
	}

	public void incrementSpecialPercentage(int gainAmount) {
		this.specialPercentage += gainAmount;

		if (specialPercentage > 100) {
			specialPercentage = 100;
		}
	}

	public boolean isRecoveringSpecialAttack() {
		return recoveringSpecialAttack;
	}

	public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
		this.recoveringSpecialAttack = recoveringSpecialAttack;
	}

	public Player getAsPlayer() {
		return ((Player)this);
	}

	public NPC getAsNpc() {
		return ((NPC)this);
	}

	public boolean isUntargetable() {
		return untargetable;
	}

	public void setUntargetable(boolean untargetable) {
		this.untargetable = untargetable;
	}
}