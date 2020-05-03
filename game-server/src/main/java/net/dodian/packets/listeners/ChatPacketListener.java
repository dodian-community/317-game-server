package net.dodian.packets.listeners;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.Opcode;
import net.dodian.packets.models.ChatPacket;

import static net.dodian.old.net.packet.PacketConstants.CHAT_OPCODE_1;

@Opcode(CHAT_OPCODE_1)
public abstract class ChatPacketListener implements PacketListenerBase {

    private ChatPacket chatPacket;

    @Override
    public ChatPacketListener create(Packet packet, Player player) {
        this.chatPacket = new ChatPacket(packet, player);
        return this;
    }
}
