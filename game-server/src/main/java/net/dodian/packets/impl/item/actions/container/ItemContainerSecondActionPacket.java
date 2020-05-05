package net.dodian.packets.impl.item.actions.container;

import lombok.Getter;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.SECOND_ITEM_CONTAINER_ACTION_OPCODE;

@Component
@Getter
@Opcodes(SECOND_ITEM_CONTAINER_ACTION_OPCODE)
public class ItemContainerSecondActionPacket extends ItemContainerActionPacket {
}
