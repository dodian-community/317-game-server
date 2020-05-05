package net.dodian.packets.handlers.impl.npc;

import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.npc.NpcFirstOptionPacket;
import net.dodian.packets.impl.npc.NpcFourthOptionPacket;
import net.dodian.packets.impl.npc.NpcSecondOptionPacket;
import net.dodian.packets.impl.npc.NpcThirdOptionPacket;
import org.springframework.stereotype.Component;

@Component
public class NpcOptionPacketHandler implements PacketListener {

    @PacketHandler
    public void onFirstClick(NpcFirstOptionPacket packet) {

    }

    @PacketHandler
    public void onSecondClick(NpcSecondOptionPacket packet) {

    }

    @PacketHandler
    public void onThirdClick(NpcThirdOptionPacket packet) {

    }

    @PacketHandler
    public void onFourthClick(NpcFourthOptionPacket packet) {

    }
}
