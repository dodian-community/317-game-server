package net.dodian.packets.impl.item;

import lombok.Getter;
import lombok.Setter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.old.net.packet.PacketConstants.EXAMINE_ITEM_OPCODE;

@Component
@Opcodes(EXAMINE_ITEM_OPCODE)
@Getter @Setter
public class ExamineItemPacket extends GamePacket {

    @Override
    public GamePacket createFrom(Packet packet, Player player) {
        this.player = player;
        return this;
    }
}
