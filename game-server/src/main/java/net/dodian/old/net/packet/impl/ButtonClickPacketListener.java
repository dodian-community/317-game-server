package net.dodian.old.net.packet.impl;

import net.dodian.extend.events.controllers.ButtonClickEventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.PlayerRights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.dodian.constants.InterfaceButton.LOGOUT;

/**
 * This packet listener manages a button that the player has clicked upon.
 *
 * @author Gabriel Hannason
 */
@Component
public class ButtonClickPacketListener implements PacketListener {

    private final List<ButtonClickEventListener> buttonClickEventListeners;

    @Autowired
    public ButtonClickPacketListener(List<ButtonClickEventListener> buttonClickEventListeners) {
        this.buttonClickEventListeners = buttonClickEventListeners;
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        int button = packet.readInt();

        if (player == null || player.getHitpoints() <= 0) {
            return;
        }

        if (player.getRights() == PlayerRights.DEVELOPER) {
            player.getPacketSender().sendConsoleMessage("ButtonClickPacketListener: " + button);
        }

        if(button == LOGOUT && player.canLogout()) {
			player.logout();
		} else if(button == LOGOUT && !player.canLogout()) {
			player.getPacketSender().sendMessage("You cannot log out at the moment.");
		} else {
            buttonClickEventListeners.forEach(event -> event.onClick(player, button));
            buttonClickEventListeners.forEach(event -> event.onClick(player, button, packet));
        }
    }
}
