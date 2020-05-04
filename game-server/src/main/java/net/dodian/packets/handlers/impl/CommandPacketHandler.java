package net.dodian.packets.handlers.impl;

import net.dodian.commands.Command;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.CommandPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class CommandPacketHandler implements PacketListener {

    private final List<Command> commands;

    @Autowired
    public CommandPacketHandler(List<Command> commands) {
        this.commands = commands;
    }

    @PacketHandler
    public void onCommand(CommandPacket packet) {
        List<String> parts = new LinkedList<>(Arrays.asList(packet.getCommand().split(" ")));

        if(packet.getCommand().contains("\r") || packet.getCommand().contains("\n")) {
            return;
        }

        if(packet.getPlayer() == null || packet.getPlayer().getHitpoints() <= 0) {
            return;
        }

        Optional<Command> optionalCommand = commands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(parts.get(0)))
                .findFirst();

        optionalCommand.ifPresent(command -> {
            parts.remove(0);
            command.execute(packet.getPlayer(), parts);
        });
    }
}
