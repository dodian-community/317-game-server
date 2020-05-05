package net.dodian.packets.impl.object;

import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.OBJECT_SECOND_CLICK_OPCODE;

@Component
@Opcodes(OBJECT_SECOND_CLICK_OPCODE)
public class ObjectSecondClickActionPacket extends ObjectActionPacket {
}
