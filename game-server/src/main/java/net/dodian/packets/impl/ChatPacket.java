package net.dodian.packets.impl;

import lombok.Getter;
import lombok.Setter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.CHAT_OPCODE_1;

@Component
@Opcodes(CHAT_OPCODE_1)
public class ChatPacket extends GamePacket {
    @Getter @Setter private int color;
    @Getter @Setter private int effect;
    @Getter @Setter private String text;
    @Getter @Setter private Player player;

    @Override
    public ChatPacket createFrom(Packet packet, Player player) {
        this.color = packet.readByte();
        this.effect = packet.readByte();
        this.text = packet.readString();
        this.player = player;
        return this;
    }
}
