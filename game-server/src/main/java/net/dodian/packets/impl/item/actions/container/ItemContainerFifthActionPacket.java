package net.dodian.packets.impl.item.actions.container;

import lombok.Getter;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.*;

@Component
@Getter
@Opcodes(FIFTH_ITEM_CONTAINER_ACTION_OPCODE)
public class ItemContainerFifthActionPacket extends ItemContainerActionPacket {
}
