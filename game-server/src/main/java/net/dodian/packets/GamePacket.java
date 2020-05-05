package net.dodian.packets;

import lombok.Data;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Data
@Component
public abstract class GamePacket {
    public Player player;
    public int opcode;
    public boolean cancelled = false;

    public abstract GamePacket createFrom(Packet packet, Player player);

    public void cancel() {
        this.cancelled = true;
    }
}
