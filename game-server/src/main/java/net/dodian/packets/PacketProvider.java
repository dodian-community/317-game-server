package net.dodian.packets;

import net.dodian.Server;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.handlers.PacketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Component
public class PacketProvider {

    private final List<GamePacket> packets;
    private final List<PacketListener> listeners;

    @Autowired
    public PacketProvider(List<GamePacket> packets, List<PacketListener> listeners) {
        this.packets = packets;
        this.listeners = listeners;
    }

    public boolean handlePacket(Packet packet, Player player) {
        if(packet.getOpcode() <= 0) {
            return false;
        }

        Server.getLogger().log(Level.INFO, "Received packet with opcode: " + packet.getOpcode());

        Optional<? extends GamePacket> optionalGamePacket = packets.stream()
                .filter(gamePacket -> gamePacketHasOpcode(gamePacket, packet.getOpcode()))
                .findFirst();

        if(optionalGamePacket.isEmpty()) {
            Server.getLogger().log(Level.INFO, "Couldn't find the packet for opcode: " + packet.getOpcode());
            return false;
        }

        GamePacket gamePacket = optionalGamePacket.get().createFrom(packet, player);
        gamePacket.setOpcode(packet.getOpcode());

        listeners.forEach(listener -> {
            List<Method> methods = Arrays.stream(listener.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(PacketHandler.class))
                    .filter(method -> method.getParameterTypes()[0].equals(gamePacket.getClass()))
                    .collect(Collectors.toList());

            methods.forEach(method -> {
                try {
                    if(!gamePacket.isCancelled()) {
                        method.invoke(listener, gamePacket);
                    } else {
                        Server.getLogger().log(Level.INFO, listener.getClass().getSimpleName() + ": packet execution was cancelled by a previous listener.");
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });

        gamePacket.setCancelled(false);

        return true;
    }

    private boolean gamePacketHasOpcode(GamePacket gamePacket, int opcode) {
        if(gamePacket.getClass().isAnnotationPresent(Opcodes.class)) {
            return Arrays.stream(gamePacket.getClass().getAnnotation(Opcodes.class).value())
                    .anyMatch(code -> code == opcode);
        }

        return false;
    }
}
