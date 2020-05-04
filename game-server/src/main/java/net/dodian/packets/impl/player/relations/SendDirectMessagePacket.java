package net.dodian.packets.impl.player.relations;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.util.Misc;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.SEND_PM_OPCODE;

@Component
@Getter
@Opcodes(SEND_PM_OPCODE)
public class SendDirectMessagePacket extends GamePacket {

    private Player friend;
    private String message;

    @Override
    public SendDirectMessagePacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.friend = World.getPlayerByName(Misc.formatText(Misc.longToString(packet.readLong()).replaceAll("_", " ")));
        this.message = packet.readString();
        return this;
    }
}
