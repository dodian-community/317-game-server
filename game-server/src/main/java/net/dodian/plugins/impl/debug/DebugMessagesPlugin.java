package net.dodian.plugins.impl.debug;

import net.dodian.Server;
import net.dodian.events.EventHandler;
import net.dodian.events.impl.player.interact.item.PlayerItemFirstClickEvent;
import net.dodian.events.impl.player.interact.item.PlayerItemSecondClickEvent;
import net.dodian.events.impl.player.interact.item.PlayerItemThirdClickEvent;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.ButtonClickPacket;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

@Component
public class DebugMessagesPlugin extends PacketListener {

    @PacketHandler
    public void onButtonClick(ButtonClickPacket packet) {
        Server.getLogger().log(Level.INFO, "Clicked button: " + packet.getButton());
    }

    @EventHandler
    public void onItemFirstAction(PlayerItemFirstClickEvent event) {
        Server.getLogger().log(Level.INFO, "Item first click: " + event.getItem().getDefinition().getName());
    }

    @EventHandler
    public void onItemSecondAction(PlayerItemSecondClickEvent event) {
        Server.getLogger().log(Level.INFO, "Item second click: " + event.getItem().getDefinition().getName());
    }

    @EventHandler
    public void onItemThirdAction(PlayerItemThirdClickEvent event) {
        Server.getLogger().log(Level.INFO, "Item third click: " + event.getItem().getDefinition().getName());
    }
}
