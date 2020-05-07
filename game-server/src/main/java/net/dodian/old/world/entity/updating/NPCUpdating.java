package net.dodian.old.world.entity.updating;

import net.dodian.old.net.packet.ByteOrder;
import net.dodian.old.net.packet.PacketBuilder;
import net.dodian.old.net.packet.ValueType;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.Entity;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Direction;
import net.dodian.old.world.model.Flag;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.UpdateFlag;

import java.util.Iterator;

/**
 * Represents a player's npc updating task, which loops through all local
 * npcs and updates their masks according to their current attributes.
 * 
 * @author Relex lawl
 */

public class NPCUpdating {

	/**
	 * Handles the actual npc updating for the associated player.
	 * @return	The NPCUpdating instance.
	 */
	public static void update(Player player) {
		PacketBuilder update = new PacketBuilder();
		PacketBuilder packet = new PacketBuilder(65);
		packet.initializeAccess(PacketBuilder.AccessType.BIT);
		packet.putBits(8, player.getLocalNpcs().size());
		for (Iterator<NPC> npcIterator = player.getLocalNpcs().iterator(); npcIterator.hasNext();) {
			NPC npc = npcIterator.next();
			if (World.getNpcs().get(npc.getIndex()) != null && npc.isVisible() && player.getPosition().isWithinDistance(npc.getPosition()) && !npc.isNeedsPlacement()) {
				updateMovement(npc, packet);
				if (npc.getUpdateFlag().isUpdateRequired()) {
					appendUpdates(npc, update);
				}
			} else {
				//	player.getNpcFacesUpdated().remove(npc);
				npcIterator.remove();
				packet.putBits(1, 1);
				packet.putBits(2, 3);
			}
		}
		for(NPC npc : World.getNpcs()) {
			if (player.getLocalNpcs().size() >= 79) //Originally 255
				break;
			if (npc == null || player.getLocalNpcs().contains(npc) || !npc.isVisible() || npc.isNeedsPlacement())
				continue;
			if (npc.getPosition().isWithinDistance(player.getPosition())) {
				player.getLocalNpcs().add(npc);
				addNPC(player, npc, packet);
				if (npc.getUpdateFlag().isUpdateRequired()) {
					appendUpdates(npc, update);
				}
			}
		}
		if (update.buffer().writerIndex() > 0) {
			packet.putBits(14, 16383);
			packet.initializeAccess(PacketBuilder.AccessType.BYTE);
			packet.writeBuffer(update.buffer());
		} else {
			packet.initializeAccess(PacketBuilder.AccessType.BYTE);
		}
		player.getSession().writeAndFlush(packet);
	}

	/**
	 * Adds an npc to the associated player's client.
	 * @param npc		The npc to add.
	 * @param builder	The packet builder to write information on.
	 * @return			The NPCUpdating instance.
	 */
	private static void addNPC(Player player, NPC npc, PacketBuilder builder) {
		builder.putBits(14, npc.getIndex());
		builder.putBits(5, npc.getPosition().getY()-player.getPosition().getY());
		builder.putBits(5, npc.getPosition().getX()-player.getPosition().getX());
		builder.putBits(1, 0);
		builder.putBits(14, npc.getId());
		builder.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
	}

	/**
	 * Updates the npc's movement queue.
	 * @param npc		The npc who's movement is updated.
	 * @return			The NPCUpdating instance.
	 */
	private static void updateMovement(NPC npc, PacketBuilder out) {
		if (npc.getRunningDirection().toInteger() == -1) {
			if (npc.getWalkingDirection().toInteger() == -1) {
				if (npc.getUpdateFlag().isUpdateRequired()) {
					out.putBits(1, 1);
					out.putBits(2, 0);
				} else {
					out.putBits(1, 0);
				}
			} else {
				out.putBits(1, 1);
				out.putBits(2, 1);
				out.putBits(3, npc.getWalkingDirection().toInteger());
				out.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
			}
		} else {
			out.putBits(1, 1);
			out.putBits(2, 2);
			out.putBits(3, npc.getWalkingDirection().toInteger());
			out.putBits(3, npc.getRunningDirection().toInteger());
			out.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
		}
	}

	/**
	 * Appends a mask update for {@code npc}.
	 * @param npc		The npc to update masks for.
	 * @return			The NPCUpdating instance.
	 */
	private static void appendUpdates(NPC npc, PacketBuilder block) {
		int mask = 0;
		UpdateFlag flag = npc.getUpdateFlag();
		if (flag.flagged(Flag.ANIMATION) && npc.getAnimation() != null) {
			mask |= 0x10;
		}
		if (flag.flagged(Flag.GRAPHIC) && npc.getGraphic() != null) {
			mask |= 0x80;
		}
		if (flag.flagged(Flag.SINGLE_HIT)) {
			mask |= 0x8;
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			mask |= 0x20;
		}
		if (flag.flagged(Flag.FORCED_CHAT) && npc.getForcedChat().length() > 0) {
			mask |= 0x1;
		}
		if (flag.flagged(Flag.DOUBLE_HIT)) {
			mask |= 0x40;
		}
		if (flag.flagged(Flag.NPC_APPEARANCE) && npc.getTransformationId() != -1) {
			mask |= 0x2;
		}
		if (flag.flagged(Flag.FACE_POSITION) && npc.getPositionToFace() != null) {
			mask |= 0x4;
		}
		block.put(mask);
		if (flag.flagged(Flag.ANIMATION) && npc.getAnimation() != null) {
			updateAnimation(block, npc);
		}
		if (flag.flagged(Flag.GRAPHIC) && npc.getGraphic() != null) {
			updateGraphics(block, npc);
		}
		if (flag.flagged(Flag.SINGLE_HIT)) {
			updateSingleHit(block, npc);
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			Entity entity = npc.getInteractingEntity();
			block.putShort(entity == null ? -1 : entity.getIndex() + (entity instanceof Player ? 32768 : 0));
		}
		if (flag.flagged(Flag.FORCED_CHAT) && npc.getForcedChat().length() > 0) {
			block.putString(npc.getForcedChat());
		}
		if (flag.flagged(Flag.DOUBLE_HIT)) {
			updateDoubleHit(block, npc);
		}
		if (flag.flagged(Flag.NPC_APPEARANCE)) {
			boolean transform = npc.getTransformationId() != -1;
			
			//Changes the npc's headicon.
			block.put(npc.getHeadIcon());
			
			//Should we transform the npc into anotehr npc?
			block.put(transform ? 1 : 0);
			
			//Transforms the npc into another npc.
			if(transform) {
				block.putShort(npc.getTransformationId(), ValueType.A, ByteOrder.LITTLE);
			}
		}
		if (flag.flagged(Flag.FACE_POSITION) && npc.getPositionToFace() != null) {
			final Position position = npc.getPositionToFace();
			int x = position == null ? 0 : position.getX(); 
			int y = position == null ? 0 : position.getY();
			block.putShort(x * 2 + 1, ByteOrder.LITTLE);
			block.putShort(y * 2 + 1, ByteOrder.LITTLE);
		}
	}

	/**
	 * Updates {@code npc}'s current animation and displays it for all local players.
	 * @param builder	The packet builder to write information on.
	 * @param npc		The npc to update animation for.
	 * @return			The NPCUpdating instance.
	 */
	private static void updateAnimation(PacketBuilder builder, NPC npc) {
		builder.putShort(npc.getAnimation().getId(), ByteOrder.LITTLE);
		builder.put(npc.getAnimation().getDelay());
	}

	/**
	 * Updates {@code npc}'s current graphics and displays it for all local players.
	 * @param builder	The packet builder to write information on.
	 * @param npc		The npc to update graphics for.
	 * @return			The NPCUpdating instance.
	 */
	private static void updateGraphics(PacketBuilder builder, NPC npc) {
		builder.putShort(npc.getGraphic().getId());
		builder.putInt(((npc.getGraphic().getHeight().ordinal() * 50) << 16) + (npc.getGraphic().getDelay() & 0xffff));
	}

	/**
	 * Updates the npc's single hit.
	 * @param builder	The packet builder to write information on.
	 * @param npc		The npc to update the single hit for.
	 * @return			The NPCUpdating instance.
	 */
	private static void updateSingleHit(PacketBuilder builder, NPC npc) {
		builder.putShort(npc.getPrimaryHit().getDamage());
		builder.put(npc.getPrimaryHit().getHitmask().ordinal());
		builder.putShort(npc.getHitpoints());
		builder.putShort(npc.getDefinition().getHitpoints());

	}

	/**
	 * Updates the npc's double hit.
	 * @param builder	The packet builder to write information on.
	 * @param npc		The npc to update the double hit for.
	 * @return			The NPCUpdating instance.
	 */
	private static void updateDoubleHit(PacketBuilder builder, NPC npc) {
		builder.putShort(npc.getSecondaryHit().getDamage());
		builder.put(npc.getSecondaryHit().getHitmask().ordinal());
		builder.putShort(npc.getHitpoints());
		builder.putShort(npc.getDefinition().getHitpoints());
	}

	/**
	 * Resets all the npc's flags that should be reset after a tick's update
	 * @param npc	The npc to reset flags for.
	 */
	public static void resetFlags(NPC npc) {
		npc.getUpdateFlag().reset();
		npc.setForcedChat("");
		npc.setNeedsPlacement(false);
		npc.setResetMovementQueue(false);
		npc.setWalkingDirection(Direction.NONE);
		npc.setRunningDirection(Direction.NONE);
		npc.performAnimation(null);
		npc.performGraphic(null);
	}
}
