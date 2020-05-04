package net.dodian.packets.impl.item;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static net.dodian.packets.PacketConstants.*;

@Component
@Getter
@Opcodes({FIRST_ITEM_CONTAINER_ACTION_OPCODE, SECOND_ITEM_CONTAINER_ACTION_OPCODE,
        THIRD_ITEM_CONTAINER_ACTION_OPCODE, FOURTH_ITEM_CONTAINER_ACTION_OPCODE,
        FIFTH_ITEM_CONTAINER_ACTION_OPCODE, SIXTH_ITEM_CONTAINER_ACTION_OPCODE})
public class ItemContainerActionPacket extends GamePacket {

    private int interfaceId;
    private int slot;
    private int id;
    private Action action;

    @Override
    public ItemContainerActionPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.interfaceId = packet.readInt();
        this.slot = packet.readShortA();
        this.id = packet.readShortA();
        this.action = Arrays.stream(Action.values())
                .filter(action -> action.getOpcode() == packet.getOpcode())
                .findFirst().orElse(Action.FIRST);
        return this;
    }

    private enum Action {
        FIRST(FIRST_ITEM_CONTAINER_ACTION_OPCODE),
        SECOND(SECOND_ITEM_CONTAINER_ACTION_OPCODE),
        THIRD(THIRD_ITEM_CONTAINER_ACTION_OPCODE),
        FOURTH(FOURTH_ITEM_CONTAINER_ACTION_OPCODE),
        FIFTH(FIFTH_ITEM_CONTAINER_ACTION_OPCODE),
        SIXTH(SIXTH_ITEM_CONTAINER_ACTION_OPCODE);

        int opcode;

        Action(int opcode) {
            this.opcode = opcode;
        }

        public int getOpcode() {
            return opcode;
        }
    }
}
