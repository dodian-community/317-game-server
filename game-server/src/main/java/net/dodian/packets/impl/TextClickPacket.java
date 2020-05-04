package net.dodian.packets.impl;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.TEXT_CLICK_OPCODE;

@Component
@Getter
@Opcodes(TEXT_CLICK_OPCODE)
public class TextClickPacket extends GamePacket {

    private int frame;
    private int action;

    @Override
    public TextClickPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.frame = packet.readInt();
        this.action = packet.readByte();
        return this;
    }
}
