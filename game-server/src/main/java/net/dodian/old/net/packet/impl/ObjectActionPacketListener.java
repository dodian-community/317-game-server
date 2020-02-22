package net.dodian.old.net.packet.impl;

import net.dodian.Server;
import net.dodian.extend.events.object.ObjectActionEventListener;
import net.dodian.old.definitions.ObjectDefinition;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.collision.region.RegionClipping;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ObjectActionPacketListener implements PacketListener {

	private final List<ObjectActionEventListener> objectActionEventListeners;

	@Autowired
	public ObjectActionPacketListener(Optional<List<ObjectActionEventListener>> objectActionEventListeners) {
		this.objectActionEventListeners = objectActionEventListeners.orElse(new ArrayList<>());
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}

		//Make sure we aren't doing something else..
		if(player.busy()) {
			return;
		}

		final int id = packet.readLEShortA();
		final int y = packet.readLEShort();
		final int x = packet.readUnsignedShortA();
		final Position position = new Position(x, y, player.getPosition().getZ());

		//Make sure object exists...
		if(!RegionClipping.objectExists(id, position)) {
			Server.getLogger().info("Object with id "+id+" does not exist in region.");
			return;
		}

		//Get object definition
		final ObjectDefinition def = ObjectDefinition.forId(id);
		if(def == null) {
			Server.getLogger().info("ObjectDefinition for object "+id+" is null.");
			return;
		}

		//Calculate object size...
		final int size = (def.getSizeX() + def.getSizeY()) - 1;

		//Face object..
		player.setPositionToFace(position);

		switch (packet.getOpcode()) {
		case PacketConstants.OBJECT_FIRST_CLICK_OPCODE:
			objectActionEventListeners.forEach(objectActionEventListener -> objectActionEventListener
					.onFirstClick(player, new GameObject(id, position)));
			break;
		case PacketConstants.OBJECT_SECOND_CLICK_OPCODE:
			objectActionEventListeners.forEach(objectActionEventListener -> objectActionEventListener
					.onSecondClick(player, new GameObject(id, position)));
			break;
		case PacketConstants.OBJECT_THIRD_CLICK_OPCODE:
			objectActionEventListeners.forEach(objectActionEventListener -> objectActionEventListener
					.onThirdClick(player, new GameObject(id, position)));
			break;
		case PacketConstants.OBJECT_FOURTH_CLICK_OPCODE:
			objectActionEventListeners.forEach(objectActionEventListener -> objectActionEventListener
					.onFourthClick(player, new GameObject(id, position)));
			break;
		case PacketConstants.OBJECT_FIFTH_CLICK_OPCODE:
			objectActionEventListeners.forEach(objectActionEventListener -> objectActionEventListener
					.onFifthClick(player, new GameObject(id, position)));
			break;
		}
	}

	private static final int MAGICAL_ALTAR = 29150;
	private static final int REJUVENATION_POOL = 29241;
	private static final int DITCH_PORTAL = 4151;
	private static final int EDGEVILLE_BANK = 6943;
	private static final int BANK_CHEST = 2693;
	private static final int WILDERNESS_DITCH = 23271;
}
