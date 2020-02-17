package net.dodian.extend.events.controllers;

import net.dodian.extend.events.EventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public interface ButtonClickEventListener extends EventListener {
    default void onClick(Player player, int button, Packet packet) {}
    void onClick(Player player, int button);
}
