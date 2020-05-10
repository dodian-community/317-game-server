package net.dodian.packets.handlers.impl.player;

import net.dodian.game.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.ButtonClickPacket;
import org.springframework.stereotype.Component;

@Component
public class ButtonClickPacketHandler extends PacketListener {

    @PacketHandler
    public void onButtonClick(ButtonClickPacket packet) {
        this.eventsProvider.executeListeners(new PlayerButtonClickEvent().create(packet.getPlayer(), packet.getButton()));
    }
}
