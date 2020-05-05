package net.dodian.packets.impl.item.actions.container;

import lombok.Getter;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.FOURTH_ITEM_CONTAINER_ACTION_OPCODE;

@Component
@Getter
@Opcodes(FOURTH_ITEM_CONTAINER_ACTION_OPCODE)
public class ItemContainerFourthActionPacket extends ItemContainerActionPacket {
}
