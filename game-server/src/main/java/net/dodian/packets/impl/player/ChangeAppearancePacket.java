package net.dodian.packets.impl.player;

import lombok.Getter;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.GamePacket;
import net.dodian.packets.Opcodes;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.CHANGE_APPEARANCE_OPCODE;

@Component
@Getter
@Opcodes(CHANGE_APPEARANCE_OPCODE)
public class ChangeAppearancePacket extends GamePacket {

    final int[] appearances = new int[MALE_VALUES.length];
    final int[] colors = new int[ALLOWED_COLORS.length];
    private int gender;

    @Override
    public ChangeAppearancePacket createFrom(Packet packet, Player player) {
        this.player = player;
        this.gender = packet.readByte();

        for(int i = 0; i < appearances.length; i++) {
            int value = packet.readByte();
            if(value < (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]) || value > (gender == 0 ? MALE_VALUES[i][1] : FEMALE_VALUES[i][1])) {
                value = (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]);
            }
            appearances[i] = value;
        }

        for(int i = 0; i < colors.length; i++) {
            int value = packet.readByte();
            if(value < ALLOWED_COLORS[i][0] || value > ALLOWED_COLORS[i][1]) {
                value = ALLOWED_COLORS[i][0];
            }
            colors[i] = value;
        }

        return this;
    }

    private static final int[][] ALLOWED_COLORS = {
        { 0, 11 }, // hair color
        { 0, 15 }, // torso color
        { 0, 15 }, // legs color
        { 0, 5 }, // feet color
        { 0, 7 } // skin color
    };

    private static final int[][] FEMALE_VALUES = {
        { 45, 54 }, // head
        { -1, -1 }, // jaw
        { 56, 60 }, // torso
        { 61, 65 }, // arms
        { 67, 68 }, // hands
        { 70, 77 }, // legs
        { 79, 80 }, // feet
    };

    private static final int[][] MALE_VALUES = {
        { 0, 8 }, // head
        { 10, 17 }, // jaw
        { 18, 25 }, // torso
        { 26, 31 }, // arms
        { 33, 34 }, // hands
        { 36, 40 }, // legs
        { 42, 43 }, // feet
    };
}
