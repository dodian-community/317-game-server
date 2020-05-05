package net.dodian.packets.impl.item.actions.container;

import lombok.Getter;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.FIRST_ITEM_CONTAINER_ACTION_OPCODE;

@Component
@Getter
@Opcodes(FIRST_ITEM_CONTAINER_ACTION_OPCODE)
public class ItemContainerFirstActionPacket extends ItemContainerActionPacket {
}
