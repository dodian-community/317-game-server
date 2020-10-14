package net.dodian.packets.handlers.impl;

import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.EnterInputPacket;
import org.springframework.stereotype.Component;

import static net.dodian.packets.PacketConstants.ENTER_AMOUNT_OPCODE;
import static net.dodian.packets.PacketConstants.ENTER_SYNTAX_OPCODE;

@Component
public class EnterInputPacketHandler extends PacketListener {

    @PacketHandler
    public void onEnterInputPacketReceived(EnterInputPacket packet) {
        if(packet.getOpcode() == ENTER_SYNTAX_OPCODE) {
            String syntax = (String) packet.getValue();

            if(syntax == null) {
                return;
            }

            if(packet.getPlayer().getEnterSyntax() != null) {
                packet.getPlayer().getEnterSyntax().handleSyntax(packet.getPlayer(), syntax);
            }
        } else if (packet.getOpcode() == ENTER_AMOUNT_OPCODE) {
            int amount = (int) packet.getValue();

            if(amount <= 0) {
                return;
            }

            if(packet.getPlayer().getEnterSyntax() != null) {
                packet.getPlayer().getEnterSyntax().handleSyntax(packet.getPlayer(), amount);
            }
        }

        packet.getPlayer().setEnterSyntax(null);
    }
}
