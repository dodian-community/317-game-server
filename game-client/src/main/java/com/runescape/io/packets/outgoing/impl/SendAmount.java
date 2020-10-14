package com.runescape.io.packets.outgoing.impl;

import com.runescape.io.ByteBuffer;
import com.runescape.io.packets.outgoing.OutgoingPacket;

public class SendAmount implements OutgoingPacket {

    private int chat;

    public SendAmount(int chat) {
        this.chat = chat;
    }

    @Override
    public void buildPacket(ByteBuffer buf) {
        buf.putOpcode(208);
        buf.putInt(chat);
    }
}
