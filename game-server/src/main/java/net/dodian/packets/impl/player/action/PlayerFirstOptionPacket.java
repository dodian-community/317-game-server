package net.dodian.packets.impl.player.action;

import lombok.Getter;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.*;

@Component
@Getter
@Opcodes(PLAYER_OPTION_1_OPCODE)
public class PlayerFirstOptionPacket extends PlayerOptionPacket {
}
