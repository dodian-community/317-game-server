package net.dodian.packets.listeners;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.Opcode;
import net.dodian.packets.models.GameMovementPacket;

import static net.dodian.old.net.packet.PacketConstants.GAME_MOVEMENT_OPCODE;

@Opcode(GAME_MOVEMENT_OPCODE)
public abstract class GameMovementPacketListener implements PacketListenerBase {

    protected GameMovementPacket packet;

    @Override
    public GameMovementPacketListener create(Packet packet, Player player) {
        this.packet = new GameMovementPacket(packet, player);
        return this;
    }
}
