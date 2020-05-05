package net.dodian.packets.handlers.impl.player;

import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Locations;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.player.PlayerAttackPacket;
import net.dodian.packets.impl.player.action.PlayerFirstOptionPacket;
import net.dodian.packets.impl.player.action.PlayerOptionPacket;
import net.dodian.packets.impl.player.action.PlayerSecondOptionPacket;
import net.dodian.packets.impl.player.action.PlayerThirdOptionPacket;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerOptionsPacketHandler extends PacketListener {

    public <T extends PlayerOptionPacket> Optional<Player> getTarget(T packet) {
        if(packet.getId() < 0 || packet.getId() > World.getPlayers().capacity()) {
            return Optional.empty();
        }

        Player target = World.getPlayers().get(packet.getId());
        if(target == null) {
            return Optional.empty();
        }

        return Optional.of(target);
    }

    @PacketHandler
    public void onAttack(PlayerAttackPacket packet) {
        if(packet.getId() < 0 || packet.getId() > World.getPlayers().capacity()) {
            return;
        }

        final Player target = World.getPlayers().get(packet.getId());

        if(target == null || target.getHitpoints() <= 0 || target.equals(packet.getPlayer())) {
            packet.getPlayer().getMovementQueue().reset();
            return;
        }

        packet.getPlayer().getCombat().attack(target);
    }

    @PacketHandler
    public void onFirstClick(PlayerFirstOptionPacket packet) {
        getTarget(packet).ifPresent(target -> packet.getPlayer().setWalkToTask(new WalkToTask(packet.getPlayer(), target.getPosition(), 1, () -> {
            if(packet.getPlayer().getLocation() == Locations.Location.DEFAULT) {
                if(packet.getPlayer().busy()) {
                    packet.getPlayer().getPacketSender().sendMessage("You cannot do that right now.");
                    return;
                }

                if(target.busy()) {
                    packet.getPlayer().getPacketSender().sendMessage("That player is currently busy.");
                    return;
                }

                packet.getPlayer().getDueling().requestDuel(target);
            }
        })));
    }

    @PacketHandler
    public void onSecondClick(PlayerSecondOptionPacket packet) {

    }

    @PacketHandler
    public void onThirdClick(PlayerThirdOptionPacket packet) {

    }
}
