package net.dodian.packets.impl.item.actions;

import lombok.Getter;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.*;

@Component
@Getter
@Opcodes({THIRD_ITEM_ACTION_OPCODE})
public class ItemThirdActionPacket extends ItemActionPacket {
}
