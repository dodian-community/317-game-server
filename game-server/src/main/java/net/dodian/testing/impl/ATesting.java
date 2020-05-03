package net.dodian.testing.impl;

import net.dodian.packets.impl.ButtonClickPacket;
import net.dodian.packets.impl.GameMovementPacket;
import net.dodian.testing.Listener;
import net.dodian.testing.PacketHandler;
import org.springframework.stereotype.Component;

@Component
public class ATesting implements Listener {

    @PacketHandler
    public void onButtonClick(ButtonClickPacket packet) {
        packet.getPlayer().getPacketSender().sendMessage("You tried to click button: " + packet.getButton());
    }

    @PacketHandler
    public void onPlayerWalk(GameMovementPacket packet) {
        packet.getPlayer().getPacketSender().sendMessage("You tried to walk " + packet.getSteps() + " steps.");
        //packet.cancel();
    }
}
