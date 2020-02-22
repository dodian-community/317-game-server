package com.runescape.io.packets.outgoing.impl;

import com.runescape.io.ByteBuffer;
import com.runescape.io.packets.outgoing.OutgoingPacket;

public class ItemContainerOption1 implements OutgoingPacket {

	int interfaceId;
	int slot;
	int nodeId;

	public ItemContainerOption1(int interfaceId, int slot, int nodeId) {
		this.interfaceId = interfaceId;
		this.slot = slot;
		this.nodeId = nodeId;
	}

	@Override
	public void buildPacket(ByteBuffer buf) {
		buf.putOpcode(145);
		buf.putInt(interfaceId);
		buf.writeUnsignedWordA(slot);
		buf.writeUnsignedWordA(nodeId);
	}
}
