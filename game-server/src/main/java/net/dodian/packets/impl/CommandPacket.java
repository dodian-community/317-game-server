package net.dodian.packets.impl;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.COMMAND_OPCODE;

@Component
@Opcodes(COMMAND_OPCODE)
@Getter
public class CommandPacket extends GamePacket {
    private String command;

    @Override
    public CommandPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.command = packet.readString();
        return this;
    }
}
