package net.dodian.packets.handlers.impl;

import net.dodian.Server;
import net.dodian.game.events.impl.player.interact.object.*;
import net.dodian.old.definitions.ObjectDefinition;
import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.world.collision.region.RegionClipping;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.model.Item;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.item.use.ItemOnObjectPacket;
import net.dodian.packets.impl.object.*;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

@Component
public class ObjectActionPacketHandler extends PacketListener {

    public <T extends PlayerObjectEvent> void handleClick(T event, ObjectActionPacket packet) {
        Server.getLogger().log(Level.SEVERE, "[" + packet.getClass().getSimpleName() + "] Attempting to use object: " + packet.getId() + " at " + packet.getPosition().toString());

        if(!RegionClipping.objectExists(packet.getId(), packet.getPosition())) {
            Server.getLogger().log(Level.SEVERE, "[" + packet.getClass().getSimpleName() + "] Object not found: " + packet.getId() + " at " + packet.getPosition().toString());
            return;
        }

        ObjectDefinition objectDefinition = ObjectDefinition.forId(packet.getId());
        if(objectDefinition == null) {
            return;
        }

        int size = (objectDefinition.getSizeX() + objectDefinition.getSizeY()) - 1;

        packet.getPlayer().setPositionToFace(packet.getPosition());

        packet.getPlayer().setWalkToTask(new WalkToTask(packet.getPlayer(), packet.getPosition(), size, () -> {
            eventsProvider.executeListeners(event.create(packet.getPlayer(), new GameObject(packet.getId(), packet.getPosition())));
        }));
    }

    public <T extends PlayerItemUsageEvent> void handleUseItem(PlayerItemOnObjectEvent event, ItemOnObjectPacket packet) {
        Item item = packet.getPlayer().getInventory().get(packet.getItemSlot());
        Server.getLogger().log(Level.SEVERE, "[" + packet.getClass().getSimpleName() + "] Attempting to use item: " + item.getId() + " at object " + packet.getGameObject().getId());

        if(!RegionClipping.objectExists(packet.getGameObject())) {
            Server.getLogger().log(Level.SEVERE, "[" + packet.getClass().getSimpleName() + "] Object not found: " + packet.getGameObject().getId() + " at " + packet.getGameObject().getPosition().toString());
            return;
        }

        ObjectDefinition objectDefinition = ObjectDefinition.forId(packet.getGameObject().getId());
        if(objectDefinition == null) {
            return;
        }

        int size = (objectDefinition.getSizeX() + objectDefinition.getSizeY()) - 1;

        packet.getPlayer().setPositionToFace(packet.getGameObject().getPosition());

        packet.getPlayer().setWalkToTask(new WalkToTask(packet.getPlayer(), packet.getGameObject().getPosition(), size, () -> {
            eventsProvider.executeListeners(event.create(packet.getPlayer(), item, packet.getGameObject()));
        }));
    }

    @PacketHandler
    public void onFirstClick(ObjectFirstClickActionPacket packet) {
        handleClick(new PlayerObjectFirstClickEvent(), packet);
    }

    @PacketHandler
    public void onSecondClick(ObjectSecondClickActionPacket packet) {
        handleClick(new PlayerObjectSecondClickEvent(), packet);
    }

    @PacketHandler
    public void onThirdClick(ObjectThirdClickActionPacket packet) {
        handleClick(new PlayerObjectThirdClickEvent(), packet);
    }

    @PacketHandler
    public void onFourthClick(ObjectFourthClickActionPacket packet) {
        handleClick(new PlayerObjectFourthClickEvent(), packet);
    }

    @PacketHandler
    public void onFifthClick(ObjectFifthClickActionPacket packet) {
        handleClick(new PlayerObjectFifthClickEvent(), packet);
    }

    @PacketHandler
    public void onItemUsage(ItemOnObjectPacket packet) {
        handleUseItem(new PlayerItemOnObjectEvent(), packet);
    }
}
