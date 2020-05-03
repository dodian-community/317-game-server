package net.dodian.packets.models;

import lombok.Data;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;

@Data
public class ChatPacket {
    private int color;
    private int effect;
    private String text;
    private Player player;

    public ChatPacket() {

    }

    public ChatPacket(Packet packet, Player player) {
        this.color = packet.readByte();
        this.effect = packet.readByte();
        this.text = packet.readString();
        this.player = player;
    }
}
