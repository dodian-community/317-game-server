package net.dodian.packets;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.listeners.PacketListenerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PacketProvider {

    private final List<PacketListenerBase> packetListeners;

    @Autowired
    public PacketProvider(List<PacketListenerBase> packetListeners) {
        this.packetListeners = packetListeners;
    }

    public void handlePacket(Packet packet, Player player) {
        if(packet.getOpcode() <= 0) {
            return;
        }

        List<PacketListenerBase> listeners = packetListeners.stream().filter(listener -> {
            if(listener.getClass().getSuperclass().isAnnotationPresent(Opcode.class)) {
                return listener.getClass().getSuperclass().getAnnotation(Opcode.class).value() == packet.getOpcode();
            }

            return false;
        }).collect(Collectors.toList());

        listeners.forEach(listener -> listener.create(packet, player).handle());
    }
}
