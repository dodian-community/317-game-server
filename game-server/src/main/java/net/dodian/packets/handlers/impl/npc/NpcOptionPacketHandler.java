package net.dodian.packets.handlers.impl.npc;

import net.dodian.events.impl.player.interact.npc.PlayerNpcEvent;
import net.dodian.events.impl.player.interact.npc.PlayerNpcFirstClickEvent;
import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.npc.*;
import org.springframework.stereotype.Component;

@Component
public class NpcOptionPacketHandler extends PacketListener {

    public <T extends PlayerNpcEvent> void handleClick(T event, NpcOptionPacket packet) {
        if(packet.getNpcIndex() < 0 || packet.getNpcIndex() > World.getNpcs().capacity()) {
            return;
        }

        final NPC target = World.getNpcs().get(packet.getNpcIndex());

        if(target == null) {
            return;
        }

        packet.getPlayer().setEntityInteraction(target);
        packet.getPlayer().setWalkToTask(new WalkToTask(packet.player, target.getPosition(), target.getSize(), () -> {
            eventsProvider.executeListeners(event.create(packet.getPlayer(), target));
            packet.getPlayer().setPositionToFace(target.getPosition());
            target.setPositionToFace(packet.getPlayer().getPosition());
        }));
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
