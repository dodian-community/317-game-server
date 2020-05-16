package net.dodian.packets.handlers.impl;

import net.dodian.old.util.PlayerPunishment;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.ChatMessage;
import net.dodian.old.world.model.Flag;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.ChatPacket;
import org.springframework.stereotype.Component;
import net.dodian.packets.PacketConstants;

@Component
public class ChatPacketHandler extends PacketListener {

    private static boolean allowChat(Player player, String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        if (PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
            player.getPacketSender().sendMessage("You are muted.");
            return false;
        }
        return true;
    }

    @PacketHandler
    public void onChat(ChatPacket packet) {
        Player player = packet.getPlayer();

        switch (packet.getOpcode()) {
            case PacketConstants.CHAT_OPCODE_1:
                int color = packet.getColor();
                int effect = packet.getEffect();
                String msg = packet.getText();

                if (!allowChat(player, msg)) {
                    return;
                }

                player.getChatMessages().set(new ChatMessage.Message(color, effect, msg));
                player.getUpdateFlag().flag(Flag.CHAT);
                break;
        }
    }
}
