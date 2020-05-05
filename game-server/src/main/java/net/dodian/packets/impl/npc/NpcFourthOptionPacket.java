package net.dodian.packets.impl.npc;

import lombok.Getter;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.FOURTH_NPC_CLICK_OPCODE;

@Component
@Getter
@Opcodes(FOURTH_NPC_CLICK_OPCODE)
public class NpcFourthOptionPacket extends NpcOptionPacket {
}
