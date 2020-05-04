package net.dodian.packets.impl;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.CHAT_SETTINGS_OPCODE;

@Component
@Getter
@Opcodes(CHAT_SETTINGS_OPCODE)
public class ChatSettingsPacket extends GamePacket {

    private int publicMode;
    private int privateMode;
    private int tradeMode;

    @Override
    public ChatSettingsPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.publicMode = packet.readByte();
        this.privateMode = packet.readByte();
        this.tradeMode = packet.readByte();
        return this;
    }
}
