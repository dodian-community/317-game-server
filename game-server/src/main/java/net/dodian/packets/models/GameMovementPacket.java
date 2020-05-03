package net.dodian.packets.models;

import lombok.Data;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;

@Data
public class GameMovementPacket {
    private int size;
    private int[][] path;
    private int firstStepX;
    private int firstStepY;
    private Position[] positions;
    private Player player;
    private int steps;

    public GameMovementPacket(Packet packet, Player player) {
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
    }
}
