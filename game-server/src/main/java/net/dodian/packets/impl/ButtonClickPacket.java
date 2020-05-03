package net.dodian.packets.impl;

import lombok.Getter;
import lombok.Setter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.old.net.packet.PacketConstants.BUTTON_CLICK_OPCODE;

@Component
@Opcodes(BUTTON_CLICK_OPCODE)
public class ButtonClickPacket extends GamePacket {
    @Getter @Setter private int button;

    @Override
    public GamePacket createFrom(Packet packet, Player player) {
        this.button = packet.readInt();
        this.player = player;
        return this;
    }
}
