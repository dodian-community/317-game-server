package net.dodian.packets.impl.player;

import lombok.Getter;
import lombok.Setter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.*;

@Component
@Opcodes({GAME_MOVEMENT_OPCODE, MINIMAP_MOVEMENT_OPCODE, COMMAND_MOVEMENT_OPCODE})
public class GameMovementPacket extends GamePacket {
    @Getter @Setter private int size;
    @Getter @Setter private int[][] path;
    @Getter @Setter private int firstStepX;
    @Getter @Setter private int firstStepY;
    @Getter @Setter private Position[] positions;
    @Getter @Setter private Player player;
    @Getter @Setter private int steps;

    @Override
    public GameMovementPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.size = packet.getSize();
        this.firstStepX = packet.readLEShortA();

        this.steps = (size - 5) / 2;
        this.path = new int[steps][2];
        for(int i = 0; i < steps; i++) {
            path[i][0] = packet.readByte();
            path[i][1] = packet.readByte();
        }
        this.firstStepY = packet.readLEShort();
        this.positions = new Position[steps + 1];
        positions[0] = new Position(firstStepX, firstStepY, player.getPosition().getZ());
        return this;
    }
}
