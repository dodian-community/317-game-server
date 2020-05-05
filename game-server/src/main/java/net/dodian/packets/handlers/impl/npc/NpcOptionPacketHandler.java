package net.dodian.packets.handlers.impl.npc;

import net.dodian.events.EventsProvider;
import net.dodian.events.impl.player.interact.npc.*;
import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.npc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NpcOptionPacketHandler implements PacketListener {

    private final EventsProvider eventsProvider;

    @Autowired
    public NpcOptionPacketHandler(EventsProvider eventsProvider) {
        this.eventsProvider = eventsProvider;
    }

    public Optional<NPC> getTarget(NpcOptionPacket packet) {
        if(packet.getNpcIndex() < 0 || packet.getNpcIndex() > World.getNpcs().capacity()) {
            return Optional.empty();
        }

        final NPC npc = World.getNpcs().get(packet.getNpcIndex());

        if(npc == null) {
            return Optional.empty();
        }

        return Optional.of(npc);
    }

    public <T extends PlayerNpcEvent> void handleClick(T event, NpcOptionPacket packet) {
        getTarget(packet).ifPresent(target -> {
            packet.getPlayer().setEntityInteraction(target);
            packet.getPlayer().setWalkToTask(new WalkToTask(packet.player, target.getPosition(), target.getSize(), () -> {
                eventsProvider.executeListeners(event.create(packet.getPlayer(), target));
                packet.getPlayer().setPositionToFace(target.getPosition());
                target.setPositionToFace(packet.getPlayer().getPosition());
            }));
        });
    }

    @PacketHandler
    public void onFirstClick(NpcFirstOptionPacket packet) {
        handleClick(new PlayerNpcFirstClickEvent(), packet);
    }

    @PacketHandler
    public void onSecondClick(NpcSecondOptionPacket packet) {
        handleClick(new PlayerNpcFirstClickEvent(), packet);
    }

    @PacketHandler
    public void onThirdClick(NpcThirdOptionPacket packet) {
        handleClick(new PlayerNpcFirstClickEvent(), packet);
    }

    @PacketHandler
    public void onFourthClick(NpcFourthOptionPacket packet) {
        handleClick(new PlayerNpcFirstClickEvent(), packet);
    }
}
