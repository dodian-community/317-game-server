package net.dodian.packets.impl;

import lombok.Getter;
import net.dodian.old.net.ByteBufUtils;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static net.dodian.packets.PacketConstants.ENTER_AMOUNT_OPCODE;
import static net.dodian.packets.PacketConstants.ENTER_SYNTAX_OPCODE;

@Component
@Getter
@Opcodes({ENTER_AMOUNT_OPCODE, ENTER_SYNTAX_OPCODE})
public class EnterInputPacket extends GamePacket {

    private InputType inputType;
    private Object value;

    @Override
    public EnterInputPacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.inputType = Arrays.stream(InputType.values())
                .filter(inputType -> inputType.getOpcode() == packet.getOpcode())
                .findFirst()
                .orElse(InputType.STRING);

        if(inputType.equals(InputType.STRING)) {
            this.value = ByteBufUtils.readString(packet.getBuffer());
        } else if(inputType.equals(InputType.INTEGER)) {
            this.value = packet.readInt();
        }

        return this;
    }

    public <R> R getValue(Class<R> type) {
        return type.cast(this.value);
    }

    public enum InputType {
        STRING(ENTER_SYNTAX_OPCODE),
        INTEGER(ENTER_AMOUNT_OPCODE);

        int opcode;

        InputType(int opcode) {
            this.opcode = opcode;
        }

        public int getOpcode() {
            return opcode;
        }
    }
}
