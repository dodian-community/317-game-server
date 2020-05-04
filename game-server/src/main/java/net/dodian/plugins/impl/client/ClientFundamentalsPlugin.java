package net.dodian.plugins.impl.client;

import net.dodian.old.definitions.WeaponInterfaces;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.ButtonClickPacket;
import org.springframework.stereotype.Component;

@Component
public class ClientFundamentalsPlugin implements PacketListener {

    @PacketHandler
    public void onLogoutButtonClick(ButtonClickPacket packet) {
        if(packet.getButton() != 2458) {
            return;
        }

        if(!packet.getPlayer().canLogout()) {
            packet.getPlayer().getPacketSender().sendMessage("You cannot log out at the moment.");
            return;
        }

        packet.getPlayer().logout();
    }

    @PacketHandler
    public void onChangeAttackStyle(ButtonClickPacket packet) {
        WeaponInterfaces.changeCombatSettings(packet.getPlayer(), packet.getButton());
    }
}
